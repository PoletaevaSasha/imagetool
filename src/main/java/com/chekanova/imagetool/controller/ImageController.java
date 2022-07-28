package com.chekanova.imagetool.controller;

import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG;

import com.chekanova.imagetool.enums.ImageProcessorType;
import com.chekanova.imagetool.enums.ParallelingStrategyType;
import com.chekanova.imagetool.service.ImageService;
import com.chekanova.imagetool.validation.FileValidationUtil;
import com.chekanova.imagetool.validation.HexValidationUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping(value = "/process",
            produces = IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> process(@RequestParam MultipartFile file,
                                          @RequestParam ImageProcessorType type,
                                          @RequestParam ParallelingStrategyType strategy,
                                          @RequestParam(required = false) Integer borderSize,
                                          @RequestParam(required = false) String frameColor,
                                          RedirectAttributes attributes) throws IOException, InterruptedException {
        if (frameColor != null) {
            HexValidationUtil.validateHex(frameColor, attributes);
        }
        FileValidationUtil.validateFile(file, attributes);
        ByteArrayOutputStream resultImage = imageService.process(file, type, strategy, borderSize, frameColor);
        return ResponseEntity.ok().contentType(IMAGE_JPEG).body(resultImage.toByteArray());
    }

    @PostMapping(value = "/compare",
            produces = IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> compare(@RequestParam MultipartFile file1,
                                          @RequestParam MultipartFile file2,
                                          RedirectAttributes attributes) throws IOException {
        return ResponseEntity.ok().contentType(IMAGE_PNG).body(imageService.compare(file1, file2).toByteArray());
    }
}