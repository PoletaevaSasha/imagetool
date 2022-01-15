package com.chekanova.phototool.service;

import com.chekanova.phototool.enums.ImageProcessorType;
import com.chekanova.phototool.enums.MultithreadingStrategy;
import java.io.IOException;

public interface PhotoService {
    byte[] reprocessImage(byte[] imageData, ImageProcessorType imageProcessorType, MultithreadingStrategy strategy, int numberOfThreads) throws IOException, InterruptedException;
}
