package com.chekanova.imagetool.service.strategy.impl;

import com.chekanova.imagetool.service.processor.impl.AbstractConvolutionProcessor;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import java.awt.image.BufferedImage;

@RunWith(MockitoJUnitRunner.class)
public abstract class AbstractProcessingStrategyTest {
    @InjectMocks
    private IdentityConvolutionProcessor identityConvolutionProcessor;

    static class IdentityConvolutionProcessor extends AbstractConvolutionProcessor {

        private final double[][] FILTER = {{0, 0, 0}, {0, 1, 0}, {0, 0, 0}};

        public double[][] getFilter() {
            return FILTER;
        }

        public int getFilterSize() {
            return FILTER.length;
        }
    }

    protected IdentityConvolutionProcessor getIdentityConvolutionProcessor() {
        return identityConvolutionProcessor;
    }

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
