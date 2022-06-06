package com.chekanova.imagetool.service.strategy.impl;

import com.chekanova.imagetool.service.AbstractImageToolTest;
import com.chekanova.imagetool.service.processor.impl.AbstractConvolutionProcessor;
import org.mockito.InjectMocks;


public abstract class AbstractProcessingStrategyTest  extends AbstractImageToolTest {
    @InjectMocks
    private IdentityConvolutionProcessor identityConvolutionProcessor;

    static class IdentityConvolutionProcessor extends AbstractConvolutionProcessor {

        private final double[][] FILTER = {{0, 0, 0}, {0, 1, 0}, {0, 0, 0}};

        public double[][] getFilter() {
            return FILTER;
        }

        public int getFilterSize() {
            return FILTER.length;
        }
    }

    protected IdentityConvolutionProcessor getIdentityConvolutionProcessor() {
        return identityConvolutionProcessor;
    }
}
