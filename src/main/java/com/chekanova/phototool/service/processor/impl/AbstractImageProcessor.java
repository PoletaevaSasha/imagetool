package com.chekanova.phototool.service.processor.impl;

import com.chekanova.phototool.service.processor.ImageProcessor;

import java.awt.image.BufferedImage;

public abstract class AbstractImageProcessor implements ImageProcessor {
    protected void setRGB(BufferedImage image, int x, int y, int rgb) {
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }

    protected int createRGBFromColors(int red, int green, int blue) {
        int rgb = 0;

        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;
        return rgb;
    }
}
