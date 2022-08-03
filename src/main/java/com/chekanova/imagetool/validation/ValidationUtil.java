package com.chekanova.imagetool.validation;

import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

@UtilityClass
public class ValidationUtil {
    private static final String IMAGE_JPEG = "image/jpeg";
    private static final String ONLY_IMAGE = "file.only.image";
    private static final String NOT_EMPTY = "file.not.empty";
    private static final String INVALID_HEX = "hex.invalid";
    private static final String HEX_PATTERN = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$";
    private static final Pattern pattern = Pattern.compile(HEX_PATTERN);

    public static void validateFile(MultipartFile file,
                                    RedirectAttributes attributes) {
        validateNotEmpty(file, attributes);
        validateOnlyImage(file, attributes);
    }

    private static void validateNotEmpty(MultipartFile file, RedirectAttributes attributes) {
        if (file.isEmpty()) {
            String message = getMessage(NOT_EMPTY);
            attributes.addFlashAttribute("message", message);
            throw new IllegalArgumentException(message);
        }
    }

    private static void validateOnlyImage(MultipartFile file, RedirectAttributes attributes) {
        if (!(IMAGE_JPEG.equals(file.getContentType()))) {
            String message = getMessage(ONLY_IMAGE);
            attributes.addFlashAttribute("message", message);
            throw new IllegalArgumentException(message);
        }
    }

    private static String getMessage(String key) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.ENGLISH);
        return bundle.getString(key);
    }

    public static void validateHex(String colorCode, RedirectAttributes attributes) {
        if (!pattern.matcher(colorCode).matches()){
            ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.ENGLISH);
            String message = bundle.getString(INVALID_HEX);
            attributes.addFlashAttribute("message", message);
            throw new IllegalArgumentException(message);
        }
    }
}
