package ua.dovhopoliuk.controller.validation;

import ua.dovhopoliuk.controller.command.utility.CommandBundleUtility;
import ua.dovhopoliuk.model.exception.RangeException;

import javax.servlet.http.HttpServletRequest;
import java.text.MessageFormat;
import java.util.Arrays;

public class RangeValidator implements Validator {
    private int minLength;
    private int maxLength;

    public RangeValidator(int minLength, int maxLength) {
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    @Override
    public void validate(HttpServletRequest request, String field) throws Exception {
        if (field.length() < minLength || field.length() > maxLength) {
            String localizedMessage = CommandBundleUtility.getMessage(request, "messages", "exception.validation.field.range");
            String formattedLocalizedMessage = MessageFormat.format(localizedMessage, minLength, maxLength);

            throw new RangeException("Field is out of ranges", formattedLocalizedMessage);
        }
    }
}
