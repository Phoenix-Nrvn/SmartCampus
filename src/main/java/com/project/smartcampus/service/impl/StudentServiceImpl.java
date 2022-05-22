package com.project.smartcampus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.smartcampus.pojo.Admin;
import com.project.smartcampus.pojo.LoginInfo;
import com.project.smartcampus.pojo.Student;
import com.project.smartcampus.service.StudentService;
import com.project.smartcampus.mapper.StudentMapper;
import com.project.smartcampus.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

/**
* @author LISHANSHAN
* @description 针对表【tb_student】的数据库操作Service实现
* @createDate 2022-05-16 00:35:01
*/
@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student>
    implements StudentService{

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public Student login(LoginInfo loginInfo) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", loginInfo.getUsername())
                .eq("password", MD5.encrypt(loginInfo.getPassword()));
        Student student = studentMapper.selectOne(queryWrapper);
        return student;
    }

    @Override
    public Student getStudentById(Long userId) {
        Student student = studentMapper.selectById(userId);
        return student;
    }

    @Override
    public IPage<Student> getStudentByOpr(Page<Student> pageParam, Student student) {
        QueryWrapper<Student> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(student.getClazzName())) {
            String clazzName = student.getClazzName();
            queryWrapper.like("clazz_name", clazzName);
        }
        if (!StringUtils.isEmpty(student.getName())) {
            String name = student.getName();
            queryWrapper.like("name", name);
        }
        queryWrapper.orderByDesc("id");
        Page<Student> studentPage = studentMapper.selectPage(pageParam, queryWrapper);

        return studentPage;
    }


}




