package ua.dovhopoliuk.controller.command.page.pages;

import ua.dovhopoliuk.controller.command.Command;

import javax.servlet.http.HttpServletRequest;

public class LogoutPageCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/login?logout";
    }
}
