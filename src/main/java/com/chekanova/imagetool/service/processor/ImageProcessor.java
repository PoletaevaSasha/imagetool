package com.chekanova.imagetool.service.processor;

import java.awt.image.BufferedImage;

public interface ImageProcessor {
    void recolorPixel(BufferedImage originalImage, BufferedImage resultImage, int x, int y);
}
