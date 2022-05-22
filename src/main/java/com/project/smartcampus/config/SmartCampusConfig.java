package com.project.smartcampus.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author LISHANSHAN
 * @ClassName SmartCampusConfig
 * @Description TODO
 * @date 2022/05/2022/5/14 22:07
 */
@Configuration
@MapperScan("com.project.smartcampus.mapper")
public class SmartCampusConfig {
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }
}
