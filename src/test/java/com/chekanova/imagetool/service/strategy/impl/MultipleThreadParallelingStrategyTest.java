package com.chekanova.imagetool.service.strategy.impl;

import org.junit.Test;
import org.mockito.InjectMocks;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;

public class MultipleThreadParallelingStrategyTest extends AbstractParallelingStrategyTest {
    public static final String SOURCE_FILE = "src/test/resources/tiger.jpg";

    @InjectMocks
    private MultipleThreadParallelingStrategy testedObject;

    @Test
    public void testThreadConvolution() throws IOException, InterruptedException {
        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage result = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());

        testedObject.recolor(getIdentityConvolutionProcessor(), originalImage, result);

        assertTrue("For identity filter results should be equals ", bufferedImagesEqual(originalImage, result));
    }
}