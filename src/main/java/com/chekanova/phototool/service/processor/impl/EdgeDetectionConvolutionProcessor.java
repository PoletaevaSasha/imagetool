package com.chekanova.phototool.service.processor.impl;

import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public class EdgeDetectionConvolutionProcessor extends AbstractConvolutionProcessor {

    private static final double[][] FILTER = {{1, 0, -1}, {1, 0, -1}, {1, 0, -1}};

    public double[][] getFilter() {
        return FILTER;
    }

    public int getFilterSize() {
        return FILTER.length;
    }

    @Override
    public void recolorPixel(BufferedImage originalImage, BufferedImage resultImage, int x, int y) {
        double[] rgb = singlePixelConvolution(originalImage, x, y);
        int newColor = (fixOutOfRangeRGBValues(rgb[0]) + fixOutOfRangeRGBValues(rgb[1]) + fixOutOfRangeRGBValues(rgb[2])) / 3;
        int newRGB = createRGBFromColors(newColor, newColor, newColor);
        setRGB(resultImage, x, y, newRGB);
    }
}
