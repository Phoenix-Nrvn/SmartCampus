package com.project.smartcampus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.smartcampus.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.smartcampus.pojo.LoginInfo;

/**
* @author LISHANSHAN
* @description 针对表【tb_admin】的数据库操作Service
* @createDate 2022-05-16 00:33:42
*/

public interface AdminService extends IService<Admin> {

    /**
     * Desc: 查询管理员信息，分页带条件
     * @param pageParam
     * @param adminName
     * @return {@link IPage< Admin>}
     * @author LISHANSHAN
     * @date 2022/5/16 20:18
     */
    IPage<Admin> getAdmins(Page<Admin> pageParam, String adminName);

    /**
     * Desc: 根据获取的用户信息判断用户是否在数据库中
     * @param loginInfo
     * @return {@link Admin}
     * @author LISHANSHAN
     * @date 2022/5/17 20:01
     */
    Admin login(LoginInfo loginInfo);

    /**
     * Desc: 根据ID获取用户
     * @param userId
     * @return {@link Admin}
     * @author LISHANSHAN
     * @date 2022/5/17 22:13
     */
    Admin getAdminById(Long userId);
}
