package com.project.smartcampus.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.project.smartcampus.pojo.Grade;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author LISHANSHAN
* @description 针对表【tb_grade】的数据库操作Service
* @createDate 2022-05-16 00:34:44
*/
public interface GradeService extends IService<Grade> {

    /**
     * Desc: 按照班级名进行查询，并将结果分页展示
     * @param page
     * @param gradeName
     * @return {@link IPage< Grade>}
     * @author LISHANSHAN
     * @date 2022/5/18 21:13
     */
    IPage<Grade> getGradeByOpr(Page<Grade> page, String gradeName);

    /**
     * Desc: 获取全部年级信息
     * @param
     * @return {@link List< Grade>}
     * @author LISHANSHAN
     * @date 2022/5/18 22:29
     */
    List<Grade> getGrades();
}
