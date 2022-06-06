package com.chekanova.imagetool.service;

import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.awt.image.BufferedImage;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractImageToolTest {
    protected boolean bufferedImagesEqual(BufferedImage img1, BufferedImage img2) {
        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            return false;
        }
        for (int x = 0; x < img1.getWidth(); x++) {
            for (int y = 0; y < img1.getHeight(); y++) {
                if (img1.getRGB(x, y) != img2.getRGB(x, y)) {
                    return false;
                }
            }
        }
        return true;
    }
}
