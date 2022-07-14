package com.chekanova.imagetool.service.strategy.impl;

import com.chekanova.imagetool.model.ImageOptions;
import com.chekanova.imagetool.service.processor.ImageProcessor;
import com.chekanova.imagetool.service.strategy.ParallelingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Processing strategy that uses {@link Thread} for performance improvement
 * @author oleksandra.chekanova
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MultipleThreadParallelingStrategy implements ParallelingStrategy {

    @Override
    public void recolor(ImageProcessor imageProcessor, BufferedImage originalImage, BufferedImage resultImage) throws InterruptedException {
        int numberOfThreads = Runtime.getRuntime().availableProcessors();
        List<Thread> threads = createThreads(imageProcessor, originalImage, resultImage,numberOfThreads);
        for (Thread thread : threads) {
            thread.start();
        }
        for (Thread thread : threads) {
                thread.join();
        }
    }

    private List<Thread> createThreads(ImageProcessor imageProcessor, BufferedImage originalImage, BufferedImage resultImage, int numberOfThreads) {
        List<Thread> threads = new ArrayList<>();
        int width = originalImage.getWidth();
        int height = (int) Math.ceil(originalImage.getHeight() / (double) numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            int threadMultiplier = i;
            Thread thread = new Thread(() -> {
                int xOrigin = 0;
                int yOrigin = height * threadMultiplier;
                ImageOptions options = new ImageOptions(imageProcessor, xOrigin, yOrigin, width, height);
                recolorImage(options, originalImage, resultImage);
            });

            threads.add(thread);
        }
        return threads;
    }
}
