package com.chekanova.imagetool.service.comparison;

import java.awt.image.BufferedImage;

/**
 * Basic interface for comparing images
 * @author oleksandra.chekanova
 */
public interface ImageComparisonService {

    /**
     * Compares two BufferedImage with the same size.
     * @param image1 must not be null. First image for comparison.
     * @param image2 must not be null. Second image for comparison.
     * @return boolean array
     */
    boolean[][] compare(BufferedImage image1, BufferedImage image2);
}
