package com.project.smartcampus.config;

import com.google.common.base.Predicates;
import io.swagger.annotations.ApiOperation;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * @author LISHANSHAN
 * @ClassName Swagger2Config
 * @Description TODO
 * @date 2022/05/2022/5/14 22:10
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {

    @Bean
    public Docket webApiConfig() {

        // 添加head参数start
        List<Parameter> parameters = new ArrayList<>();
        ParameterBuilder tokenParameter = new ParameterBuilder();
        tokenParameter.name("userId")
                .description("用户ID")
                .defaultValue("1")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        parameters.add(tokenParameter.build());

        ParameterBuilder tmpParameter = new ParameterBuilder();
        tmpParameter.name("userTempId")
                .description("临时用户ID")
                .defaultValue("1")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
        parameters.add(tmpParameter.build());

        return new Docket(DocumentationType.SWAGGER_2)
                .groupName("webApi")
                .apiInfo(webApiInfo())
                .select()
                // 可以测试请求头中，输入token
                // .apis(RequestHandlerSelectors.withClassAnnotation(ApiOperation.class))
                // 扫描参数中的包，生成swagger-API文档
                .apis(RequestHandlerSelectors.basePackage("com.project.smartcampus.controller"))
                // 过滤掉admin路径下的所有页面
                // .paths(Predicates.and(PathSelectors.regex("/sms/.*")))
                .build()
                .globalOperationParameters(parameters);
    }

    private ApiInfo webApiInfo() {
        return new ApiInfoBuilder()
                .title("网站-API文档")
                .description("本文档描述了网站微服务接口定义")
                .version("1.0")
                .build();
    }

    private ApiInfo adminApiInfo() {
        return new ApiInfoBuilder()
                .title("后台管理系统-API文档")
                .description("本文档描述了后台管理系统微服务接口定义")
                .version("1.0")
                .build();
    }
}
