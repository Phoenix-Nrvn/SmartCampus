package com.project.smartcampus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.smartcampus.pojo.Grade;
import com.project.smartcampus.service.GradeService;
import com.project.smartcampus.mapper.GradeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.List;

/**
* @author LISHANSHAN
* @description 针对表【tb_grade】的数据库操作Service实现
* @createDate 2022-05-16 00:34:44
*/
@Service
public class GradeServiceImpl extends ServiceImpl<GradeMapper, Grade>
        implements GradeService{

    @Autowired
    GradeMapper gradeMapper;

    @Override
    public IPage<Grade> getGradeByOpr(Page<Grade> page, String gradeName) {
        QueryWrapper<Grade> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(gradeName)) {
            queryWrapper.like("name", gradeName);
        }
        queryWrapper.orderByDesc("id");
        Page<Grade> gradePage = gradeMapper.selectPage(page, queryWrapper);

        return gradePage;
    }

    @Override
    public List<Grade> getGrades() {

        List<Grade> grades = gradeMapper.selectList(null);
        return grades;
    }
}




