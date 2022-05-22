package com.project.smartcampus.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.smartcampus.pojo.Student;
import com.project.smartcampus.service.StudentService;
import com.project.smartcampus.util.CreateVerifiCodeImage;
import com.project.smartcampus.util.MD5;
import com.project.smartcampus.util.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

/**
 * @author LISHANSHAN
 * @ClassName StudentController
 * @Description TODO
 * @date 2022/05/2022/5/16 19:14
 */
@RestController
@RequestMapping("/sms/studentController")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @ApiOperation("分页带条件查询学生信息")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentByOpr(
            @ApiParam("页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("每页的容量") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("查询的条件") Student student
    ) {
        Page<Student> pageParam = new Page<>(pageNo, pageSize);
        IPage<Student> studentPage = studentService.getStudentByOpr(pageParam, student);
        return Result.ok(studentPage);
    }

    @ApiParam("修改或新增学生信息")
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(@ApiParam("提取请求体中的学生信息")@RequestBody Student student) {
        Integer id = student.getId();
        if (null == id || id == 0) {
            student.setPassword(MD5.encrypt(student.getPassword()));
        }
        studentService.saveOrUpdate(student);
        return Result.ok();
    }

    @ApiOperation("删除一个或多个学生信息")
    @DeleteMapping("/delStudentById")
    public Result deleteStudent(@ApiParam("提取请求体中的学生id") @RequestBody List<Integer> ids) {
        studentService.removeByIds(ids);
        return Result.ok();
    }

}
