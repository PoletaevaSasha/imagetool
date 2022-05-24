package com.chekanova.imagetool.controller;

import com.chekanova.imagetool.enums.ImageProcessorType;
import com.chekanova.imagetool.enums.MultithreadingStrategy;
import com.chekanova.imagetool.service.ImageService;
import com.chekanova.imagetool.validation.FileValidationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {
    private static final String JPG = "jpg";
    private final ImageService imageService;

    @PostMapping(value = "/process",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> process(@RequestParam MultipartFile file,
                                          @RequestParam ImageProcessorType type,
                                          @RequestParam MultithreadingStrategy strategy,
                                          @RequestParam int numberOfThreads,
                                          RedirectAttributes attributes) throws IOException, InterruptedException {
        FileValidationUtil.validateFile(file, attributes);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(file.getBytes());
        BufferedImage originalImage = ImageIO.read(byteArrayInputStream);
        BufferedImage resultImage = imageService.reprocess(originalImage, type, strategy, numberOfThreads);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(resultImage, JPG, byteArrayOutputStream);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(byteArrayOutputStream.toByteArray());
    }

    @PostMapping(value = "/compare",
            produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> compare(@RequestParam MultipartFile file1,
                                          @RequestParam MultipartFile file2,
                                          RedirectAttributes attributes) throws IOException {
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(imageService.compare(file1, file2).toByteArray());
    }
}