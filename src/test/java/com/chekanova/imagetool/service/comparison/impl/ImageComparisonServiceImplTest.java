package com.chekanova.imagetool.service.comparison.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;

@RunWith(MockitoJUnitRunner.class)
public class ImageComparisonServiceImplTest {
    public static final String SOURCE_FILE = "src/test/resources/";

    @InjectMocks
    private final ImageComparisonServiceImpl testedObject = new ImageComparisonServiceImpl();

    private BufferedImage image1;
    private BufferedImage image2;
    private BufferedImage image3;

    @Before
    public void setUp() throws IOException {
        image1 = ImageIO.read(new File(SOURCE_FILE + "image1.png"));
        image2 = ImageIO.read(new File(SOURCE_FILE + "image2.png"));
        image3 = ImageIO.read(new File(SOURCE_FILE + "image3.png"));
    }

    @Test
    public void testCompareSimilarColors() {
        boolean[][] expected = {{false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false}};

        boolean[][] actual = testedObject.compare(image3, image2);
        assertArrayEquals(actual, expected);
    }

    @Test
    public void testCompareDifferentColors() {
        boolean[][] expected = {{false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, true, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false}};
        boolean[][] actual = testedObject.compare(image1, image2);
        assertArrayEquals(actual, expected);
    }
}