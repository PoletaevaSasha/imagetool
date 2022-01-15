package com.chekanova.phototool.config;

import com.chekanova.phototool.enums.ImageProcessorType;
import com.chekanova.phototool.service.processor.ImageProcessor;
import com.chekanova.phototool.service.processor.impl.BlurConvolutionProcessor;
import com.chekanova.phototool.service.processor.impl.EdgeDetectionConvolutionProcessor;
import com.chekanova.phototool.service.processor.impl.GrayScaleImageProcessor;
import com.chekanova.phototool.service.processor.impl.SharpConvolutionProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.EnumMap;
import java.util.Map;

import static com.chekanova.phototool.enums.ImageProcessorType.BLUR;
import static com.chekanova.phototool.enums.ImageProcessorType.EDGE_DETECTION;
import static com.chekanova.phototool.enums.ImageProcessorType.GRAY_SCALE;
import static com.chekanova.phototool.enums.ImageProcessorType.SHARP;

@Configuration
@RequiredArgsConstructor
public class ImageProcessorConfig {
    private final GrayScaleImageProcessor grayScaleImageProcessor;
    private final BlurConvolutionProcessor blurConvolutionProcessor;
    private final EdgeDetectionConvolutionProcessor edgeDetectionConvolutionProcessor;
    private final SharpConvolutionProcessor sharpConvolutionProcessor;

    @Bean
    public Map<ImageProcessorType, ImageProcessor> imageProcessors() {
        EnumMap<ImageProcessorType,ImageProcessor> processors = new EnumMap<>(ImageProcessorType.class);
        processors.put(GRAY_SCALE, grayScaleImageProcessor);
        processors.put(BLUR, blurConvolutionProcessor);
        processors.put(EDGE_DETECTION, edgeDetectionConvolutionProcessor);
        processors.put(SHARP, sharpConvolutionProcessor);
        return processors;
    }
}
