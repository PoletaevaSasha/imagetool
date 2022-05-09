package com.chekanova.phototool.service.strategy.impl;

import com.chekanova.phototool.service.processor.ImageProcessor;
import com.chekanova.phototool.service.strategy.ProcessingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ThreadProcessingStrategy implements ProcessingStrategy {

    @Override
    public void recolor(ImageProcessor imageProcessor, BufferedImage originalImage, BufferedImage resultImage, int numberOfThreads) throws InterruptedException {
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

                recolorImage(imageProcessor, originalImage, resultImage, xOrigin, yOrigin, width, height);
            });

            threads.add(thread);
        }
        return threads;
    }
}
