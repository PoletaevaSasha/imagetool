package com.chekanova.imagetool.service.comparison;

import java.awt.image.BufferedImage;

/**
 * Basic interface for comparing images
 * @author oleksandra.chekanova
 */
public interface DrawDifferenceService {
    /**
     * Returns image with frames that show differences according to comparison param
     * @param comparison boolean array with comparison of two images by pixel.
     * @param image target image to draw frame
     * @return BufferedImage with first image, where all differences are marked with frames
     */
    BufferedImage drawDifference(boolean[][] comparison, BufferedImage image);
}
