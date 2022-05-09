package com.chekanova.phototool.service;

import com.chekanova.phototool.enums.ImageProcessorType;
import com.chekanova.phototool.enums.MultithreadingStrategy;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface PhotoService {
    BufferedImage reprocessImage(BufferedImage originalImage, ImageProcessorType imageProcessorType, MultithreadingStrategy strategy, int numberOfThreads) throws IOException, InterruptedException;
}
