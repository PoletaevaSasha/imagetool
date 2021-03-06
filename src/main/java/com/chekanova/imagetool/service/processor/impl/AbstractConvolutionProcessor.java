package com.chekanova.imagetool.service.processor.impl;

import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
public abstract class AbstractConvolutionProcessor extends AbstractImageProcessor {

    protected abstract double[][] getFilter();

    protected abstract int getFilterSize();

    /**
     * Recolors pixel with coordinates x and y from {@code originalImage} according to convolution,
     * save it to {@code resultImage}
     * @param originalImage must not be null. Original image.
     * @param resultImage must not be null. Image with changed pixel.
     * @param x not null. X-coordinate of pixel.
     * @param y not null. Y-coordinate of pixel.
     */
    @Override
    public void recolorPixel(BufferedImage originalImage, BufferedImage resultImage, int x, int y) {
        double[] rgb = singlePixelConvolution(originalImage, x,y);
        int newRGB = createRGBFromColors(fixOutOfRangeRGBValues(rgb[0]),
                fixOutOfRangeRGBValues(rgb[1]), fixOutOfRangeRGBValues(rgb[2]));

        setRGB(resultImage, x, y, newRGB);
    }

    /**
     * Calculate convolution for given pixel with coordinates x and y
     * @param originalImage must not be null. Original image
     * @param x not null. Coordinate of pixel.
     * @param y not null. Coordinate of pixel.
     * @return array consisting of RGB values for the pixel.
     */
    protected double[] singlePixelConvolution(BufferedImage originalImage,
                                         int x, int y) {
        double[] result = new double[3];
        int[] pixel;
        double[][] k = getFilter();
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int kernelSize = getFilterSize();
        for (int i = 0; i < kernelSize; ++i) {
            for (int j = 0; j < kernelSize; ++j) {
                pixel = originalImage.getRaster().getPixel(indexTrim(x + i - kernelSize / 2, width - 1),
                        indexTrim(y + j - kernelSize / 2, height - 1), new int[3]);
                result[0] = result[0] + (pixel[0] * k[i][j]);
                result[1] = result[1] + (pixel[1] * k[i][j]);
                result[2] = result[2] + (pixel[2] * k[i][j]);
            }
        }
        return result;
    }

    protected int fixOutOfRangeRGBValues(double value) {
        if (value < 0.0) {
            value = -value;
        }
        return value > 255 ? 255 : (int) value;
    }

    private int indexTrim(int index, int max) {
        return index < 0 ? 0 : Math.min(index, max);
    }
}
