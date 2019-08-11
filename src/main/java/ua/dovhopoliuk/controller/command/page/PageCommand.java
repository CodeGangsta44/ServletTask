package ua.dovhopoliuk.controller.command.page;

import ua.dovhopoliuk.controller.command.Command;
import ua.dovhopoliuk.controller.command.page.pages.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

public class PageCommand implements Command {
    private Map<String, Command> pages;

    public PageCommand() {
        pages = new HashMap<>();

        pages.put("conferences", new ConferencesPageCommand());
        pages.put("fragments", new FragmentPageCommand());
        pages.put("login", new LoginPageCommand());
        pages.put("", new MainPageCommand());
        pages.put("notifications", new NotificationsPageCommand());
        pages.put("registration", new RegistrationPageCommand());
        pages.put("reportRequests", new ReportRequestsPageCommand());
        pages.put("reports", new ReportsPageCommand());
        pages.put("users", new UsersPageCommand());
    }

    @Override
    public String execute(HttpServletRequest request) {
        String path = request.getRequestURI();
        path = path.replaceFirst(".*/app/" , "")
                .replaceFirst("/.*", "");

        Command command = pages.getOrDefault(path ,
                (r) -> "/main.jsp");

        return command.execute(request);
    }
}
