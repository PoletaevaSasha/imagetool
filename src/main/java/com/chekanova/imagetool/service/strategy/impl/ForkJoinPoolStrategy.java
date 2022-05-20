package com.chekanova.imagetool.service.strategy.impl;

import com.chekanova.imagetool.model.ImageOptions;
import com.chekanova.imagetool.service.processor.ImageProcessor;
import com.chekanova.imagetool.service.strategy.ProcessingStrategy;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

@Slf4j
@Service
@AllArgsConstructor
public class ForkJoinPoolStrategy implements ProcessingStrategy {

    @Override
    public void recolor(ImageProcessor imageProcessor, BufferedImage originalImage, BufferedImage resultImage, int numberOfThreads) {
        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        int threshold = originalImage.getHeight() / numberOfThreads;
        ImageOptions options = new ImageOptions(imageProcessor, 0, 0, originalImage.getWidth(), originalImage.getHeight());
        SimpleRecursiveAction action = new SimpleRecursiveAction(options, originalImage, resultImage, threshold);
        forkJoinPool.invoke(action);
    }

    class SimpleRecursiveAction extends RecursiveAction {

        private transient ImageOptions options;
        private transient BufferedImage originalImage;
        private transient BufferedImage resultImage;
        private int threshold;

        public SimpleRecursiveAction(ImageOptions options, BufferedImage originalImage, BufferedImage resultImage, int threshold) {
            this.options = options;
            this.originalImage = originalImage;
            this.resultImage = resultImage;
            this.threshold = threshold;
        }

        @Override
        protected void compute() {
            if (options.getHeight() > threshold && options.getHeight() > 1) {
                int newHeight = options.getHeight() / 2;
                ForkJoinTask.invokeAll(createSubtasks(options, newHeight))
                        .forEach(SimpleRecursiveAction::join);
            } else {
                recolorImage(options, originalImage, resultImage);
            }
        }

        private List<SimpleRecursiveAction> createSubtasks(ImageOptions options, int newHeight) {
            List<SimpleRecursiveAction> tasks = new ArrayList<>();
            ImageProcessor imageProcessor = options.getImageProcessor();
            int leftCorner = options.getLeftCorner();
            int topCorner = options.getTopCorner();
            int width = options.getWidth();
            int height = options.getHeight();
            ImageOptions options1 = new ImageOptions(imageProcessor, leftCorner, topCorner, width, newHeight);
            ImageOptions options2 = new ImageOptions(imageProcessor, leftCorner, topCorner + newHeight, width, height - newHeight);
            tasks.add(new SimpleRecursiveAction(options1, originalImage, resultImage, threshold));
            tasks.add(new SimpleRecursiveAction(options2, originalImage, resultImage, threshold));
            return tasks;
        }
    }
}
