package com.project.smartcampus.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.smartcampus.pojo.Clazz;
import com.project.smartcampus.service.ClazzService;
import com.project.smartcampus.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author LISHANSHAN
 * @ClassName ClazzController
 * @Description TODO
 * @date 2022/05/2022/5/16 19:13
 */
@Api(tags = "班级管理控制器")
@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {

    @Autowired
    ClazzService clazzService;

    @ApiOperation("查询所有班级信息")
    @GetMapping("/getClazzs")
    public Result getClasses() {
        List<Clazz> classes = clazzService.getClazzs();

        return Result.ok(classes);
    }

    @ApiOperation("分页带条件查询班级信息")
    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzByOpr(
            @ApiParam("页码数") @PathVariable("pageNo") Integer pageNo,
            @ApiParam("每页的容量") @PathVariable("pageSize") Integer pageSize,
            @ApiParam("分页查询的查询条件") Clazz clazz
    ) {
        Page<Clazz> page = new Page<>(pageNo, pageSize);
        IPage<Clazz> clazzes = clazzService.getClazzesByOpr(page, clazz);

        return Result.ok(clazzes);
    }

    @ApiOperation("修改或新增班级信息")
    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClazz(@ApiParam("将请求体中的JSON数据传输进入clazz对象中")@RequestBody Clazz clazz) {
        clazzService.saveOrUpdate(clazz);
        return Result.ok();
    }

    @ApiOperation("删除一个或多个班级信息")
    @DeleteMapping("/deteleClazz")
    public Result deleteClazz(@ApiParam("删除的班级信息") @RequestBody List<Integer> clazzIds) {
        clazzService.removeByIds(clazzIds);
        return Result.ok();
    }
}
