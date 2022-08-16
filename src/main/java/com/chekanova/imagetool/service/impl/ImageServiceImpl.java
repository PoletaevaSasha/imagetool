package com.chekanova.imagetool.service.impl;

import com.chekanova.imagetool.enums.ImageProcessorType;
import com.chekanova.imagetool.enums.ParallelingStrategyType;
import com.chekanova.imagetool.service.ImageService;
import com.chekanova.imagetool.service.comparison.DrawDifferenceService;
import com.chekanova.imagetool.service.comparison.ImageComparisonService;
import com.chekanova.imagetool.service.processor.ImageProcessor;
import com.chekanova.imagetool.service.strategy.ParallelingStrategy;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;
import javax.imageio.ImageIO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements ImageService {
    private static final String PNG = "png";
    private static final String JPG = "jpg";
    private final Map<ParallelingStrategyType, ParallelingStrategy> processingStrategies;
    private final Map<ImageProcessorType, ImageProcessor> imageProcessors;
    private final ImageComparisonService imageComparisonService;
    private final DrawDifferenceService drawDifferenceService;

    @Override
    public ByteArrayOutputStream process(MultipartFile file, ImageProcessorType imageProcessorType, ParallelingStrategyType strategy,
                                         int borderSize, String borderColorHex) throws InterruptedException, IOException {
        BufferedImage originalImage = getBufferedImage(file);
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), originalImage.getType());
        ImageProcessor imageProcessor = imageProcessors.get(imageProcessorType);
        long startTime = System.currentTimeMillis();
        processingStrategies.get(strategy).recolor(imageProcessor, originalImage, resultImage);
        log.debug("processing time for {} time duration = {}", strategy, System.currentTimeMillis() - startTime);
        if (borderSize > 0) {
            resultImage = drawBorder(resultImage, borderSize, borderColorHex);
        }
        return getByteArrayOutputStream(resultImage, JPG);
    }

    @Override
    public ByteArrayOutputStream compare(MultipartFile file1, MultipartFile file2) throws IOException {
        BufferedImage image1 = getBufferedImage(file1);
        BufferedImage image2 = getBufferedImage(file2);
        boolean[][] comparisonResult = imageComparisonService.compare(image1, image2);
        BufferedImage resultImage = drawDifferenceService.drawDifference(comparisonResult, image1);
        return getByteArrayOutputStream(resultImage, PNG);
    }

    private BufferedImage getBufferedImage(MultipartFile file) throws IOException {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file.getBytes());
        return ImageIO.read(byteArrayInputStream);
    }

    private ByteArrayOutputStream getByteArrayOutputStream(BufferedImage image, String formatName) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(image, formatName, byteArrayOutputStream);
        return byteArrayOutputStream;
    }

    private BufferedImage drawBorder(BufferedImage img, int borderSize, String borderColorHex) {
        Image scaledImage = img.getScaledInstance(img.getWidth() - borderSize * 2,
                img.getHeight() - borderSize * 2, Image.SCALE_SMOOTH);
        BufferedImage background = new BufferedImage(img.getWidth(), img.getHeight(), img.getType());

        Graphics2D g2d = background.createGraphics();
        Color color = Color.decode(borderColorHex);
        g2d.setPaint(color);
        g2d.fillRect(0, 0, background.getWidth(), background.getHeight());
        int startingX = borderSize;
        int startingY = borderSize;
        g2d.drawImage(scaledImage, startingX, startingY, null);
        g2d.dispose();

        return background;
    }
}
