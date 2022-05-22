package com.project.smartcampus.pojo;

import lombok.Data;

/**
 * @author LISHANSHAN
 * @ClassName LoginInfo
 * @Description 使用对象传递登录界面的用户信息
 * @date 2022/05/2022/5/16 00:50
 */
@Data
public class LoginInfo {

    private String username;

    private String password;

    private String verifiCode;

    private Integer userType;
}
