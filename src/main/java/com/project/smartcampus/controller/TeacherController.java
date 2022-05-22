package com.project.smartcampus.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.smartcampus.pojo.Teacher;
import com.project.smartcampus.service.TeacherService;
import com.project.smartcampus.util.MD5;
import com.project.smartcampus.util.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author LISHANSHAN
 * @ClassName TeacherController
 * @Description TODO
 * @date 2022/05/2022/5/16 19:14
 */
@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    @ApiOperation("分页待条件查询教师信息")
    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result getTeachers(
            @ApiParam("页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("页面大小") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("请求查询条件") Teacher teacher
    ) {
        Page<Teacher> pageParam = new Page<>(pageNo, pageSize);
        IPage<Teacher> page = teacherService.getTeachersByOpr(pageParam, teacher);

        return Result.ok(page);
    }

    @ApiOperation("新增或修改教师信息")
    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(@ApiParam("提取请求体中教师信息") @RequestBody Teacher teacher) {
        Integer id = teacher.getId();
        if (null == id || id == 0) {
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }
        teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }

    @ApiOperation("删除教师信息，支持批量")
    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(@ApiParam("提取请求体中教师id") @RequestBody List<Integer> ids) {
        teacherService.removeByIds(ids);
        return Result.ok();
    }
}
