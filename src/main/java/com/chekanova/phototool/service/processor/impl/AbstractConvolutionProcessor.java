package com.chekanova.phototool.service.processor.impl;

import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.ToIntFunction;

@Service
public abstract class AbstractConvolutionProcessor extends AbstractImageProcessor {

    protected abstract double[][] getFilter();

    protected abstract int getFilterSize();

    @Override
    public void recolorPixel(BufferedImage originalImage, BufferedImage resultImage, int x, int y) {
      /*  double newRed = singlePixelConvolution(originalImage, x, y, Color::getRed);
        double newGreen = singlePixelConvolution(originalImage, x, y, Color::getGreen);
        double newBlue = singlePixelConvolution(originalImage, x, y, Color::getBlue);*/
        double[] rgb = singlePixelConvolution(originalImage, x,y);
        int newRGB = createRGBFromColors(fixOutOfRangeRGBValues(rgb[0]),
                fixOutOfRangeRGBValues(rgb[1]), fixOutOfRangeRGBValues(rgb[2]));
       /* int newRGB = createRGBFromColors(fixOutOfRangeRGBValues(newRed),
                fixOutOfRangeRGBValues(newGreen), fixOutOfRangeRGBValues(newBlue));*/

        setRGB(resultImage, x, y, newRGB);
    }
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

    public double singlePixelConvolution(BufferedImage originalImage,
                                         int x, int y,
                                         ToIntFunction<Color> colorIntFunction) {
        double[][] k = getFilter();
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int kernelSize = getFilterSize();
        double output = 0;
        for (int i = 0; i < kernelSize; ++i) {
            for (int j = 0; j < kernelSize; ++j) {
                Color color = new Color(originalImage.getRGB(
                        indexTrim(x + i - kernelSize / 2, width - 1),
                        indexTrim(y + j - kernelSize / 2, height - 1)));
                int pixel = colorIntFunction.applyAsInt(color);
                output = output + (pixel * k[i][j]);
            }
        }
        return output;
    }

    protected int fixOutOfRangeRGBValues(double value) {
        if (value < 0.0) {
            value = -value;
        }
        if (value > 255) {
            return 255;
        } else {
            return (int) value;
        }
    }

    private int indexTrim(int index, int max) {
        if (index < 0) {
            return 0;
        }
        return index > max ? max : index;
    }
}
