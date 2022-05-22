package com.project.smartcampus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.project.smartcampus.pojo.Admin;
import com.project.smartcampus.pojo.LoginInfo;
import com.project.smartcampus.service.AdminService;
import com.project.smartcampus.mapper.AdminMapper;
import com.project.smartcampus.util.MD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

/**
* @author LISHANSHAN
* @description 针对表【tb_admin】的数据库操作Service实现
* @createDate 2022-05-16 00:33:42
*/
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
    implements AdminService{

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public IPage<Admin> getAdmins(Page<Admin>pageParams, String adminName) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(adminName)) {
            queryWrapper.like("name", adminName);
        }

        queryWrapper.orderByDesc("id").orderByAsc("name");
        Page page = baseMapper.selectPage(pageParams, queryWrapper);

        return page;
    }

    @Override
    public Admin login(LoginInfo loginInfo) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", loginInfo.getUsername())
                .eq("password", MD5.encrypt(loginInfo.getPassword()));
        Admin admin = adminMapper.selectOne(queryWrapper);
        return admin;
    }

    @Override
    public Admin getAdminById(Long userId) {
        Admin admin = adminMapper.selectById(userId);
        return admin;
    }
}




