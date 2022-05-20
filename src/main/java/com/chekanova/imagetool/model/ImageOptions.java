package com.chekanova.imagetool.model;

import com.chekanova.imagetool.service.processor.ImageProcessor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ImageOptions {
    private ImageProcessor imageProcessor;
    private int leftCorner;
    private int topCorner;
    private int width;
    private int height;
}
