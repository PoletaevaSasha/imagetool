package com.chekanova.imagetool.service.comparison.impl;

import com.chekanova.imagetool.service.comparison.DrawDifferenceService;
import com.chekanova.imagetool.service.comparison.ImageComparisonService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.AbstractMap;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Implementation of {@link ImageComparisonService} where a square is implemented as a frame
 *
 * @author oleksandra.chekanova
 */
@Service
public class DrawSquareDifferenceServiceImpl implements DrawDifferenceService {
    @Value("${margin.size:10}")
    private int marginSize;

    /**
     * Returns image with red square frames that show differences according to comparison param
     *
     * @param comparison boolean array with comparison of two images by pixel.
     * @param image      target image to draw frames
     * @return BufferedImage with first image, where all differences are marked with frames
     */
    @Override
    public BufferedImage drawDifference(boolean[][] comparison, BufferedImage image) {
        int height = image.getHeight();
        int width = image.getWidth();
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (comparison[i][j]) {
                    Box box = buildBox(i, j, width, height, comparison);
                    drawBox(image, box);
                    clearProcessedPixels(box, comparison);
                }
            }
        }
        return image;
    }

    /**
     * Build frame for displaying difference using DFS
     *
     * @param x          horizontal constraint
     * @param y          vertical constraint
     * @param w          width of initial picture
     * @param h          height of initial picture
     * @param comparison boolean matrix with difference
     */
    private Box buildBox(int x, int y, int w, int h, boolean[][] comparison) {
        Box box = new Box(x, y);
        Deque<AbstractMap.SimpleImmutableEntry<Integer, Integer>> stack
                = new ArrayDeque<>();
        stack.push(new AbstractMap.SimpleImmutableEntry<>(x, y));
        while (!stack.isEmpty()) {
            AbstractMap.SimpleImmutableEntry<Integer, Integer> current = stack.pop();
            x = current.getKey();
            y = current.getValue();
            if (x < 0 || x >= w || y < 0 || y >= h || !comparison[x][y]) {
                continue;
            }
            comparison[x][y] = false;
            box.updateSize(x, y);
            stack.push(new AbstractMap.SimpleImmutableEntry<>(x - 1, y));
            stack.push(new AbstractMap.SimpleImmutableEntry<>(x, y - 1));
            stack.push(new AbstractMap.SimpleImmutableEntry<>(x + 1, y));
            stack.push(new AbstractMap.SimpleImmutableEntry<>(x, y + 1));
        }
        return box;
    }

    private void drawBox(BufferedImage image, Box box) {
        if (box == null) {
            return;
        }
        int minX = getMinWithMargin(box.minX);
        int maxX = getMaxWithMargin(box.maxX, image.getWidth());
        int minY = getMinWithMargin(box.minY);
        int maxY = getMaxWithMargin(box.maxY, image.getHeight());
        for (int x = minX; x <= maxX; x++) {
            image.setRGB(x, minY, Color.red.getRGB());
            image.setRGB(x, maxY, Color.red.getRGB());
        }
        for (int y = minY; y <= maxY; y++) {
            image.setRGB(minX, y, Color.red.getRGB());
            image.setRGB(maxX, y, Color.red.getRGB());
        }
    }

    private void clearProcessedPixels(Box box, boolean[][] comparisonResult) {
        for (int x = box.minX; x <= box.maxX; x++) {
            for (int y = box.minY; y <= box.maxY; y++) {
                comparisonResult[x][y] = false;
            }
        }
    }

    private int getMaxWithMargin(int coord, int limit) {
        return Math.min(limit - 1, coord + marginSize);
    }

    private int getMinWithMargin(int coord) {
        return Math.max(0, coord - marginSize);
    }

    private class Box {
        private int minX;
        private int maxX;
        private int minY;
        private int maxY;

        public Box(int x, int y) {
            maxX = x;
            minX = x;
            maxY = y;
            minY = y;
        }

        private void updateSize(int x, int y) {
            minX = Math.min(minX, x);
            minY = Math.min(minY, y);
            maxX = Math.max(maxX, x);
            maxY = Math.max(maxY, y);
        }
    }
}
