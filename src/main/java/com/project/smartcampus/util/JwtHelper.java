package com.project.smartcampus.util;

import io.jsonwebtoken.*;
import org.thymeleaf.util.StringUtils;

import java.util.Date;

/**
 * @author LISHANSHAN
 * @ClassName JwtHelper
 * @Description TODO
 * @date 2022/05/2022/5/14 23:10
 */

public class JwtHelper {

    private static long tokenExpiration = 24 * 60 * 60 * 1000;
    private static String tokenSignKey = "123456";

    // 生成token字符串
    public static String createToken(Long userId, Integer userType) {
        String token = Jwts.builder()
                .setSubject("YYHH-USER")
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration))
                .claim("userId", userId)
                // .claim("userName", userName)
                .claim("userType", userType)

                // 生成签名的算法
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
                .compressWith(CompressionCodecs.GZIP)
                .compact();
        return token;
    }

    /**
     * Desc: 从token字符串获取userId
     * @param token
     * @return {@link Long}
     * @author LISHANSHAN
     * @date 2022/5/14 23:22
     */
    public static Long getUserId(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        Integer userId = (Integer)claims.get("userId");
        return userId.longValue();
    }

   /**
    * Desc: 从token字符串获取userType
    * @param token
    * @return {@link Integer}
    * @author LISHANSHAN
    * @date 2022/5/14 23:23
    */
    public static Integer getUserType(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Jws<Claims> claimsJws = Jwts.parser().setSigningKey(tokenSignKey).parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        return (Integer) (claims.get("userType"));
    }

    /**
     * Desc: 判断token是否有效
     * @param token
     * @return {@link boolean}
     * @author LISHANSHAN
     * @date 2022/5/17 22:07
     */
    public static boolean isExpiration(String token) {
        try {
            boolean isExpire = Jwts.parser()
                    .setSigningKey(tokenSignKey)
                    .parseClaimsJws(token)
                    .getBody()
                    .getExpiration()
                    .before(new Date());
            return isExpire;
        } catch (Exception e) {
            return true;
        }
    }

    /**
     * Desc: 刷新token
     * @param token
     * @return {@link String}
     * @author LISHANSHAN
     * @date 2022/5/17 22:07
     */
    public String refreshToken(String token) {
        String refreshToken;
        try {
            final Claims claims = Jwts.parser()
                    .setSigningKey(tokenSignKey)
                    .parseClaimsJws(token)
                    .getBody();
            refreshToken = JwtHelper.createToken(getUserId(token), getUserType(token));
        } catch (Exception e) {
            refreshToken = null;
        }
        return refreshToken;
    }
}
