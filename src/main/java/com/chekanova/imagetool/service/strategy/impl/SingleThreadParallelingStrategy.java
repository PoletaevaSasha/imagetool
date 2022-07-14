package com.chekanova.imagetool.service.strategy.impl;

import com.chekanova.imagetool.model.ImageOptions;
import com.chekanova.imagetool.service.processor.ImageProcessor;
import com.chekanova.imagetool.service.strategy.ParallelingStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;

/**
 * Processing strategy that doesn't use multithreading for performance improvement
 * @author oleksandra.chekanova
 */
@Service
@RequiredArgsConstructor
public class SingleThreadParallelingStrategy implements ParallelingStrategy {

    @Override
    public void recolor(ImageProcessor imageProcessor, BufferedImage originalImage, BufferedImage resultImage) {
        ImageOptions options = new ImageOptions(imageProcessor, 0, 0, originalImage.getWidth(), originalImage.getHeight());
        recolorImage(options, originalImage, resultImage);
    }
}
