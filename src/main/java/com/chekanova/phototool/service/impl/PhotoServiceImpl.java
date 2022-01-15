package com.chekanova.phototool.service.impl;

import com.chekanova.phototool.enums.ImageProcessorType;
import com.chekanova.phototool.enums.MultithreadingStrategy;
import com.chekanova.phototool.service.PhotoService;
import com.chekanova.phototool.service.processor.ImageProcessor;
import com.chekanova.phototool.service.strategy.ProcessingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PhotoServiceImpl implements PhotoService {
    private final Map<MultithreadingStrategy, ProcessingStrategy> processingStrategies;
    private final Map<ImageProcessorType, ImageProcessor> imageProcessors;

    public byte[] reprocessImage(byte[] imageData, ImageProcessorType imageProcessorType, MultithreadingStrategy strategy, int numberOfThreads) throws IOException, InterruptedException {
        ByteArrayInputStream bais = new ByteArrayInputStream(imageData);
        BufferedImage originalImage = ImageIO.read(bais);
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        ImageProcessor imageProcessor = imageProcessors.get(imageProcessorType);
        invokeCalc(imageProcessor, originalImage, resultImage, strategy, numberOfThreads);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(resultImage, "jpg", baos);
        return baos.toByteArray();
    }

    private void invokeCalc(ImageProcessor imageProcessor, BufferedImage originalImage, BufferedImage resultImage, MultithreadingStrategy strategy, int numberOfThreads) throws InterruptedException {
        long startTime = System.currentTimeMillis();

        processingStrategies.get(strategy).recolor(imageProcessor, originalImage, resultImage, numberOfThreads);
        long endTime = System.currentTimeMillis();
        log.info("{} number of threads {} time duration = {}", strategy, numberOfThreads, endTime - startTime);
    }
}
