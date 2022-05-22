package com.project.smartcampus.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.smartcampus.pojo.Grade;
import com.project.smartcampus.service.GradeService;
import com.project.smartcampus.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author LISHANSHAN
 * @ClassName GradeController
 * @Description TODO
 * @date 2022/05/2022/5/16 19:13
 */
@Api(tags = "年级控制器")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @ApiOperation("在班级管理的搜索框，获取全部年级")
    @GetMapping("/getGrades")
    public Result getGrades() {
        List<Grade> grades = gradeService.getGrades();
        return Result.ok(grades);
    }

    @ApiOperation("查询年级信息")
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrade(
            @ApiParam("当前页的页码数") @PathVariable Integer pageNo,
            @ApiParam("每页的容量") @PathVariable Integer pageSize,
            @ApiParam("年级的名称，进行模糊匹配") String gradeName
    ) {
        // 分页
        Page<Grade> page = new Page<>(pageNo, pageSize);
        // 在业务逻辑层进行查询以及分页，将结果返回前端
        IPage<Grade> grade = gradeService.getGradeByOpr(page, gradeName);
        // 封装Result对象并返回
        return Result.ok(grade);
    }

    @ApiOperation("新增或修改年级信息，有id为修改，无id为新增")
    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(@ApiParam("将JSON格式的数据转换为Grade对象") @RequestBody Grade grade) {
        // 接收参数，调用服务层方法实现增加或修改功能
        gradeService.saveOrUpdate(grade);
        return Result.ok();
    }

    @DeleteMapping("deleteGrade")
    public Result deleteGrade(@RequestBody List<Integer> gradeIds) {

        gradeService.removeByIds(gradeIds);
        return Result.ok();
    }
}
