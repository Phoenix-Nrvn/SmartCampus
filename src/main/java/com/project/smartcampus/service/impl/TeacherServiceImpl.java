package com.project.smartcampus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.smartcampus.pojo.LoginInfo;
import com.project.smartcampus.pojo.Teacher;
import com.project.smartcampus.service.TeacherService;
import com.project.smartcampus.mapper.TeacherMapper;
import com.project.smartcampus.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

/**
* @author LISHANSHAN
* @description 针对表【tb_teacher】的数据库操作Service实现
* @createDate 2022-05-16 00:35:07
*/
@Service
public class TeacherServiceImpl extends ServiceImpl<TeacherMapper, Teacher>
    implements TeacherService{

    @Autowired
    private TeacherMapper teacherMapper;

    @Override
    public Teacher login(LoginInfo loginInfo) {

        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", loginInfo.getUsername())
                        .eq("password", MD5.encrypt(loginInfo.getPassword()));
        Teacher teacher = teacherMapper.selectOne(queryWrapper);
        return teacher;
    }

    @Override
    public Teacher getTeacherById(Long userId) {
        Teacher teacher = teacherMapper.selectById(userId);
        return teacher;
    }

    @Override
    public IPage<Teacher> getTeachersByOpr(Page<Teacher> pageParam, Teacher teacher) {
        QueryWrapper<Teacher> queryWrapper = new QueryWrapper<>();
        String name = teacher.getName();
        String clazzName = teacher.getClazzName();

        if (!StringUtils.isEmpty(name)) {
            queryWrapper.like("name", name);
        }
        if(!StringUtils.isEmpty(clazzName)) {
            queryWrapper.like("clazz_name", clazzName);
        }
        queryWrapper.orderByDesc("id");

        Page<Teacher> teacherPage = teacherMapper.selectPage(pageParam, queryWrapper);

        return teacherPage;
    }

}




