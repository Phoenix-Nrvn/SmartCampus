package com.project.smartcampus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.smartcampus.pojo.Clazz;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author LISHANSHAN
* @description 针对表【tb_clazz】的数据库操作Service
* @createDate 2022-05-16 00:34:35
*/
public interface ClazzService extends IService<Clazz> {

    /**
     * Desc: 通过班级名称获取班级信息
     * @param page
     * @param clazz
     * @return {@link IPage< Clazz>}
     * @author LISHANSHAN
     * @date 2022/5/18 22:47
     */
    IPage<Clazz> getClazzesByOpr(Page<Clazz> page, Clazz clazz);

    /**
     * Desc: 查询所有的班级信息
     * @param
     * @return {@link List< Clazz>}
     * @author LISHANSHAN
     * @date 2022/5/21 17:19
     */
    List<Clazz> getClazzs();
}
