package com.project.smartcampus.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author LISHANSHAN
 * @ClassName MD5
 * @Description TODO
 * @date 2022/05/2022/5/15 20:34
 */

public final class MD5 {

    /**
     * Desc: 将明文转换为密文
     * @param strSrc
     * @return {@link String}
     * @author LISHANSHAN
     * @date 2022/5/15 20:41
     */
    public static String encrypt(String strSrc) {
        try {
            char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};
            byte[] bytes = strSrc.getBytes();
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 相当于为messageDigest实例赋值
            messageDigest.update(bytes);
            // 返回生成的byte[16]数组
            bytes = messageDigest.digest();
            int j = bytes.length;
            char[] chars = new char[j * 2];
            int k = 0;

            for (int i = 0; i < bytes.length; i++) {
                // 一个b是8位
                byte b = bytes[i];
                // b的高4位
                chars[k++] = hexChars[b >>> 4 & 0xf];
                // b的低4位
                chars[k++] = hexChars[b & 0xf];
            }
            return new String(chars);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("MD5加密出错！" + e);
        }
    }
}
