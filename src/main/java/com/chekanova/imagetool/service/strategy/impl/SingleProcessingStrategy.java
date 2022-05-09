package com.chekanova.imagetool.service.strategy.impl;

import com.chekanova.imagetool.model.ImageOptions;
import com.chekanova.imagetool.service.processor.ImageProcessor;
import com.chekanova.imagetool.service.strategy.ProcessingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

@Service
@RequiredArgsConstructor
public class SingleProcessingStrategy implements ProcessingStrategy {

    @Override
    public void recolor(ImageProcessor imageProcessor, BufferedImage originalImage, BufferedImage resultImage, int numberOfThreads) {
        ImageOptions options = new ImageOptions(imageProcessor, 0, 0, originalImage.getWidth(), originalImage.getHeight());
        recolorImage(options, originalImage, resultImage);
    }
}
