package com.chekanova.imagetool.service.processor;

import java.awt.image.BufferedImage;

/**
 * Basic interface for processing images
 * @author oleksandra.chekanova
 */
public interface ImageProcessor {
    /**
     * Recolors pixel with coordinates x and y from {@code originalImage} and save it to {@code resultImage}
     * @param originalImage must not be null. Original image
     * @param resultImage must not be null. Image with changed pixel
     * @param x not null. X-coordinate of pixel
     * @param y not null. Y-coordinate of pixel
     */
    void recolorPixel(BufferedImage originalImage, BufferedImage resultImage, int x, int y);
}
