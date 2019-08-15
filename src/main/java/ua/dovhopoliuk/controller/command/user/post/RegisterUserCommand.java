package ua.dovhopoliuk.controller.command.user.post;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.utility.CommandBCryptUtility;
import ua.dovhopoliuk.controller.command.utility.CommandBundleUtility;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.controller.command.utility.CommandRequestBodyReaderUtility;
import ua.dovhopoliuk.controller.validation.CompositeValidator;
import ua.dovhopoliuk.controller.validation.Status;
import ua.dovhopoliuk.model.dto.RegNoteDTO;
import ua.dovhopoliuk.model.entity.Role;
import ua.dovhopoliuk.model.entity.User;
import ua.dovhopoliuk.model.exception.LoginNotUniqueException;
import ua.dovhopoliuk.model.exception.RequestException;
import ua.dovhopoliuk.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.*;

public class RegisterUserCommand implements Command {
    private CommandJsonUtility<RegNoteDTO> regNoteDTOCommandJsonUtility =
            new CommandJsonUtility<>(RegNoteDTO.class);

    private UserService userService;

    public RegisterUserCommand(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String execute(HttpServletRequest request) {

        RegNoteDTO regNoteDTO = regNoteDTOCommandJsonUtility
                .fromJson(CommandRequestBodyReaderUtility
                        .readRequestBody(request));

        regNoteDTO.setStatus(Status.VALID);

        validateRegistrationNote(request, regNoteDTO);

        if (regNoteDTO.getStatus() == Status.INVALID) {
            String localizedMessage = CommandBundleUtility
                    .getMessage(request, "messages", "exception.validation.message");
            regNoteDTO.setLocalizedMessage(localizedMessage);
            String message = regNoteDTOCommandJsonUtility.toJson(regNoteDTO);
            throw new RequestException(message);
        }

        try {
            userService.registerUser(createUserFromRegNoteDTO(regNoteDTO));
        } catch (LoginNotUniqueException e) {
            regNoteDTO.setLogin("");

            String localizedMessage = CommandBundleUtility
                    .getMessage(request, "messages", "exception.login.not.unique");

            String validationMessage = CommandBundleUtility
                    .getMessage(request, "messages", "exception.validation.login.not.unique");

            regNoteDTO.setLocalizedMessage(localizedMessage);
            regNoteDTO.getValidationMessages().put("login", Collections.singletonList(validationMessage));

            String message = regNoteDTOCommandJsonUtility.toJson(regNoteDTO);

            throw new RequestException(message);
        }

        return  new CommandJsonUtility<>(String.class).toJson( CommandBundleUtility
                .getMessage(request, "messages", "registration.success"));
    }

    private User createUserFromRegNoteDTO(RegNoteDTO regNoteDTO){
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        if (Boolean.parseBoolean(regNoteDTO.getIsSpeaker())) {
            roles.add(Role.SPEAKER);
        }


        User user = new User();
        user.setSurname(regNoteDTO.getSurname());
        user.setName(regNoteDTO.getName());
        user.setPatronymic(regNoteDTO.getPatronymic());
        user.setLogin(regNoteDTO.getLogin());
        user.setEmail(regNoteDTO.getEmail());
        user.setPassword(CommandBCryptUtility.encodePassword(regNoteDTO.getPassword()));
        user.setRoles(roles);

        return user;
    }

    private void validateRegistrationNote(HttpServletRequest request, RegNoteDTO regNoteDTO) {
        Map<String, List<String>> validationMessages = new HashMap<>();

        CompositeValidator.SURNAME.validate(request, regNoteDTO.getSurname()).ifPresent(messages -> {
            validationMessages.put("surname", messages);
            regNoteDTO.setStatus(Status.INVALID);
        });

        CompositeValidator.NAME.validate(request, regNoteDTO.getName()).ifPresent(messages -> {
            validationMessages.put("name", messages);
            regNoteDTO.setStatus(Status.INVALID);
        });

        CompositeValidator.PATRONYMIC.validate(request, regNoteDTO.getPatronymic()).ifPresent(messages -> {
            validationMessages.put("patronymic", messages);
            regNoteDTO.setStatus(Status.INVALID);
        });

        CompositeValidator.LOGIN.validate(request, regNoteDTO.getLogin()).ifPresent(messages -> {
            validationMessages.put("login", messages);
            regNoteDTO.setStatus(Status.INVALID);
        });

        CompositeValidator.EMAIL.validate(request, regNoteDTO.getEmail()).ifPresent(messages -> {
            validationMessages.put("email", messages);
            regNoteDTO.setStatus(Status.INVALID);
        });

        CompositeValidator.PASSWORD.validate(request, regNoteDTO.getPassword()).ifPresent(messages -> {
            validationMessages.put("password", messages);
            regNoteDTO.setStatus(Status.INVALID);
        });

        regNoteDTO.setValidationMessages(validationMessages);
    }
}
