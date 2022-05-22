package com.project.smartcampus.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.smartcampus.pojo.Admin;
import com.project.smartcampus.service.AdminService;
import com.project.smartcampus.util.MD5;
import com.project.smartcampus.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author LISHANSHAN
 * @ClassName AdminController
 * @Description TODO
 * @date 2022/05/2022/5/16 19:13
 */
@Api(tags="系统管理员控制器")
@RestController
@RequestMapping("/sms/adminController")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @ApiOperation("分页获取所有的Admin信息")
    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmins(@ApiParam("页码数") @PathVariable Integer pageNo,
                               @ApiParam("页大小") @PathVariable Integer pageSize,
                               @ApiParam("管理员姓名") String adminName) {
        Page<Admin> pageParam = new Page<>(pageNo, pageSize);
        IPage<Admin> page = adminService.getAdmins(pageParam, adminName);
        return Result.ok(page);
    }

    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(@RequestBody Admin admin) {
        Integer id = admin.getId();
        if (null == id || id == 0) {
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }

    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(@RequestBody List<Integer> ids) {
        adminService.removeByIds(ids);
        return Result.ok();
    }

}
