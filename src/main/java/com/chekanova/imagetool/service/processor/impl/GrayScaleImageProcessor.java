package com.chekanova.imagetool.service.processor.impl;

import org.springframework.stereotype.Service;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Processor for making original image gray scale
 * @author oleksandra.chekanova
 */
@Service
public class GrayScaleImageProcessor extends AbstractImageProcessor {

    //For performance, it would be better not to use  new Color, but I left it because it is hard to see speed improvement without it
    @Override
    public void recolorPixel(BufferedImage originalImage, BufferedImage resultImage, int x, int y) {
        Color rgb = new Color(originalImage.getRGB(x, y));
        int newColor = getRed(rgb) + getGreen(rgb) + getBlue(rgb);

        int newRGB = createRGBFromColors(newColor, newColor, newColor);
        setRGB(resultImage, x, y, newRGB);
    }

    private int getRed(Color rgb) {
        return (int) (rgb.getRed() * 0.299);
    }

    private int getGreen(Color rgb) {
        return (int) (rgb.getGreen() * 0.587);
    }

    private int getBlue(Color rgb) {
        return (int) (rgb.getBlue() * 0.114);
    }
}
