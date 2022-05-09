package com.chekanova.imagetool.service.strategy.impl;

import org.junit.Test;
import org.mockito.InjectMocks;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class ForkJoinPoolStrategyTest extends AbstractProcessingStrategyTest{
    public static final String SOURCE_FILE = "src/test/resources/tiger.jpg";

    @InjectMocks
    private ForkJoinPoolStrategy testedObject;

    @Test
    public void testForkJoinPoolConvolution() throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage result = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());

        testedObject.recolor(getIdentityConvolutionProcessor(), originalImage, result, 10);

        assertTrue("For identity filter results should be equals ", bufferedImagesEqual(originalImage, result));
    }
}