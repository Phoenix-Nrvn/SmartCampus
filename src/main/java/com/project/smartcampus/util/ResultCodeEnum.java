package com.project.smartcampus.util;

import lombok.Getter;

/**
 * @author LISHANSHAN
 * @ClassName ResultCodeEnum
 * @Description TODO
 * @date 2022/05/2022/5/15 22:52
 */
@Getter
public enum ResultCodeEnum {

    SUCCESS(200, "成功"),
    FAIL(201, "失败"),
    SERVICE_ERROR(2012, "服务异常"),
    ILLEGAL_REOUEST(204, "非法请求"),
    ARGUMENT_VALID_ERROR(206, "参数校验错误"),
    LOGIN_CODE(222,"长时间未操作,会话已失效,请刷新页面后重试!"),
    CODE_ERROR(223,"验证码错误!"),
    TOKEN_ERROR(224,"Token无效!");

    private Integer code;

    private String message;

    ResultCodeEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
