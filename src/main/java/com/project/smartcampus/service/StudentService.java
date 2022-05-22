package com.project.smartcampus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.smartcampus.pojo.LoginInfo;
import com.project.smartcampus.pojo.Student;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author LISHANSHAN
* @description 针对表【tb_student】的数据库操作Service
* @createDate 2022-05-16 00:35:01
*/
public interface StudentService extends IService<Student> {

    /**
     * Desc: 根据用户名和密码去匹配数据库中的学生信息
     * @param loginInfo
     * @return {@link Student}
     * @author LISHANSHAN
     * @date 2022/5/17 20:11
     */
    Student login(LoginInfo loginInfo);

    /**
     * Desc: 根据ID获得整条学生信息
     * @param userId
     * @return {@link Student}
     * @author LISHANSHAN
     * @date 2022/5/17 22:17
     */
    Student getStudentById(Long userId);

    /**
     * Desc: 分页查询
     * @param pageParam
     * @param student
     * @return {@link IPage< Student>}
     * @author LISHANSHAN
     * @date 2022/5/21 17:42
     */
    IPage<Student> getStudentByOpr(Page<Student> pageParam, Student student);
}
