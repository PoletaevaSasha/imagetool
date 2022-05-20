package com.chekanova.imagetool.service.strategy;

import com.chekanova.imagetool.model.ImageOptions;
import com.chekanova.imagetool.service.processor.ImageProcessor;

import java.awt.image.BufferedImage;

public interface ProcessingStrategy {
    void recolor(ImageProcessor imageProcessor, BufferedImage originalImage, BufferedImage resultImage, int numberOfThreads) throws InterruptedException;

    default void recolorImage(ImageOptions options,
                              BufferedImage originalImage,
                              BufferedImage resultImage) {
        ImageProcessor imageProcessor = options.getImageProcessor();
        int leftCorner = options.getLeftCorner();
        int topCorner = options.getTopCorner();
        int width = options.getWidth();
        int height = options.getHeight();
        for (int x = leftCorner; x < leftCorner + width && x < originalImage.getWidth(); x++) {
            for (int y = topCorner; y < topCorner + height && y < originalImage.getHeight(); y++) {
                imageProcessor.recolorPixel(originalImage, resultImage, x, y);
            }
        }
    }
}