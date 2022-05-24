package com.chekanova.imagetool.service.comparison.impl;

import com.chekanova.imagetool.service.comparison.ImageComparisonService;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Default implementation {@link ImageComparisonService}
 *
 * @author oleksandra.chekanova
 */
@Service
public class ImageComparisonServiceImpl implements ImageComparisonService {

    /**
     * Compares two BufferedImage with the same size by each pixel and
     * returns boolean array with comparison of two images by pixel.
     * When the pixels are equal, then the corresponding value in the array cell is false,
     * otherwise true.
     * If the difference between each color less than 10, it means that pixels are equals.
     * @param image1 must not be null. First image for comparison.
     * @param image2 must not be null. Second image for comparison.
     * @return boolean array
     */
    public boolean[][] compare(BufferedImage image1, BufferedImage image2) {
        int height = image1.getHeight();
        int width = image1.getWidth();
        boolean[][] result = new boolean[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                Color firstImageColor = new Color(image1.getRGB(i, j));
                Color secondImageColor = new Color(image2.getRGB(i, j));
                result[i][j] = calculateColorDifference(firstImageColor, secondImageColor) > 10.0;
            }
        }
        return result;
    }

    /**
     * Returns maximum difference from red, blue and green colors.
     * @param firstImageColor  pixel color of first image
     * @param secondImageColor pixel color of second image
     * @return double value of maximum difference between different colors between pixels
     */
    private double calculateColorDifference(Color firstImageColor, Color secondImageColor) {
        int r1 = firstImageColor.getRed();
        int r2 = secondImageColor.getRed();
        int g1 = firstImageColor.getGreen();
        int g2 = secondImageColor.getGreen();
        int b1 = firstImageColor.getBlue();
        int b2 = secondImageColor.getBlue();
        double redDifference = Math.abs(r1 - r2) * 100.0 / Math.min(r1, r2);
        double greenDifference = Math.abs(g1 - g2) * 100.0 / Math.min(g1, g2);
        double blueDifference = Math.abs(b1 - b2) * 100.0 / Math.min(b1, b2);
        return Math.max(redDifference, Math.max(greenDifference, blueDifference));
    }
}
