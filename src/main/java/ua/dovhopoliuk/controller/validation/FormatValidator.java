package ua.dovhopoliuk.controller.validation;

import ua.dovhopoliuk.controller.command.utility.CommandBundleUtility;
import ua.dovhopoliuk.model.exception.FormatException;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Pattern;

public class FormatValidator implements Validator {
    private String regularExpressionKey;

    public FormatValidator(String regularExpressionKey) {
        this.regularExpressionKey = regularExpressionKey;
    }

    @Override
    public void validate(HttpServletRequest request, String field) throws Exception {
        String regularExpression = CommandBundleUtility.getMessage(request, "regular_expressions", regularExpressionKey);

        if (!Pattern.matches(regularExpression, field)) {
            String localizedMessage = CommandBundleUtility.getMessage(request, "messages", "exception.validation.format");
            throw new FormatException("Field does not match format", localizedMessage);
        }
    }
}
