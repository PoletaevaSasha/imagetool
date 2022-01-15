package com.chekanova.phototool.service.strategy.impl;

import com.chekanova.phototool.service.processor.ImageProcessor;
import com.chekanova.phototool.service.strategy.ProcessingStrategy;
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
        SimpleRecursiveAction action = new SimpleRecursiveAction(imageProcessor, originalImage, resultImage, 0, 0, originalImage.getWidth(), originalImage.getHeight(), threshold);
        forkJoinPool.invoke(action);
    }

    class SimpleRecursiveAction extends RecursiveAction {

        private ImageProcessor imageProcessor;
        private transient BufferedImage originalImage;
        private transient BufferedImage resultImage;
        int leftCorner;
        int topCorner;
        int width;
        int height;
        int threshold;

        //TODO: decrease number of params
        public SimpleRecursiveAction(ImageProcessor imageProcessor, BufferedImage originalImage, BufferedImage resultImage, int leftCorner, int topCorner, int width, int height, int threshold) {
            this.imageProcessor = imageProcessor;
            this.originalImage = originalImage;
            this.resultImage = resultImage;
            this.leftCorner = leftCorner;
            this.topCorner = topCorner;
            this.width = width;
            this.height = height;
            this.threshold = threshold;
        }

        @Override
        protected void compute() {
            if (height > threshold && height > 1) {
                int newHeight = height / 2;
                ForkJoinTask.invokeAll(createSubtasks(newHeight))
                        .forEach(SimpleRecursiveAction::join);
            } else {
                recolorImage(imageProcessor, originalImage, resultImage, leftCorner, topCorner, width, height);
            }
        }

        private List<SimpleRecursiveAction> createSubtasks(int newHeight) {
            List<SimpleRecursiveAction> tasks = new ArrayList<>();
            tasks.add(new SimpleRecursiveAction(imageProcessor, originalImage, resultImage, leftCorner, topCorner, width, newHeight, threshold));
            tasks.add(new SimpleRecursiveAction(imageProcessor, originalImage, resultImage, leftCorner, topCorner + newHeight, width, height - newHeight, threshold));
            return tasks;
        }
    }
}
