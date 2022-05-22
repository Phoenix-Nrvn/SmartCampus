package com.project.smartcampus.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author LISHANSHAN
 * @ClassName AuthContextHolder
 * @Description 解析request请求中的token口令的工具AuthContextHolder
 * @date 2022/05/2022/5/15 20:27
 */

public class AuthContextHolder {

    /**
     * Desc: 从请求头token获取userId
     * @param request
     * @return {@link Long}
     * @author LISHANSHAN
     * @date 2022/5/15 20:29
     */
    public static Long getUserIdToken(HttpServletRequest request) {
        String token = request.getHeader("token");
        // 调用工具类
        Long userId = JwtHelper.getUserId(token);

        return userId;
    }

    /**
     * Desc: 从请求头token获取type
     * @param request
     * @return {@link String}
     * @author LISHANSHAN
     * @date 2022/5/15 20:31
     */
    public static Integer getUserType(HttpServletRequest request) {
        String token = request.getHeader("token");
        Integer userType = JwtHelper.getUserType(token);

        return userType;
    }

}
