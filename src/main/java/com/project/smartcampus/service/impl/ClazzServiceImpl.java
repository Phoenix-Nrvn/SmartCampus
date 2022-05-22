package com.project.smartcampus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.smartcampus.pojo.Clazz;
import com.project.smartcampus.service.ClazzService;
import com.project.smartcampus.mapper.ClazzMapper;
import com.project.smartcampus.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.util.List;

/**
* @author LISHANSHAN
* @description 针对表【tb_clazz】的数据库操作Service实现
* @createDate 2022-05-16 00:34:35
*/
@Service
public class ClazzServiceImpl extends ServiceImpl<ClazzMapper, Clazz>
    implements ClazzService{

    @Autowired
    ClazzMapper clazzMapper;

    @Override
    public IPage<Clazz> getClazzesByOpr(Page<Clazz> page, Clazz clazz) {

        QueryWrapper<Clazz> queryWrapper = new QueryWrapper<>();
        String gradeName = clazz.getGradeName();
        String name = clazz.getName();
        if (!StringUtils.isEmpty(gradeName)) {
            queryWrapper.like("grade_name", gradeName);
        }
        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        queryWrapper.orderByDesc("id");

        Page<Clazz> clazzPage = clazzMapper.selectPage(page, queryWrapper);
        return clazzPage;
    }

    @Override
    public List<Clazz> getClazzs() {
        List<Clazz> clazzes = clazzMapper.selectList(null);
        return clazzes;
    }
}




