package com.chekanova.imagetool.controller;

import com.chekanova.imagetool.enums.ImageProcessorType;
import com.chekanova.imagetool.enums.ParallelingStrategyType;
import com.chekanova.imagetool.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static com.chekanova.imagetool.validation.ValidationUtil.validateFile;
import static org.springframework.http.MediaType.IMAGE_JPEG;
import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;
import static org.springframework.http.MediaType.IMAGE_PNG;

@Validated
@RestController
@RequestMapping("/v1/image")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;
    private static final String HEX_PATTERN = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$";


    @PostMapping(value = "/process",
            produces = IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> process(@RequestParam MultipartFile file,
                                          @RequestParam ImageProcessorType type,
                                          @RequestParam ParallelingStrategyType strategy,
                                          RedirectAttributes attributes) throws IOException, InterruptedException {
        validateFile(file, attributes);
        ByteArrayOutputStream resultImage = imageService.process(file, type, strategy);
        return ResponseEntity.ok().contentType(IMAGE_JPEG).body(resultImage.toByteArray());
    }

    @PostMapping(value = "/compare",
            produces = IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> compare(@RequestParam MultipartFile file1,
                                          @RequestParam MultipartFile file2,
                                          @RequestParam(defaultValue = "#ff0000")
                                          @Valid @Pattern(regexp = HEX_PATTERN) String hexColorCode) throws IOException {
        Color color = Color.decode(hexColorCode);
        return ResponseEntity.ok().contentType(IMAGE_PNG).body(imageService.compare(file1, file2, color).toByteArray());
    }
}