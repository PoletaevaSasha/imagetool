package com.chekanova.phototool.service.strategy.impl;

import com.chekanova.phototool.service.processor.ImageProcessor;
import com.chekanova.phototool.service.strategy.ProcessingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExecutorServiceProcessingStrategy implements ProcessingStrategy {

    @Override
    public void recolor(ImageProcessor imageProcessor, BufferedImage originalImage, BufferedImage resultImage, int numberOfThreads) {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        int width = originalImage.getWidth();
        int height = (int) Math.ceil(originalImage.getHeight() / (double) numberOfThreads);
        for (int i = 0; i < numberOfThreads; i++) {
            int threadMultiplier = i;
            Runnable thread = new Thread(() -> {
                int xOrigin = 0;
                int yOrigin = height * threadMultiplier;
                recolorImage(imageProcessor, originalImage, resultImage, xOrigin, yOrigin, width, height);
            });
            executor.submit(thread);
        }
        awaitTerminationAfterShutdown(executor);
    }
    private void awaitTerminationAfterShutdown(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException ex) {
            threadPool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
