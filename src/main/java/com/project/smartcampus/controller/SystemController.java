package com.project.smartcampus.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.smartcampus.pojo.Admin;
import com.project.smartcampus.pojo.LoginInfo;
import com.project.smartcampus.pojo.Student;
import com.project.smartcampus.pojo.Teacher;
import com.project.smartcampus.service.AdminService;
import com.project.smartcampus.service.StudentService;
import com.project.smartcampus.service.TeacherService;
import com.project.smartcampus.util.*;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

/**
 * @author LISHANSHAN
 * @ClassName SystemController
 * @Description TODO
 * @date 2022/05/2022/5/16 20:40
 */
@RestController
@RequestMapping("/sms/system")
public class SystemController {
    // 一些公共的功能，放在这里

    @Autowired
    private AdminService adminService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    /**
     * Desc: 修改密码的控制器
     * @param token
     * @param oldPwd
     * @param newPwd
     * @return {@link Result}
     * @author LISHANSHAN
     * @date 2022/5/22 11:13
     */
    @ApiOperation("修改用户密码")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @ApiParam("token口令") @RequestHeader("token") String token,
            @ApiParam("旧密码") @PathVariable("oldPwd") String oldPwd,
            @ApiParam("新密码") @PathVariable("newPwd") String newPwd
    ) {

        // 判断token是否过期
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            return Result.fail().message("token失效，请重新登录");
        }
        // 获取用户ID和用户类型
        Integer userType = JwtHelper.getUserType(token);
        Integer userId = JwtHelper.getUserId(token).intValue();
        oldPwd = MD5.encrypt(oldPwd);
        newPwd = MD5.encrypt(newPwd);
        switch (userType) {
            case 1:
                QueryWrapper<Admin> queryWrapperA = new QueryWrapper<>();
                queryWrapperA.eq("id", userId).eq("password", oldPwd);
                Admin admin = adminService.getOne(queryWrapperA);
                if (null != admin) {
                    // 匹配到结果，则修改密码
                    admin.setPassword(newPwd);
                    adminService.saveOrUpdate(admin);
                } else {
                    return Result.fail().message("原密码有误，请确认后重新输入");
                }
                break;
            case 2:
                QueryWrapper<Student> queryWrapperS = new QueryWrapper<>();
                queryWrapperS.eq("id", userId).eq("password", oldPwd);
                Student student = studentService.getOne(queryWrapperS);
                if (null != student) {
                    // 匹配到结果，则修改密码
                    student.setPassword(newPwd);
                    studentService.saveOrUpdate(student);
                } else {
                    return Result.fail().message("原密码有误，请确认后重新输入");
                }
                break;
            case 3:
                QueryWrapper<Teacher> queryWrapperT = new QueryWrapper<>();
                queryWrapperT.eq("id", userId).eq("password", oldPwd);
                Teacher teacher = teacherService.getOne(queryWrapperT);
                if (null != teacher) {
                    teacher.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacher);
                } else {
                    return Result.fail().message("原密码有误，请确认后重新输入");
                }
                break;
        }
        return Result.ok();

    }

    @ApiOperation("文件上传统一入口")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
            @ApiParam("图片文件") @RequestPart("multipartFile") MultipartFile multipartFile,
            HttpServletRequest request
    ) {
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String originFileName = multipartFile.getOriginalFilename();
        int i = originFileName.lastIndexOf(".");
        String newFileName = uuid + originFileName.substring(i);

        // 保存文件，一般而言将文件发送到独立的图片服务器上，拿到url，即路径；但没有，拿到图片在服务器里的真实路径就好啦
        String url = "https://sm.ms/image/kLVMAXTP1xCFG3m";
        String path = "E:/IDEA_coding/smartcampus/target/classes/public/upload/" + newFileName;
        try {
            multipartFile.transferTo(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*multipartFile.transferTo(new File(url));

        // 相应图片路径
        String path = "upload/" + (newFileName);
        return Result.ok(path);*/
        path = "upload/".concat(newFileName);
        return Result.ok(path);
    }

    @ApiOperation("通过token口令，获取当前登录的用户信息")
    @GetMapping("/getInfo")
    public Result getInfoByToken(@ApiParam("获取token口令") @RequestHeader("token") String token){
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }

        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);
        Map<String, Object> map = new HashMap<>();
        switch (userType) {
            case 1:
                Admin admin = adminService.getAdminById(userId);
                map.put("userType", userType);
                map.put("user", admin);
                break;
            case 2:
                Student student = studentService.getStudentById(userId);
                map.put("userType", userType);
                map.put("user", student);
                break;
            case 3:
                Teacher teacher = teacherService.getTeacherById(userId);
                map.put("userType", userType);
                map.put("user", teacher);
                break;
        }
        return Result.ok(map);
    }

    @ApiOperation("进行验证码的验证")
    @PostMapping("/login")
    public Result login(@RequestBody LoginInfo loginInfo, HttpServletRequest request) {

        // 验证码是否有效，前边将验证码封装进入Session，会随着请求一起传输过来，所以是Request类型
        HttpSession session = request.getSession();
        String sessionVerifyCode = (String)session.getAttribute("verifiCode");
        String loginVerifyCode = loginInfo.getVerifiCode ();

        if ("".equals(sessionVerifyCode) || null == sessionVerifyCode) {
            return Result.fail().message("验证码失效，请刷新后重试");
        }
        if (!sessionVerifyCode.equalsIgnoreCase(loginVerifyCode)) {
            return Result.fail().message("验证码有误，请重试");
        }
        // 从session域中移除现有验证码，防止后续判断错误
        session.removeAttribute("verifiCode");

        Map<String, Object> map = new HashMap<>();
        // 分用户类型进行校验，即根据用户类型，用不同的表进行登录信息校验
        switch (loginInfo.getUserType()) {
            case 1:
                try {
                    Admin admin = adminService.login(loginInfo);
                    if (null != admin) {
                        // 将用户的类型和名称，转换为密文，以token的形式发送到前端
                        String token = JwtHelper.createToken(admin.getId().longValue(), 1);
                        map.put("token", token);
                    } else {
                        throw new RuntimeException("用户名或密码有误！");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 2:
                try {
                    Student student = studentService.login(loginInfo);
                    if (null != student) {
                        // 将用户的类型和名称，转换为密文，以token的形式发送到前端
                        String token = JwtHelper.createToken(student.getId().longValue(), 2);
                        map.put("token", token);
                    } else {
                        throw new RuntimeException("用户名或密码有误！");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 3:
                try {
                    Teacher teacher = teacherService.login(loginInfo);
                    if (null != teacher) {
                        // 将用户的类型和名称，转换为密文，以token的形式发送到前端
                        String token = JwtHelper.createToken(teacher.getId().longValue(), 3);
                        map.put("token", token);
                    } else {
                        throw new RuntimeException("用户名或密码有误！");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }
        return Result.fail().message("您尚未注册，请先注册！");
    }

    @RequestMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response) {
        // 获取图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        // 获取图片上的验证码
        String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());
        // 将验证码上的文本放入session域，使服务器可以判断验证码的正确性
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode", verifiCode);
        // 将验证码响应到浏览器
        try {
            // 将验证码图片以JEPG格式写到输出流，响应到浏览器
            ImageIO.write(verifiCodeImage, "JPEG", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
