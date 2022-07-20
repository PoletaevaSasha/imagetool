package com.chekanova.imagetool.service.comparison.impl;

import com.chekanova.imagetool.service.AbstractImageToolTest;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertTrue;


@RunWith(MockitoJUnitRunner.class)
public class DrawSquareDifferenceServiceImplTest extends AbstractImageToolTest {
    public static final String SOURCE_FILE = "src/test/resources/";
    public static final String FRAME_COLOR = "#ff0000";

    @Spy
    private final DrawSquareDifferenceServiceImpl testedObject = new DrawSquareDifferenceServiceImpl();
    private BufferedImage image1;

    @Before
    public void setUp() throws IOException {
        image1 = ImageIO.read(new File(SOURCE_FILE + "image1.png"));
    }
    @Test
    public void testComparisonWithBigBox() throws IOException {
        ReflectionTestUtils.setField(testedObject, "marginSize", 10);
        BufferedImage expected = ImageIO.read(new File(SOURCE_FILE + "image_big_margin.png"));

        boolean[][] comparison = {{false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, true, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false}};
        BufferedImage actual = testedObject.drawDifference(comparison, image1, FRAME_COLOR);

        assertTrue(bufferedImagesEqual(actual, expected));
    }

    @Test
    public void testComparisonSmallBigBox() throws IOException {
        ReflectionTestUtils.setField(testedObject, "marginSize", 1);
        BufferedImage expected = ImageIO.read(new File(SOURCE_FILE + "image_small_margin.png"));

        boolean[][] comparison = {{false, false, false, false, false},
                {false, false, false, false, false},
                {false, false, true, false, false},
                {false, false, false, false, false},
                {false, false, false, false, false}};

        BufferedImage actual = testedObject.drawDifference(comparison, image1, FRAME_COLOR);

        assertTrue(bufferedImagesEqual(actual, expected));
    }

}