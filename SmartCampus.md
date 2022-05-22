---
title: SmartCampus
date: 2022-05-15 21:16:14
tags: 智慧校园
---

# 1. 前置

## 1. MD5加密算法

**MD5以512位分组来处理输入的信息，且每一分组又被划分为16个32位子分组，经过了一系列的处理后，算法的输出由四个32位分组组成，将这四个32位分组级联后将生成一个128位散列值。**

将这128bit转换成常见的32个表示16进制的数字或字母的字符形式。

MessageDigest的类方法返回的这128bit是一个byte[ ]，一个byte有8个bit。128 / 8 = 16，也就是说返回的是byte[16]的数组，其中任意byte[i]有8个bit。

之后就是要把这个byte[16]转换为含有32个字符的String串，也就是每4位bit要转换为一个字符。

使用每个byte[i]的高4位和低4位的十进制作为String数组的下标，对应两个十六进制的字符。

![image-20220515222448141](https://s2.loli.net/2022/05/15/qX2S3645iPteKrs.png)

### 1. 加密的特点

1. 不可逆：当MD5对密码加密后是不能逆向解码的，保证了数据的安全
2. 适用广泛：对所有的字符串都能加密成32位的字符串

### 2. salt

由于MD5碰撞，使得MD5加密也变得不靠谱。也就是说，通过不断地尝试，可以获得原始密码。

#### MD5碰撞

使用穷举法对密文进行穷举，最终得到一个和密文一样的字符串，确定原始的密码

#### 具体操作

引进了加盐(salt，即干扰项)的操作

1. 先在密码中加入一串字符串，然后再使用MD5算法得到一个32位的字符串

这样就算解密出来了，也不知道哪几个是密码。

2. Java中的MessageDigest类封装了MD5和SHA算法，调用 .getInstance("MD5")(生成实例，即返回 实现 指定的摘要算法的 对象，会抛出算法不存在的异常)，来使用MD5算法

调用MD5算法，即返回16个byte类型的值。

3. update( )方法处理数据，使用指定的byte数组更新摘要。update传入的参数是字节类型或字节类型数组，对于字符串，需要先使用getBytes( )方法生成字符串数组。

4. 所需要更新的数据都被更新后，调用digest方法，通过执行诸如填充之类的最终操作完成Hash计算，调用之后，摘要被重置为初始状态。

执行MessageDigest对象的digest( )方法完成计算，计算的结果通过字节类型的数组返回。

## 2. 其他

### @RestController

是@ResponseBody和@Controller的合集，表示这是一个控制层的bean，并且是将函数的返回值直接填入HTTP响应体中，是一个REST风格的控制器。

返回JSON或XML形式的对象数据，直接写入HTTP响应中，属于RESTful Web 服务，是前后端分离最常用的情况。

待补充。

# 2. 业务实现

## 1. 工具类包

### 1. 生成验证码图片

Graphics对象封装了Java支持的基本呈现操作所需的状态信息，是用于创建图形图像的对象。

```java
/**
 * @author LISHANSHAN
 * @ClassName CreateVerifiCodeImage
 * @Description 验证码图片工具类
 * @date 2022/05/2022/5/14 22:25
 */

public class CreateVerifiCodeImage {

    private static int WIDTH = 90;
    private static int HEIGHT = 35;
    private static int FONT_SIZE = 20;
    private static char[] verifiCode;
    private static BufferedImage verifiCodeImage;

    /**
     * Desc: 获取验证码图片
     * @return {@link BufferedImage}
     * @author LISHANSHAN
     * @date 2022/5/14 22:35
     */
    public static BufferedImage getVerifiCodeImage(){
        // create a image
        verifiCodeImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = verifiCodeImage.getGraphics();

        // 生成文字验证码
        verifiCode = generateCheckCode();
        // 绘制背景图片
        drawBackground(graphics);
        // 绘制随机点
        drawRands(graphics, verifiCode);
        graphics.dispose();

        return verifiCodeImage;
    }

    /**
     * Desc: 获取验证码，用于后台进行验证码的验证
     * @return {@link char[]}
     * @author LISHANSHAN
     * @date 2022/5/14 22:35
     */
    public static char[] getVerifiCode() {
        return verifiCode;
    }

    /**
     * Desc: 随机生成验证码
     * @return {@link char[]}
     * @author LISHANSHAN
     * @date 2022/5/14 22:35
     */
    private static char[] generateCheckCode() {
        // 可用字符范围
        String chars = "0123456789abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] rands = new char[4];
        // 随机挑选4个字符生成验证码文字
        for (int i = 0; i < 4; i++) {
            int rand = (int) (Math.random() * (10 + 26 * 2));
            rands[i] = chars.charAt(rand);
        }
        return rands;
    }

    /**
     * Desc: 绘制验证码
     * @param g
     * @param rands
     * @author LISHANSHAN
     * @date 2022/5/14 22:35
     */
    private static void drawRands(Graphics g, char[] rands) {
        g.setFont(new Font("Console", Font.BOLD, FONT_SIZE));

        for (int i = 0; i < rands.length; i++) {
            g.setColor(getRandomColor());
            g.drawString("" + rands[i], i * FONT_SIZE + 10, 25);
        }
    }

    /**
     * Desc: 绘制验证码图片背景
     * @param g
     * @author LISHANSHAN
     * @date 2022/5/14 22:57
     */
    private static void drawBackground(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // 绘制验证码干扰点
        for (int i = 0; i < 200; i++) {
            int x = (int) (Math.random() * WIDTH);
            int y = (int) (Math.random() * HEIGHT);
            g.setColor(getRandomColor());
            g.drawOval(x, y, 1, 1);
        }
    }

    /**
     * Desc: 获取随机颜色
     * @return Color
     * @author LISHANSHAN
     * @date 2022/5/14 23:02
     */
    private static Color getRandomColor() {
        Random random = new Random();
        return new Color(random.nextInt(220), random.nextInt(220), random.nextInt(220));
    }
}
```

### 2. MD5算法加密密码

```java
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
            // 确定密文可选的字符范围
            char[] hexChars = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
            'a', 'b', 'c', 'd', 'e', 'f'};
            // 将字符串密码转换为字节类型的数组
            byte[] bytes = strSrc.getBytes();
            // 使用MD5算法，即为messageDigest实例赋值
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            // 使用指定的字符数组更新摘要
            messageDigest.update(bytes);
            // 返回生成的byte[16]数组，注意：一个字节八个字
            bytes = messageDigest.digest();
            int j = bytes.length;
            // 生成了长度为32位的加密字符数组，一个字节，两个四位，可生成两个十六进制的下标，共16个字节，故为32位
            char[] chars = new char[j * 2];
            int k = 0;

            for (int i = 0; i < bytes.length; i++) {
                // 一个b是8位
                byte b = bytes[i];
                // 取b的高4位，生成第一个十六进制的下标
                chars[k++] = hexChars[b >>> 4 & 0xf];
                // b的低4位，生成第二个十六进制的下标
                chars[k++] = hexChars[b & 0xf];
            }
            // 将字符数组转换为字符串类型后返回
            return new String(chars);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("MD5加密出错！" + e);
        }
    }
}
```

### 3. 签发JWT

token应用在系统中，一般会使用jwt(Json Web Token，实现token技术的一种解决方案)来进行。

一个JWT由三部分组成，头部、载荷、签名，各部分以 . 分割

```java
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
                .setSubject("YYHH-USER") // 主题
                .setExpiration(new Date(System.currentTimeMillis() + tokenExpiration)) // 过期时间
            	// 将需要保存的验证用户的信息以Map映射的形式保存到JWT中
            	// 此处判断builder中Claims属性是否为空，是则创建DefaultClaims对象，将键值对放入，如果不为空，则判断键，存在更新值，不存在直接放入
                .claim("userId", userId)
                // .claim("userName", userName)
                .claim("userType", userType)

                // 生成签名的算法
                .signWith(SignatureAlgorithm.HS512, tokenSignKey)
            	// 压缩方法，当载荷过长时，可对其进行压缩，另一种方法是CompressionCodecs.DEFLATE
                .compressWith(CompressionCodecs.GZIP)
            	// 生成JWT
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
        // 判断token是否为空
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        Jws<Claims> claimsJws = 
            // 生成一个DefaultJwtParser对象，返回值为new DefaultJwtParser( )
            Jwts.parser()
            // 进行编码并返回一个JwtParser
            .setSigningKey(tokenSignKey)
            .parseClaimsJws(token);
        Claims claims = claimsJws.getBody();
        // 实现了Map集合，get方法通过键获得值，即为userId。
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
            // 如果token失效，就重新创建一个token
            refreshToken = JwtHelper.createToken(getUserId(token), getUserType(token));
        } catch (Exception e) {
            refreshToken = null;
        }
        return refreshToken;
    }
}
```

#### 参考

[JJWT使用笔记（一）—— JWT token的生成 - 简书 (jianshu.com)](https://www.jianshu.com/p/1ebfc1d78928)

[(4条消息) token的问题和原理解析_骑士天空的博客-CSDN博客_token问题](https://blog.csdn.net/u013478912/article/details/105881379)

### 4. 返回结果的样式

#### 1. 结果的格式

使用两个build，一个装填返回的数据，另一个装填状态码和信息

```java
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
        // 设置返回的数据
        if (data != null) {
            result.setData(data);
        }
        return result;
    }

    // 设置返回的码和信息
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
        // 操作成功，封装数据和状态码
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
```

#### 2. 状态码的枚举类

包含状态的码和含义

```java
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
```

### 5. 上传文件

```java
/**
 * @author LISHANSHAN
 * @ClassName UploadFile
 * @Description TODO
 * @date 2022/05/2022/5/15 23:03
 */

public class UploadFile {

    private static Map<String, Object> error = new HashMap<>();

    private static Map<String, Object> upload = new HashMap<>();

    private static Map<String, Object> uploadPhoto(MultipartFile photo, String path) {

        int MAX_SIZE = 20971520;
        // 获取文件原始名称
        String originalName = photo.getOriginalFilename();

        // 如果保存文件的路径不存在，就创建一个
        File filePath = new File(path);
        if (!filePath.exists()) {
            filePath.mkdirs();
        }

        // 限制上传文件的大小
        if (photo.getSize() > MAX_SIZE) {
            error.put("success", false);
            error.put("msg", "上传的图片大小不能超过20MB呀！");
            return error;
        }

        // 限制上传文件的类型
        String[] suffixes = new String[] {".png", ".PNG", ".jpg", ".JPG",
                ".jpeg", ".JPEG", ".gif", ".GIF", ".bmp", ".BMP"};
        SuffixFileFilter suffixFileFilter = new SuffixFileFilter(suffixes);
        // 如果没有成功接收图片
        if (!suffixFileFilter.accept(new File(path + originalName))) {
            error.put("success", false);
            error.put("msg", "禁止上传此类型文件，上传图片哟！");
            return error;
        }

        return null;
    }

    public static Map<String, Object> getUploadResult(MultipartFile photo, String dirPath, String portraitPath) {
        if (!photo.isEmpty() && photo.getSize() > 0) {
            // 获取图片的原始名称
            String originalName = photo.getOriginalFilename();
            // 如果没有顺利完成，则返回上传错误
            Map<String, Object> error_result = UploadFile.uploadPhoto(photo, dirPath);
            if (error_result != null) {
                return error_result;
            }
		
            // 通过uuid生成一个新名称
            String newPhotoName = UUID.randomUUID() + "__" + originalName;
            try {
                // 将文件保存到以下位置
                photo.transferTo(new File(dirPath + newPhotoName));
                upload.put("success", true);
                // 将存储头像的路径返回给界面
                upload.put("portrait_path", portraitPath + newPhotoName);
            } catch (IOException e) {
                e.printStackTrace();
                upload.put("success", false);
                upload.put("msg", "上传文件失败! 服务器端发生异常!");
                return upload;
            }
        } else {
            upload.put("success", false);
            upload.put("msg", "上传图片失败! 未找到指定图片!");
        }
        return upload;
    }
}
```

### 6. 没用上

```java
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
```

## 2. 配置类包

### 1. MyBatis-Plus的分页插件

```java
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
        // 分页过滤器
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }
}
```

### 2. Swagger生成API文档

生成前端测试接口

```java
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
```

## 3. Dao层

通过MyBatis的Generator生成相关代码，其中的LoginInfo主要用于记录前端登录页面的表单信息，降低检验的复杂度。

![image-20220522165839178](C:\Users\LISHANSHAN\AppData\Roaming\Typora\typora-user-images\image-20220522165839178.png)

### 举例：管理员层

#### 1. Admin类

```java
package com.project.smartcampus.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author LISHANSHAN
 * @TableName tb_admin
 */
@TableName(value ="tb_admin")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Admin implements Serializable {

    @TableId(type = IdType.AUTO)
    private Integer id;

    private String name;

    private String password;

    private Character gender;

    private String email;

    private String telephone;

    private String address;

    private String portraitPath;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
```

#### 2. AdminMapper接口

```java
package com.project.smartcampus.mapper;

import com.project.smartcampus.pojo.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;

/**
* @author LISHANSHAN
* @description 针对表【tb_admin】的数据库操作Mapper
* @createDate 2022-05-16 00:33:42
* @Entity com.project.smartcampus.pojo.Admin
*/
@Repository
public interface AdminMapper extends BaseMapper<Admin> {

}
```

#### 3. AdminMapper.xml

以下为自动生成的内容

```java
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.project.smartcampus.mapper.AdminMapper">

    <resultMap id="BaseResultMap" type="com.project.smartcampus.pojo.Admin">
            <id property="id" column="id" jdbcType="INTEGER"/>
            <result property="name" column="name" jdbcType="VARCHAR"/>
            <result property="gender" column="gender" jdbcType="CHAR"/>
            <result property="password" column="password" jdbcType="VARCHAR"/>
            <result property="email" column="email" jdbcType="VARCHAR"/>
            <result property="telephone" column="telephone" jdbcType="VARCHAR"/>
            <result property="address" column="address" jdbcType="VARCHAR"/>
            <result property="portraitPath" column="portrait_path" jdbcType="VARCHAR"/>
    </resultMap>

    <sql id="Base_Column_List">
        id,name,gender,
        password,email,telephone,
        address,portrait_path
    </sql>
</mapper>
```



其实要使用的功能，很多都已经被MyBatis-Plus中的BaseMapper接口封装啦，可以直接调用或间接调用。

## 4. Service层

主要用来实现业务逻辑，调用Dao层，实现相应的操作。

在本次编写代码的过程中，主要是根据Controller层的需求，实现相应的业务逻辑代码，即Service代码。

比如，Controller需要分页带条件查询用户信息，就在Service层增加这个方法，然后调用它实现业务。

### 1. AdminService

```java
/**
* @author LISHANSHAN
* @description 针对表【tb_admin】的数据库操作Service
* @createDate 2022-05-16 00:33:42
*/

public interface AdminService extends IService<Admin> {

    /**
     * Desc: 查询管理员信息，分页带条件
     * @param pageParam
     * @param adminName
     * @return {@link IPage< Admin>}
     * @author LISHANSHAN
     * @date 2022/5/16 20:18
     */
    IPage<Admin> getAdmins(Page<Admin> pageParam, String adminName);

    /**
     * Desc: 根据获取的用户信息判断用户是否在数据库中
     * @param loginInfo
     * @return {@link Admin}
     * @author LISHANSHAN
     * @date 2022/5/17 20:01
     */
    Admin login(LoginInfo loginInfo);

    /**
     * Desc: 根据ID获取用户
     * @param userId
     * @return {@link Admin}
     * @author LISHANSHAN
     * @date 2022/5/17 22:13
     */
    Admin getAdminById(Long userId);
}
```

### 2. AdminSerivceImpl

```java
/**
* @author LISHANSHAN
* @description 针对表【tb_admin】的数据库操作Service实现
* @createDate 2022-05-16 00:33:42
*/
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin>
    implements AdminService{

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public IPage<Admin> getAdmins(Page<Admin>pageParams, String adminName) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(adminName)) {
            queryWrapper.like("name", adminName);
        }

        queryWrapper.orderByDesc("id").orderByAsc("name");
        Page page = baseMapper.selectPage(pageParams, queryWrapper);

        return page;
    }

    @Override
    public Admin login(LoginInfo loginInfo) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", loginInfo.getUsername())
                .eq("password", MD5.encrypt(loginInfo.getPassword()));
        Admin admin = adminMapper.selectOne(queryWrapper);
        return admin;
    }

    @Override
    public Admin getAdminById(Long userId) {
        Admin admin = adminMapper.selectById(userId);
        return admin;
    }
}
```

其余界面代码与此类似，当前班级和年级的Service接口中不包含验证登陆的操作，即无需login方法。

## 5. Controller层

### 1. SystemController类

主要存放一些公用的功能，如验证码的正确性判断，修改用户密码，上传头像图片，验证用户登录信息并创建Token返回给前端浏览器，将验证码传输到服务器中，从token中提取出用户信息等。

```java
/**
 * @author LISHANSHAN
 * @ClassName SystemController
 * @Description TODO
 * @date 2022/05/2022/5/16 20:40
 */
@RestController
@RequestMapping("/sms/system")
public class SystemController {
    // 一些公共的功能，放在这里

    @Autowired
    private AdminService adminService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    /**
     * Desc: 修改密码的控制器
     * @param token
     * @param oldPwd
     * @param newPwd
     * @return {@link Result}
     * @author LISHANSHAN
     * @date 2022/5/22 11:13
     */
    @ApiOperation("修改用户密码")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(
            @ApiParam("token口令") @RequestHeader("token") String token,
            @ApiParam("旧密码") @PathVariable("oldPwd") String oldPwd,
            @ApiParam("新密码") @PathVariable("newPwd") String newPwd
    ) {

        // 判断token是否过期
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            return Result.fail().message("token失效，请重新登录");
        }
        // 获取用户ID和用户类型
        Integer userType = JwtHelper.getUserType(token);
        Integer userId = JwtHelper.getUserId(token).intValue();
        oldPwd = MD5.encrypt(oldPwd);
        newPwd = MD5.encrypt(newPwd);
        switch (userType) {
            case 1:
                QueryWrapper<Admin> queryWrapperA = new QueryWrapper<>();
                queryWrapperA.eq("id", userId).eq("password", oldPwd);
                Admin admin = adminService.getOne(queryWrapperA);
                if (null != admin) {
                    // 匹配到结果，则修改密码
                    admin.setPassword(newPwd);
                    adminService.saveOrUpdate(admin);
                } else {
                    return Result.fail().message("原密码有误，请确认后重新输入");
                }
                break;
            case 2:
                QueryWrapper<Student> queryWrapperS = new QueryWrapper<>();
                queryWrapperS.eq("id", userId).eq("password", oldPwd);
                Student student = studentService.getOne(queryWrapperS);
                if (null != student) {
                    // 匹配到结果，则修改密码
                    student.setPassword(newPwd);
                    studentService.saveOrUpdate(student);
                } else {
                    return Result.fail().message("原密码有误，请确认后重新输入");
                }
                break;
            case 3:
                QueryWrapper<Teacher> queryWrapperT = new QueryWrapper<>();
                queryWrapperT.eq("id", userId).eq("password", oldPwd);
                Teacher teacher = teacherService.getOne(queryWrapperT);
                if (null != teacher) {
                    teacher.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacher);
                } else {
                    return Result.fail().message("原密码有误，请确认后重新输入");
                }
                break;
        }
        return Result.ok();

    }

    @ApiOperation("文件上传统一入口")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(
            @ApiParam("图片文件") @RequestPart("multipartFile") MultipartFile multipartFile,
            HttpServletRequest request
    ) {
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String originFileName = multipartFile.getOriginalFilename();
        int i = originFileName.lastIndexOf(".");
        String newFileName = uuid + originFileName.substring(i);

        // 保存文件，一般而言将文件发送到独立的图片服务器上，拿到url，即路径；但没有，拿到图片在服务器里的真实路径就好啦
        String url = "https://sm.ms/image/kLVMAXTP1xCFG3m";
        String path = "E:/IDEA_coding/smartcampus/target/classes/public/upload/" + newFileName;
        try {
            multipartFile.transferTo(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*multipartFile.transferTo(new File(url));

        // 相应图片路径
        String path = "upload/" + (newFileName);
        return Result.ok(path);*/
        path = "upload/".concat(newFileName);
        return Result.ok(path);
    }

    @ApiOperation("通过token口令，获取当前登录的用户信息")
    @GetMapping("/getInfo")
    public Result getInfoByToken(@ApiParam("获取token口令") @RequestHeader("token") String token){
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }

        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);
        Map<String, Object> map = new HashMap<>();
        switch (userType) {
            case 1:
                Admin admin = adminService.getAdminById(userId);
                map.put("userType", userType);
                map.put("user", admin);
                break;
            case 2:
                Student student = studentService.getStudentById(userId);
                map.put("userType", userType);
                map.put("user", student);
                break;
            case 3:
                Teacher teacher = teacherService.getTeacherById(userId);
                map.put("userType", userType);
                map.put("user", teacher);
                break;
        }
        return Result.ok(map);
    }

    @ApiOperation("进行验证码的验证")
    @PostMapping("/login")
    public Result login(@RequestBody LoginInfo loginInfo, HttpServletRequest request) {

        // 验证码是否有效，前边将验证码封装进入Session，会随着请求一起传输过来，所以是Request类型
        HttpSession session = request.getSession();
        String sessionVerifyCode = (String)session.getAttribute("verifiCode");
        String loginVerifyCode = loginInfo.getVerifiCode ();

        if ("".equals(sessionVerifyCode) || null == sessionVerifyCode) {
            return Result.fail().message("验证码失效，请刷新后重试");
        }
        if (!sessionVerifyCode.equalsIgnoreCase(loginVerifyCode)) {
            return Result.fail().message("验证码有误，请重试");
        }
        // 从session域中移除现有验证码，防止后续判断错误
        session.removeAttribute("verifiCode");

        Map<String, Object> map = new HashMap<>();
        // 分用户类型进行校验，即根据用户类型，用不同的表进行登录信息校验
        switch (loginInfo.getUserType()) {
            case 1:
                try {
                    Admin admin = adminService.login(loginInfo);
                    if (null != admin) {
                        // 将用户的类型和名称，转换为密文，以token的形式发送到前端
                        String token = JwtHelper.createToken(admin.getId().longValue(), 1);
                        map.put("token", token);
                    } else {
                        throw new RuntimeException("用户名或密码有误！");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 2:
                try {
                    Student student = studentService.login(loginInfo);
                    if (null != student) {
                        // 将用户的类型和名称，转换为密文，以token的形式发送到前端
                        String token = JwtHelper.createToken(student.getId().longValue(), 2);
                        map.put("token", token);
                    } else {
                        throw new RuntimeException("用户名或密码有误！");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
            case 3:
                try {
                    Teacher teacher = teacherService.login(loginInfo);
                    if (null != teacher) {
                        // 将用户的类型和名称，转换为密文，以token的形式发送到前端
                        String token = JwtHelper.createToken(teacher.getId().longValue(), 3);
                        map.put("token", token);
                    } else {
                        throw new RuntimeException("用户名或密码有误！");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }
        return Result.fail().message("您尚未注册，请先注册！");
    }

    // 在内存上生成验证码的图片，通过IO流传输，响应给浏览器，展示出来
    @RequestMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response) {
        // 获取图片
        BufferedImage verifiCodeImage = CreateVerifiCodeImage.getVerifiCodeImage();
        // 获取图片上的验证码
        String verifiCode = new String(CreateVerifiCodeImage.getVerifiCode());
        // 将验证码上的文本放入session域，使服务器可以判断验证码的正确性
        HttpSession session = request.getSession();
        session.setAttribute("verifiCode", verifiCode);
        // 将验证码响应到浏览器
        try {
            // 将验证码图片以JEPG格式写到输出流，响应到浏览器
            ImageIO.write(verifiCodeImage, "JPEG", response.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
```

#### 补充

##### 图片上传

1. 数据库中存储的是图片的路径
2. 将图片转换为这个对象MultipartFile，需加注解@RequestPart，将请求中的name="multipartFile"的文件转换为MultipartFile对象。
3. 因为Spring Boot项目使用的是内置的Tomcat，那么通过获取上下文再拼接文件夹名得到的路径，将不是项目中文件保存的真实路径，会对应不上，所以手动添加其在本地服务器上的真实路径。
4. 一般而言，图片都是放到第三方的图片服务器上，不然存到本地项目中，图片会越来越多，内存占用会变大。

### 2. AdminController

```java
/**
 * @author LISHANSHAN
 * @ClassName AdminController
 * @Description TODO
 * @date 2022/05/2022/5/16 19:13
 */
@Api(tags="系统管理员控制器")
@RestController
@RequestMapping("/sms/adminController")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @ApiOperation("分页获取所有的Admin信息")
    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmins(@ApiParam("页码数") @PathVariable Integer pageNo,
                               @ApiParam("页大小") @PathVariable Integer pageSize,
                               @ApiParam("管理员姓名") String adminName) {
        Page<Admin> pageParam = new Page<>(pageNo, pageSize);
        IPage<Admin> page = adminService.getAdmins(pageParam, adminName);
        return Result.ok(page);
    }

    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(@RequestBody Admin admin) {
        Integer id = admin.getId();
        if (null == id || id == 0) {
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }
        adminService.saveOrUpdate(admin);
        return Result.ok();
    }

    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(@RequestBody List<Integer> ids) {
        adminService.removeByIds(ids);
        return Result.ok();
    }

}
```

其余页面基本功能与此类似，故而代码基本一致。

# 3. 总结

## Why 前后端分离？

项目使用了前后端分离的模式进行开发。

前后端分离，实际上是软件技术和业务发展到一定程度后，在项目管理工作上必须进行的一个省级。

初期的开发，其实是侧重于后端的，因为初期页面的功能比较简单。只需要做数据的展示，然后提供基本操作即可。所以整个项目的重点是放在后台的业务逻辑处理上。

但随着业务和技术的发展，前端功能越来越复杂，越来越重要，所以就不能再像之前那样零散地分布在整个系统架构中了。所以就成立了专门的前端部门，专门去研究开发工程化的前端技术。

也就是说，前后端分离的根本原因是为了适应技术和业务发展的需求。

### 传统的Java Web开发过程

![image-20220522173808033](C:\Users\LISHANSHAN\AppData\Roaming\Typora\typora-user-images\image-20220522173808033.png)

![image-20220522174148739](C:\Users\LISHANSHAN\AppData\Roaming\Typora\typora-user-images\image-20220522174148739.png)

### 前后端分离

前端只需要独立编写客户端代码，后端也只需要独立编写服务端代码提供数据接口即可。

前端通过Ajax请求来访问后端的数据接口，将Model展示到View中即可。

![image-20220522173954006](C:\Users\LISHANSHAN\AppData\Roaming\Typora\typora-user-images\image-20220522173954006.png)

### 参考

[(4条消息) 轻松理解前后端分离（通俗易懂）_Coder Xu的博客-CSDN博客_前后端分离](https://blog.csdn.net/weixin_46594796/article/details/115123635)



这才只是入门而已，通过这个案例感受一下开发过程，之后的路还长着呢，加油！

会卷，要卷！





























































































































































































































































































































































