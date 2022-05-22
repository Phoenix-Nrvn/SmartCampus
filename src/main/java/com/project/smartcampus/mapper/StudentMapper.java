package com.project.smartcampus.mapper;

import com.project.smartcampus.pojo.Student;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
* @author LISHANSHAN
* @description 针对表【tb_student】的数据库操作Mapper
* @createDate 2022-05-16 00:35:01
* @Entity com.project.smartcampus.pojo.Student
*/
@Repository
public interface StudentMapper extends BaseMapper<Student> {

}




