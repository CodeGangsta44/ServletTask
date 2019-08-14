package ua.dovhopoliuk.controller.validation;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public enum CompositeValidator {
    SURNAME(new NotBlankValidator(),
            new RangeValidator(2, 50),
            new FormatValidator("pattern.surname")),
    NAME(new NotBlankValidator(),
            new RangeValidator(2, 30),
            new FormatValidator("pattern.name")),
    PATRONYMIC(new NotBlankValidator(),
            new RangeValidator(2, 40),
            new FormatValidator("pattern.patronymic")),
    LOGIN(new NotBlankValidator(),
            new RangeValidator(4, 20),
            new FormatValidator("pattern.login")),
    EMAIL(new NotBlankValidator(),
            new FormatValidator("pattern.email")),
    PASSWORD(new NotBlankValidator(),
            new RangeValidator(6, 20),
            new FormatValidator("pattern.password"));

    private List<Validator> validators;

    private CompositeValidator(Validator... validators) {
        this.validators = Arrays.asList(validators);
    }

    public Optional<List<String>> validate(HttpServletRequest request, String field) {
        List<String> messages = new ArrayList<>();

        for (Validator validator : validators) {
            try {
                validator.validate(request, field);
            } catch (Exception e) {
                messages.add(e.getLocalizedMessage());
            }
        }

        if (messages.size() > 0) {
            return Optional.of(messages);
        }

        return Optional.empty();
    }
}
