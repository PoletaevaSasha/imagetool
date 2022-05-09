package com.chekanova.imagetool.service.processor.impl;

import org.springframework.stereotype.Service;

@Service
public class SharpConvolutionProcessor extends AbstractConvolutionProcessor {
    private static final double[][] FILTER = {{0, -1, 0}, {-1, 5, -1}, {0, -1, 0}};

    public double[][] getFilter() {
        return FILTER;
    }

    public int getFilterSize() {
        return FILTER.length;
    }
}
