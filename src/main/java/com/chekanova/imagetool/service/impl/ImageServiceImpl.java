package com.chekanova.imagetool.service.impl;

import com.chekanova.imagetool.enums.ImageProcessorType;
import com.chekanova.imagetool.enums.MultithreadingStrategy;
import com.chekanova.imagetool.service.ImageService;
import com.chekanova.imagetool.service.comparison.DrawDifferenceService;
import com.chekanova.imagetool.service.comparison.ImageComparisonService;
import com.chekanova.imagetool.service.processor.ImageProcessor;
import com.chekanova.imagetool.service.strategy.ProcessingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private static final String PNG = "png";
    private static final String JPG = "jpg";
    private final Map<MultithreadingStrategy, ProcessingStrategy> processingStrategies;
    private final Map<ImageProcessorType, ImageProcessor> imageProcessors;
    private final ImageComparisonService imageComparisonService;
    private final DrawDifferenceService drawDifferenceService;

    @Override
    public ByteArrayOutputStream reprocess(MultipartFile file, ImageProcessorType imageProcessorType, MultithreadingStrategy strategy) throws InterruptedException, IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file.getBytes());
        BufferedImage originalImage = ImageIO.read(byteArrayInputStream);
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
        ImageProcessor imageProcessor = imageProcessors.get(imageProcessorType);
        long startTime = System.currentTimeMillis();
        processingStrategies.get(strategy).recolor(imageProcessor, originalImage, resultImage);
        long endTime = System.currentTimeMillis();
        log.info("processing time for {} time duration = {}", strategy, endTime - startTime);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(resultImage, JPG, byteArrayOutputStream);
        return byteArrayOutputStream;
    }

    @Override
    public ByteArrayOutputStream compare(MultipartFile file1, MultipartFile file2) throws IOException {
        BufferedImage image1 = ImageIO.read(new ByteArrayInputStream(file1.getBytes()));
        BufferedImage image2 = ImageIO.read(new ByteArrayInputStream(file2.getBytes()));
        boolean[][] comparisonResult = imageComparisonService.compare(image1, image2);
        BufferedImage resultImage = drawDifferenceService.drawDifference(comparisonResult, image1);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(resultImage, PNG, byteArrayOutputStream);
        return byteArrayOutputStream;
    }
}
