package com.han.tools.crawler;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageUtils {

    public static void main(String[] args) {
        BufferedImage bufferedImage;

        try {
            //1.读取图片
            bufferedImage = ImageIO.read(new File("D:\\1.png"));

            //2.创建一个空白大小相同的RGB背景
            BufferedImage newBufferedImage = new BufferedImage(bufferedImage.getWidth(),
                    bufferedImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            newBufferedImage.createGraphics().drawImage(bufferedImage, 0, 0, Color.WHITE, null);

            //3.再次写入的时候以jpeg图片格式
            ImageIO.write(newBufferedImage, "jpg", new File("D:\\1.jpg"));

            System.out.println("成功将png格式图片转换为jpg格式");

        } catch (IOException e) {

            e.printStackTrace();

        }

    }

}
