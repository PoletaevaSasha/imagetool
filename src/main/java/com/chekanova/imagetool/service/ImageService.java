package com.chekanova.imagetool.service;

import com.chekanova.imagetool.enums.ImageProcessorType;
import com.chekanova.imagetool.enums.MultithreadingStrategy;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImageService {
    BufferedImage reprocess(BufferedImage originalImage, ImageProcessorType imageProcessorType, MultithreadingStrategy strategy, int numberOfThreads) throws IOException, InterruptedException;
}
