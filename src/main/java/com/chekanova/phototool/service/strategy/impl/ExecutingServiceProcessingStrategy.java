package com.chekanova.phototool.service.strategy.impl;

import com.chekanova.phototool.service.processor.ImageProcessor;
import com.chekanova.phototool.service.strategy.ProcessingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExecutingServiceProcessingStrategy implements ProcessingStrategy {

    @Override
    public void recolor(ImageProcessor imageProcessor, BufferedImage originalImage, BufferedImage resultImage, int numberOfThreads) throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        int width = originalImage.getWidth();
        int height = (int) Math.ceil(originalImage.getHeight() / (double) numberOfThreads);

        Collection<Callable<Void>> threads = new ArrayList<>();
        for (int i = 0; i < numberOfThreads; i++) {
            final int threadMultiplier = i;

            Callable<Void> thread = () -> {
                int xOrigin = 0;
                int yOrigin = height * threadMultiplier;
                recolorImage(imageProcessor, originalImage, resultImage, xOrigin, yOrigin, width, height);
                return null;
            };
            threads.add(thread);
        }
        executor.invokeAll(threads);
    }
}
