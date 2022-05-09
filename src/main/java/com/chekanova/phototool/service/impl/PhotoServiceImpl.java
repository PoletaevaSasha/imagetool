package com.chekanova.phototool.service.impl;

import com.chekanova.phototool.enums.ImageProcessorType;
import com.chekanova.phototool.enums.MultithreadingStrategy;
import com.chekanova.phototool.service.PhotoService;
import com.chekanova.phototool.service.processor.ImageProcessor;
import com.chekanova.phototool.service.strategy.ProcessingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Map;

import static java.awt.image.BufferedImage.TYPE_INT_RGB;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {
    private final Map<MultithreadingStrategy, ProcessingStrategy> processingStrategies;
    private final Map<ImageProcessorType, ImageProcessor> imageProcessors;

    public BufferedImage reprocessImage(BufferedImage originalImage,  ImageProcessorType imageProcessorType, MultithreadingStrategy strategy, int numberOfThreads) throws InterruptedException {
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
        ImageProcessor imageProcessor = imageProcessors.get(imageProcessorType);
        invokeCalc(imageProcessor, originalImage, resultImage, strategy, numberOfThreads);
        return resultImage;
    }

    private void invokeCalc(ImageProcessor imageProcessor, BufferedImage originalImage, BufferedImage resultImage, MultithreadingStrategy strategy, int numberOfThreads) throws InterruptedException {
        long startTime = System.currentTimeMillis();
        processingStrategies.get(strategy).recolor(imageProcessor, originalImage, resultImage, numberOfThreads);
        long endTime = System.currentTimeMillis();
        log.info("{} number of threads {} time duration = {}", strategy, numberOfThreads, endTime - startTime);
    }
}
