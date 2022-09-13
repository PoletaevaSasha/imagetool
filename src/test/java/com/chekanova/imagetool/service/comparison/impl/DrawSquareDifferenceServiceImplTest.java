package com.chekanova.imagetool.service.comparison.impl;

import com.chekanova.imagetool.service.AbstractImageToolTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class DrawSquareDifferenceServiceImplTest extends AbstractImageToolTest {
    private static final String SOURCE_FILE = "src/test/resources/";
    private static final Color FRAME_COLOR = Color.RED;
    private static final String MESSAGE_FRAME_IS_BIGGER_THAN_IMAGE = "Should draw the border inside the image when frame is bigger than image";
    private static final String MESSAGE_FRAME_IS_SMALLER_THAN_IMAGE = "Should draw the border inside the image when frame is smaller than image";

    @InjectMocks
    private DrawSquareDifferenceServiceImpl testedObject;
    private BufferedImage image;

    @BeforeEach
    public void setUp() throws IOException {
        image = ImageIO.read(new File(SOURCE_FILE + "image1.png"));
    }

    @DisplayName("Test border drawing")
    @ParameterizedTest(name = "{index}, {0}")
    @ArgumentsSource(DrawDifferenceArgumentsProvider.class)
    void testDrawBorderValidData(String message, Integer marginSize, String expectedFileName, boolean[][] comparison) throws Exception {
        ReflectionTestUtils.setField(testedObject, "marginSize", marginSize);
        BufferedImage expected = ImageIO.read(new File(SOURCE_FILE + expectedFileName));
        BufferedImage actual = testedObject.drawDifference(comparison, image, FRAME_COLOR);
        assertTrue(bufferedImagesEqual(expected, actual));
    }

    static class DrawDifferenceArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(MESSAGE_FRAME_IS_BIGGER_THAN_IMAGE, 10, "image_big_margin.png", initComparison()),
                    Arguments.of(MESSAGE_FRAME_IS_SMALLER_THAN_IMAGE, 1, "image_small_margin.png", initComparison())
            );
        }

        private static boolean[][] initComparison() {
            return new boolean[][]{{false, false, false, false, false},
                    {false, false, false, false, false},
                    {false, false, true, false, false},
                    {false, false, false, false, false},
                    {false, false, false, false, false}};
        }
    }
}