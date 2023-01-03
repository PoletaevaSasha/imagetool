package com.chekanova.imagetool.controller;

import com.chekanova.imagetool.enums.ImageProcessorType;
import com.chekanova.imagetool.enums.ParallelingStrategyType;
import com.chekanova.imagetool.security.UserRoles;
import com.chekanova.imagetool.service.ImageService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.security.RolesAllowed;
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

    @RolesAllowed({UserRoles.ROLE_ONLY_PROCESS, UserRoles.ROLE_FULL})
    @ApiOperation(value = "To add effects to the uploaded image. It is possible to make the image blurry, sharp, grey-scale and highlight the edges")
    @PostMapping(value = "/process",
            produces = IMAGE_JPEG_VALUE
    )
    public ResponseEntity<byte[]> process(@ApiParam(value = "Image to be processed", required = true) @RequestPart("file") MultipartFile file,
                                          @ApiParam(name = "type", value = "Defines how the image is processed (for example gray-scale, blur... )") @RequestParam("type") ImageProcessorType type,
                                          @ApiParam (name = "strategy", value = "Defines multithreading strategy for image processing", required = true) @RequestParam("strategy") ParallelingStrategyType strategy,
                                          RedirectAttributes attributes) throws IOException, InterruptedException {
        validateFile(file, attributes);
        ByteArrayOutputStream resultImage = imageService.process(file, type, strategy);
        return ResponseEntity.ok().contentType(IMAGE_JPEG).body(resultImage.toByteArray());
    }

    @RolesAllowed({UserRoles.ROLE_ONLY_COMPARISON, UserRoles.ROLE_FULL})
    @ApiOperation(value = "To compare two images of the same size and get one of images where all differences are marked with frames")
    @PostMapping(value = "/compare",
            produces = IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> compare(@ApiParam(value = "First image to be compare", required = true)
                                          @RequestPart("file1") MultipartFile file1,
                                          @ApiParam(value = "Second image to be compare", required = true)
                                          @RequestPart("file2") MultipartFile file2,
                                          @ApiParam(value = "Color of the frame that emphasizes the differences in hex format", defaultValue = "#ff0000")
                                          @RequestParam(defaultValue = "#ff0000")
                                          @Valid @Pattern(regexp = HEX_PATTERN) String hexColorCode) throws IOException {
        Color color = Color.decode(hexColorCode);
        return ResponseEntity.ok().contentType(IMAGE_PNG).body(imageService.compare(file1, file2, color).toByteArray());
    }
}