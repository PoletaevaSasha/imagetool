package com.chekanova.imagetool.service.impl;

import com.chekanova.imagetool.enums.ImageProcessorType;
import com.chekanova.imagetool.enums.MultithreadingStrategy;
import com.chekanova.imagetool.service.ImageService;
import com.chekanova.imagetool.service.processor.ImageProcessor;
import com.chekanova.imagetool.service.strategy.ProcessingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private final Map<MultithreadingStrategy, ProcessingStrategy> processingStrategies;
    private final Map<ImageProcessorType, ImageProcessor> imageProcessors;

    @Override
    public BufferedImage reprocess(BufferedImage originalImage, ImageProcessorType imageProcessorType, MultithreadingStrategy strategy, int numberOfThreads) throws InterruptedException {
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
        ImageProcessor imageProcessor = imageProcessors.get(imageProcessorType);
        long startTime = System.currentTimeMillis();
        processingStrategies.get(strategy).recolor(imageProcessor, originalImage, resultImage, numberOfThreads);
        long endTime = System.currentTimeMillis();
        log.info("{} number of threads {} time duration = {}", strategy, numberOfThreads, endTime - startTime);
        return resultImage;
    }
}
