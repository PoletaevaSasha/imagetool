package com.chekanova.imagetool.service.strategy.impl;

import com.chekanova.imagetool.enums.ParallelingStrategyType;
import com.chekanova.imagetool.service.AbstractImageToolTest;
import com.chekanova.imagetool.service.processor.impl.AbstractConvolutionProcessor;
import com.chekanova.imagetool.service.strategy.ParallelingStrategy;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;

import static com.chekanova.imagetool.enums.ParallelingStrategyType.EXECUTOR_SERVICE;
import static com.chekanova.imagetool.enums.ParallelingStrategyType.FORK_JOIN;
import static com.chekanova.imagetool.enums.ParallelingStrategyType.SINGLE;
import static com.chekanova.imagetool.enums.ParallelingStrategyType.THREADS;
import static org.springframework.test.util.AssertionErrors.assertTrue;

class ParallelingStrategyTest extends AbstractImageToolTest {
    private static final String SOURCE_FILE = "src/test/resources/tiger.jpg";
    private static Map<ParallelingStrategyType, ParallelingStrategy> strategies;
    private static IdentityConvolutionProcessor identityConvolutionProcessor;

    @BeforeAll
    public static void setUp() {
        strategies = new EnumMap<>(ParallelingStrategyType.class);
        strategies.put(SINGLE, new SingleThreadParallelingStrategy());
        strategies.put(THREADS, new MultipleThreadParallelingStrategy());
        strategies.put(EXECUTOR_SERVICE, new ExecutorServiceParallelingStrategy());
        strategies.put(FORK_JOIN, new ForkJoinPoolParallelingStrategy());
        identityConvolutionProcessor = new IdentityConvolutionProcessor();
    }

    @ParameterizedTest
    @EnumSource(ParallelingStrategyType.class)
    void testConvolution(ParallelingStrategyType type) throws IOException, InterruptedException {
        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage result = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
        strategies.get(type).recolor(identityConvolutionProcessor, originalImage, result);
        assertTrue("For identity filter results should be equals ",  bufferedImagesEqual(originalImage, result));
    }

    static class IdentityConvolutionProcessor extends AbstractConvolutionProcessor {

        private final double[][] FILTER = {{0, 0, 0}, {0, 1, 0}, {0, 0, 0}};

        public double[][] getFilter() {
            return FILTER;
        }

        public int getFilterSize() {
            return FILTER.length;
        }
    }
}
