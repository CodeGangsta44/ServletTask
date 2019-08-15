package ua.dovhopoliuk.controller.command.page.pages;

import ua.dovhopoliuk.controller.command.Command;

import javax.servlet.http.HttpServletRequest;

public class FragmentPageCommand implements Command {
    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        path = path.replaceFirst(".*/app/fragments/", "");
        String[] parameters = path.split("/");
        return "/fragments/" + parameters[0] + "/" + parameters[1] + ".jsp";
    }
}
