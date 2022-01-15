package com.chekanova.phototool.service.strategy;

import com.chekanova.phototool.service.processor.ImageProcessor;

import java.awt.image.BufferedImage;

public interface ProcessingStrategy {
    void recolor(ImageProcessor imageProcessor, BufferedImage originalImage, BufferedImage resultImage, int numberOfThreads) throws InterruptedException;

    default void recolorImage(ImageProcessor imageProcessor,
                              BufferedImage originalImage,
                              BufferedImage resultImage,
                              int leftCorner, int topCorner,
                              int width, int height) {
        for (int x = leftCorner; x < leftCorner + width && x < originalImage.getWidth(); x++) {
            for (int y = topCorner; y < topCorner + height && y < originalImage.getHeight(); y++) {
                imageProcessor.recolorPixel(originalImage, resultImage, x, y);
            }
        }
    }
}