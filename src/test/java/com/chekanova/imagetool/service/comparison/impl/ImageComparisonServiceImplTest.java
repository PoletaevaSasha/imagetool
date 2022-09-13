package com.chekanova.imagetool.service.comparison.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

@ExtendWith(MockitoExtension.class)
class ImageComparisonServiceImplTest {
    private static final String SOURCE_FILE = "src/test/resources/";

    @InjectMocks
    private ImageComparisonServiceImpl testedObject;

    private static final boolean[][] comparisonWithDiff = {{false, false, false, false, false},
            {false, false, false, false, false},
            {false, false, true, false, false},
            {false, false, false, false, false},
            {false, false, false, false, false}};

    private static final boolean[][] comparisonWithoutDiff = {{false, false, false, false, false},
            {false, false, false, false, false},
            {false, false, false, false, false},
            {false, false, false, false, false},
            {false, false, false, false, false}};

    @DisplayName("Test image comparison")
    @ParameterizedTest(name = "{index}, {0}")
    @ArgumentsSource(ImageComparisonArgumentsProvider.class)
    void testCompare(String message, String fileName1, String fileName2, boolean[][] expected) throws IOException {
        BufferedImage image1 = ImageIO.read(new File(SOURCE_FILE + fileName1));
        BufferedImage image2 = ImageIO.read(new File(SOURCE_FILE + fileName2));
        boolean[][] actual = testedObject.compare(image1, image2);
        assertArrayEquals(expected, actual);
    }

    static class ImageComparisonArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of("Should return comparison result for similar images", "image3.png", "image2.png", comparisonWithoutDiff),
                    Arguments.of("Should return comparison result for different images", "image1.png", "image2.png", comparisonWithDiff)
            );
        }
    }
}