package ua.dovhopoliuk.controller.command;

import javax.servlet.http.HttpServletRequest;

public class LogoutCommand implements Command {

    @Override
    public String execute(HttpServletRequest request) {
        CommandSessionUtility.logoutFromSession(request);
        return "redirect:/login?logout";
    }
}
