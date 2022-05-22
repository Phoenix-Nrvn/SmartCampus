package com.project.smartcampus.util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

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

        verifiCode = generateCheckCode();
        drawBackground(graphics);
        drawRands(graphics, verifiCode);
        graphics.dispose();

        return verifiCodeImage;
    }

    /**
     * Desc: 获取验证码
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
        String chars = "0123456789abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        char[] rands = new char[4];
        for (int i = 0; i < 4; i++) {
            int rand = (int) (Math.random() * (10 + 26 * 2));
            rands[i] = chars.charAt(rand);
        }
        return rands;
    }

    /**
     * Desc: 绘制验证null码
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