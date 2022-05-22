package com.project.smartcampus.util;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author LISHANSHAN
 * @ClassName Result
 * @Description TODO
 * @date 2022/05/2022/5/15 22:31
 */
@Data
@ApiModel(value = "全局统一返回结果")
public class Result<T> {

    @ApiModelProperty(value = "返回码")
    private Integer code;

    @ApiModelProperty(value = "返回消息")
    private String message;

    @ApiModelProperty(value = "返回数据")
    private T data;

    public Result(){}

    protected static <T> Result<T> build(T data){
        Result<T> result = new Result<>();
        if (data != null) {
            result.setData(data);
        }
        return result;
    }

    public static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum) {
        Result<T> result = build(body);
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        return result;
    }

    /**
     * Desc: 操作成功
     * @param
     * @return {@link Result<T>}
     * @author LISHANSHAN
     * @date 2022/5/15 22:45
     */
    public static <T> Result<T> ok() {
        return Result.ok(null);
    }

    public static <T> Result<T> ok(T data) {
        Result<T> result = build(data);
        return build(data, ResultCodeEnum.SUCCESS);
    }

    /**
     * Desc: 操作失败
     * @param
     * @return {@link Result<T>}
     * @author LISHANSHAN
     * @date 2022/5/15 22:45
     */
    public static <T> Result<T> fail() {
        return Result.fail(null);
    }

    public static <T> Result<T> fail(T data) {
        Result<T> result = build(data);
        return build(data, ResultCodeEnum.FAIL);
    }

    public Result<T> message(String message) {
        this.message = message;
        return this;
    }

    public Result<T> code(Integer code) {
        this.setCode(code);
        return this;
    }

    public boolean isOk() {
        if (this.getCode().intValue() == ResultCodeEnum.SUCCESS.getCode().intValue()) {
            return true;
        }
        return false;
    }
}
