package com.chekanova.imagetool.service;

import com.chekanova.imagetool.enums.ImageProcessorType;
import com.chekanova.imagetool.enums.MultithreadingStrategy;
import com.chekanova.imagetool.service.impl.ImageServiceImpl;
import com.chekanova.imagetool.service.processor.ImageProcessor;
import com.chekanova.imagetool.service.processor.impl.AbstractConvolutionProcessor;
import com.chekanova.imagetool.service.strategy.ProcessingStrategy;
import com.chekanova.imagetool.service.strategy.impl.ExecutorServiceProcessingStrategy;
import com.chekanova.imagetool.service.strategy.impl.ForkJoinPoolStrategy;
import com.chekanova.imagetool.service.strategy.impl.SingleProcessingStrategy;
import com.chekanova.imagetool.service.strategy.impl.ThreadProcessingStrategy;
import org.junit.Before;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

import static com.chekanova.imagetool.enums.ImageProcessorType.BLUR;
import static com.chekanova.imagetool.enums.MultithreadingStrategy.EXECUTOR_SERVICE;
import static com.chekanova.imagetool.enums.MultithreadingStrategy.FORK_JOIN;
import static com.chekanova.imagetool.enums.MultithreadingStrategy.SINGLE;
import static com.chekanova.imagetool.enums.MultithreadingStrategy.THREADS;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImageServiceTest {
    public static final String SOURCE_FILE = "src/test/resources/tiger.jpg";

    private ImageServiceImpl testedObject;
    @Mock
    private Map<MultithreadingStrategy, ProcessingStrategy> processingStrategies;
    @Mock
    private Map<ImageProcessorType, ImageProcessor> imageProcessors;

    @InjectMocks
    private IdentityConvolutionProcessor identityConvolutionProcessor;
    @InjectMocks
    private SingleProcessingStrategy singleProcessingStrategy;
    @InjectMocks
    private ThreadProcessingStrategy threadProcessingStrategy;
    @InjectMocks
    private ExecutorServiceProcessingStrategy executorServiceProcessingStrategy;
    @InjectMocks
    private ForkJoinPoolStrategy forkJoinPoolStrategy;

    @Before
    public void setUp()  {
        testedObject = new ImageServiceImpl(processingStrategies,imageProcessors);
        when(imageProcessors.get(any()))
                .thenReturn(identityConvolutionProcessor);
    }

    @Test
    public void testSingleConvolution() throws IOException, InterruptedException {
        when(processingStrategies.get(any(MultithreadingStrategy.class)))
                .thenReturn(singleProcessingStrategy);
        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage result = testedObject.reprocessImage(originalImage, BLUR, SINGLE, 1);

        assertTrue("For identity filter results should be equals ", bufferedImagesEqual(originalImage, result));
    }

    @Test
    public void testThreadConvolution() throws IOException, InterruptedException {
        when(processingStrategies.get(any(MultithreadingStrategy.class)))
                .thenReturn(threadProcessingStrategy);

        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage result = testedObject.reprocessImage(originalImage, BLUR, THREADS, 1);

        assertTrue("For identity filter results should be equals ", bufferedImagesEqual(originalImage, result));
    }

    @Test
    public void testExecutorServiceConvolution() throws IOException, InterruptedException {
        when(processingStrategies.get(any(MultithreadingStrategy.class)))
                .thenReturn(executorServiceProcessingStrategy);

        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage result = testedObject.reprocessImage(originalImage, BLUR, EXECUTOR_SERVICE, 10);

        assertTrue("For identity filter results should be equals ", bufferedImagesEqual(originalImage, result));
    }

    @Test
    public void testForkJoinPoolConvolution() throws IOException, InterruptedException {
        when(processingStrategies.get(any(MultithreadingStrategy.class)))
                .thenReturn(forkJoinPoolStrategy);

        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage result = testedObject.reprocessImage(originalImage, BLUR, FORK_JOIN, 10);

        assertTrue("For identity filter results should be equals ", bufferedImagesEqual(originalImage, result));
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

    private boolean bufferedImagesEqual(BufferedImage img1, BufferedImage img2) {
        if (img1.getWidth() == img2.getWidth() && img1.getHeight() == img2.getHeight()) {
            for (int x = 0; x < img1.getWidth(); x++) {
                for (int y = 0; y < img1.getHeight(); y++) {
                    if (img1.getRGB(x, y) != img2.getRGB(x, y))
                        return false;
                }
            }
        } else {
            return false;
        }
        return true;
    }
}