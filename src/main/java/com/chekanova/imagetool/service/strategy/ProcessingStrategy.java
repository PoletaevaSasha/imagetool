package com.chekanova.imagetool.service.strategy;

import com.chekanova.imagetool.model.ImageOptions;
import com.chekanova.imagetool.service.processor.ImageProcessor;

import java.awt.image.BufferedImage;

/**
 * Basic interface for processing
 * @author oleksandra.chekanova
 */
public interface ProcessingStrategy {
    /**
     * Recolors all pixels of {@code originalImage} and saves the result to {@code resultImage}
     * @param imageProcessor must not be null. Defines how the image is processed (for example gray-scale, blur... )
     * @param originalImage must not be null. Original image
     * @param resultImage resulting image with changed pixels
     */
    void recolor(ImageProcessor imageProcessor, BufferedImage originalImage, BufferedImage resultImage) throws InterruptedException;

    /**
     * Recolors a part of {@code originalImage} and saves the result to {@code resultImage}
     * @param options must not be null. Contains recoloring options like image processor and coordinates
     * @param originalImage must not be null. Original image
     * @param resultImage resulting image with changed pixels
     */
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