package ua.dovhopoliuk.controller.command;

import ua.dovhopoliuk.controller.command.utility.CommandBCryptUtility;
import ua.dovhopoliuk.controller.command.utility.CommandBundleUtility;
import ua.dovhopoliuk.controller.command.utility.CommandJsonUtility;
import ua.dovhopoliuk.model.dto.RegNoteDTO;
import ua.dovhopoliuk.model.entity.Role;
import ua.dovhopoliuk.model.entity.User;
import ua.dovhopoliuk.model.exception.LoginNotUniqueException;
import ua.dovhopoliuk.model.exception.RequestException;
import ua.dovhopoliuk.model.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class RegisterUserCommand implements Command {
    private CommandJsonUtility<RegNoteDTO> regNoteDTOCommandJsonUtility = new CommandJsonUtility<>(RegNoteDTO.class);
    private final UserService userService = new UserService();

    @Override
    public String execute(HttpServletRequest request) {

        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader bufferedReader = request.getReader()) {
            String line;
            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        RegNoteDTO regNoteDTO = regNoteDTOCommandJsonUtility.fromJson(stringBuilder.toString());

        try {
            userService.registerUser(createUserFromRegNoteDTO(regNoteDTO));
        } catch (LoginNotUniqueException e) {
            regNoteDTO.setLogin("");

            e.setLocalizedMessage(CommandBundleUtility
                    .getMessage(request, "messages", "exception.login.not.unique"));

            e.setNote(regNoteDTO);

            String message = new CommandJsonUtility<>(LoginNotUniqueException.class).toJson(e);
            throw new RequestException(message);
        }


        return  CommandBundleUtility
                .getMessage(request, "message", "registration.success");
    }

    private User createUserFromRegNoteDTO(RegNoteDTO regNoteDTO){
        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        roles.add(Role.SPEAKER);

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
}
