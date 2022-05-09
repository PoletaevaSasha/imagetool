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
    private final ImageService imageService;
    private static final String JPG = "jpg";

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
        BufferedImage resultImage = imageService.reprocessImage(originalImage, type, strategy, numberOfThreads);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(resultImage, JPG, byteArrayOutputStream);
        return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(byteArrayOutputStream.toByteArray());
    }
}
