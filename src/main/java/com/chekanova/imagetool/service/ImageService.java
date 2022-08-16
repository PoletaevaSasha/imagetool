package com.chekanova.imagetool.service;

import com.chekanova.imagetool.enums.ImageProcessorType;
import com.chekanova.imagetool.enums.ParallelingStrategyType;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

/**
 * Basic interface for working with images
 *
 * @author oleksandra.chekanova
 */
public interface ImageService {
    /**
     * Processes {@code originalImage} according to the image processor type and returns the result
     *
     * @param originalImage      must not be null. Original image
     * @param imageProcessorType must not be null. Defines how the image is processed (for example gray-scale, blur... )
     * @param strategy           must not be null. Defines multithreading strategy for image processing and the framework for paralleling processing.
     * @param borderSize         number of pixels of border width/height which will be generated
     * @param borderColorHex     color of border which will be generated
     * @return ByteArrayOutputStream result with the changed image
     */
    ByteArrayOutputStream process(MultipartFile originalImage, ImageProcessorType imageProcessorType, ParallelingStrategyType strategy, int borderSize, String borderColorHex) throws InterruptedException, IOException;

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
