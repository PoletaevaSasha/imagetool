package com.chekanova.imagetool.service.processor.impl;

import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

/**
 * Processor for determining edges on the original image
 * @author oleksandra.chekanova
 */
@Service
public class EdgeDetectionConvolutionProcessor extends AbstractConvolutionProcessor {

    private static final double[][] FILTER = {{1, 0, -1}, {1, 0, -1}, {1, 0, -1}};

    public double[][] getFilter() {
        return FILTER;
    }

    public int getFilterSize() {
        return FILTER.length;
    }

    /**
     * Recolors pixel with coordinates x and y from {@code originalImage} according to convolution and make it
     * gray scale, save it to {@code resultImage}
     * @param originalImage must not be null. Original image.
     * @param resultImage must not be null. Image with changed pixel.
     * @param x not null. X-coordinate of pixel.
     * @param y not null. Y-coordinate of pixel.
     */
    @Override
    public void recolorPixel(BufferedImage originalImage, BufferedImage resultImage, int x, int y) {
        double[] rgb = singlePixelConvolution(originalImage, x, y);
        int newColor = (fixOutOfRangeRGBValues(rgb[0]) + fixOutOfRangeRGBValues(rgb[1]) + fixOutOfRangeRGBValues(rgb[2])) / 3;
        int newRGB = createRGBFromColors(newColor, newColor, newColor);
        setRGB(resultImage, x, y, newRGB);
    }
}
