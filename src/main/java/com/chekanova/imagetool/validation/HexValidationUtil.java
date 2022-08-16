package com.chekanova.imagetool.validation;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Class for hex validation
 *
 * @author Natanius18
 */
@UtilityClass
public class HexValidationUtil {
    private static final String INVALID_HEX = "hex.invalid";
    private static final String HEX_PATTERN = "^#([a-fA-F0-9]{6}|[a-fA-F0-9]{3})$";

    public static void validateHex(String colorCode, RedirectAttributes attributes) {
        if (!colorCode.matches(HEX_PATTERN)) {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", Locale.ENGLISH);
            String message = bundle.getString(INVALID_HEX);
            attributes.addFlashAttribute("message", message);
            throw new IllegalArgumentException(message);
        }
    }
}