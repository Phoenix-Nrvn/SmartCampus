package com.project.smartcampus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.project.smartcampus.mapper")
public class SmartcampusApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmartcampusApplication.class, args);
    }

}
