package com.jackeyj.utils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

/**
 * verify code tool class
 * @author jiyaofei
 */
public class CaptchaUtil {

    private int width = 160;
    private int height = 40;
    private int codeNum = 4;
    private int lineNum = 20;
    private String captcha;
    private BufferedImage bufferedImage;
    Random random = new Random();

    public CaptchaUtil(){
        createImg();
    }

    public CaptchaUtil(int width, int height){
        this.width = width;
        this.height = height;
        createImg();
    }

    public BufferedImage getBufferedImage() {
        return bufferedImage;
    }

    public String getCaptcha(){
        return captcha;
    }

    private void createImg() {
        int fontWidth = width / codeNum;
        int fontHeight = height - 5;
        int codeY = height - 8;

        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics graphics = bufferedImage.getGraphics();
        graphics.setColor(getRandomColor(200, 250));
        graphics.fillRect(0, 0, width, height);

        //set font
        Font font = new Font("fixed_font", Font.BOLD, fontHeight);
        graphics.setFont(font);

        //set interfering lines
        for (int i = 0; i < lineNum; i++) {
            int startX = random.nextInt(width);
            int endX = random.nextInt(width);
            int startY = random.nextInt(height);
            int endY = random.nextInt(height);
            graphics.setColor(getRandomColor(1, 255));
            graphics.drawLine(startX, startY, endX, endY);
        }

        //add noise points
        float noiseRate = 0.01f;
        int pointNum = (int) (noiseRate * width * height);
        for (int i = 0; i < pointNum; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);

            bufferedImage.setRGB(x, y, random.nextInt(255));
        }

        String codes = getRandomCaptcha(codeNum);
        this.captcha = codes.toLowerCase();

        //draw verify codes
        for (int i = 0; i < codeNum; i++) {
            String str = codes.substring(i, i + 1);
            graphics.setColor(getRandomColor(1, 255));
            graphics.drawString(str, i * fontWidth + 3, codeY);
        }
    }

    /**
     * create random verify code
     * @param codeNum code number
     * @return verify code
     */
    private String getRandomCaptcha(int codeNum) {
        String source = "QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm1234567890";
        int len = source.length();
        String code = "";
        for (int i = 0; i < codeNum; i++) {
            int rand = random.nextInt(len);
            code = code + source.charAt(rand);
        }
        return code;
    }

    /**
     * get random color
     * @param lo
     * @param hi
     * @return
     */
    private Color getRandomColor(int lo, int hi) {
        if (lo < 0){
            lo = 0;
        }
        if (lo > 255){
            lo = 255;
        }
        if (hi < 0){
            hi = 0;
        }
        if (hi > 255){
            hi = 255;
        }
        int r = lo + random.nextInt(hi - lo);
        int g = lo + random.nextInt(hi - lo);
        int b = lo + random.nextInt(hi - lo);
        return new Color(r, g, b);
    }
}
