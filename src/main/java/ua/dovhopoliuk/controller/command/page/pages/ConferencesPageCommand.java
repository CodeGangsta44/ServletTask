package ua.dovhopoliuk.controller.command.page.pages;

import ua.dovhopoliuk.controller.command.Command;

import javax.servlet.http.HttpServletRequest;

public class ConferencesPageCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        return "/conferences.jsp";
    }
}
