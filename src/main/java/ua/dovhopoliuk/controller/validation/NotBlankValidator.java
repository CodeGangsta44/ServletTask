package ua.dovhopoliuk.controller.validation;

import ua.dovhopoliuk.controller.command.utility.CommandBundleUtility;
import ua.dovhopoliuk.model.exception.FieldIsBlankException;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

public class NotBlankValidator implements Validator {
    @Override
    public void validate(HttpServletRequest request, String field) throws FieldIsBlankException{
        if (Objects.isNull(field) || field.equals("")) {
            String localizedMessage = CommandBundleUtility.getMessage(request, "messages", "exception.validation.field.is.blank");
            throw new FieldIsBlankException("Field is blank", localizedMessage);
        }
    }
}
