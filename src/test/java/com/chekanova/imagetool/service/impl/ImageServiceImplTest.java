package com.chekanova.imagetool.service.impl;

import com.chekanova.imagetool.service.AbstractImageToolTest;
import com.chekanova.imagetool.service.ImageService;
import java.awt.image.BufferedImage;
import java.io.File;
import java.lang.reflect.Method;
import java.util.stream.Stream;
import javax.imageio.ImageIO;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
class ImageServiceImplTest extends AbstractImageToolTest {
    public static final String SOURCE_FILE = "src/test/resources/drawBorderTestsPictures/";
    public static final String ORIGINAL_FILE_NAME = "original_image.jpg";
    public static ImageService emptyInstance;
    public static Method drawBorderMethod;

    @BeforeAll
    static void setUp() throws Exception {
        emptyInstance = new ImageServiceImpl(null, null, null, null);
        Class[] params = {BufferedImage.class, Integer.class, String.class};
        drawBorderMethod = ImageServiceImpl.class.getDeclaredMethod("drawBorder", params);
        drawBorderMethod.setAccessible(true);
    }

    @ParameterizedTest(name = "{index}, {3}")
    @ArgumentsSource(DrawBorderArgumentsProvider.class)
    void testDrawBorderValidData(Integer borderSize, String color, String expectedFileName, String massage) throws Exception {
        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE + ORIGINAL_FILE_NAME));

        Object value = drawBorderMethod.invoke(emptyInstance, originalImage, borderSize, color);
        BufferedImage actualImage = (BufferedImage) value;

        BufferedImage expectedImage = ImageIO.read(new File(SOURCE_FILE + expectedFileName));
        Assert.assertTrue(massage, bufferedImagesEqual(actualImage, expectedImage));
    }

    static class DrawBorderArgumentsProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) throws Exception {
            return Stream.of(
                    Arguments.of(10, "#FF00FF", "magenta_border_image.jpg", "Should draw a magenta border"),
                    Arguments.of(20, null, "black_border_image.jpg", "Should draw a black border with null color argument"),
                    Arguments.of(0, "#FF00FF", ORIGINAL_FILE_NAME, "Should not change the image with border size 0"),
                    Arguments.of(null, "#FF00FF", ORIGINAL_FILE_NAME, "Should not change the image with border size null argument"),
                    Arguments.of(-15, "#FF00FF", ORIGINAL_FILE_NAME, "Should not change the image with negative border size")
            );
        }
    }
}
