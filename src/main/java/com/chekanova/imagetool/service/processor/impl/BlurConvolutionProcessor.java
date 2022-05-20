package com.chekanova.imagetool.service.processor.impl;

import org.springframework.stereotype.Service;

@Service
public class BlurConvolutionProcessor extends AbstractConvolutionProcessor {

    private static final double[][] FILTER =
            {{1.0/256, 4.0/256, 6.0/256, 4.0/256, 1.0/256},
                    {4.0/256, 16.0/256, 24.0/256, 16.0/256, 4.0/256},
                    {6.0/256, 24.0/256, 36.0/256, 24.0/256, 6.0/256},
                    {4.0/256, 16.0/256, 24.0/256, 16.0/256, 4.0/256},
                    {1.0/256, 4.0/256, 6.0/256, 4.0/256, 1.0/256}};


    public double[][] getFilter() {
        return FILTER;
    }

    public int getFilterSize() {
        return FILTER.length;
    }

}
