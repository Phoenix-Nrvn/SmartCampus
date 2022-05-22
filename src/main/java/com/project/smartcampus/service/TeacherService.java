package com.project.smartcampus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.smartcampus.pojo.LoginInfo;
import com.project.smartcampus.pojo.Teacher;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author LISHANSHAN
* @description 针对表【tb_teacher】的数据库操作Service
* @createDate 2022-05-16 00:35:07
*/
public interface TeacherService extends IService<Teacher> {

    /**
     * Desc: 根据用户名和密码去匹配数据库中的用户
     * @param loginInfo
     * @return {@link Teacher}
     * @author LISHANSHAN
     * @date 2022/5/17 20:10
     */
    Teacher login(LoginInfo loginInfo);

    /**
     * Desc: 根据ID获取一条教师信息
     * @param userId
     * @return {@link Teacher}
     * @author LISHANSHAN
     * @date 2022/5/17 22:17
     */
    Teacher getTeacherById(Long userId);

    /**
     * Desc: 分页待条件查询教师信息
     * @return {@link IPage< Teacher>}
     * @author LISHANSHAN
     * @date 2022/5/22 1:56
     */
    IPage<Teacher> getTeachersByOpr(Page<Teacher> pageParam, Teacher teacher);



}
