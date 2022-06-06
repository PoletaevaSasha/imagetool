package com.chekanova.imagetool.service;

import com.chekanova.imagetool.enums.ImageProcessorType;
import com.chekanova.imagetool.enums.MultithreadingStrategy;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public interface ImageService {
    BufferedImage reprocess(BufferedImage originalImage, ImageProcessorType imageProcessorType, MultithreadingStrategy strategy, int numberOfThreads) throws InterruptedException;

    /**
     * Compares two images with the same size by each pixel and returns first image,
     * where all differences are marked with frames.
     *
     * @param file1 must not be null. File with first image for comparison.
     * @param file2 must not be null. File with second image for comparison.
     * @return ByteArrayOutputStream with first image, where all differences are marked with frames
     */
    ByteArrayOutputStream compare(MultipartFile file1, MultipartFile file2) throws IOException;
}
