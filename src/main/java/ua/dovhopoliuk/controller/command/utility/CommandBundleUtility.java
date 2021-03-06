package ua.dovhopoliuk.controller.command.utility;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;
import java.util.ResourceBundle;

public class CommandBundleUtility {
    public static String getMessage(HttpServletRequest request, String baseName, String key) {
        System.out.println("Lang in bundle: " + (String)request.getSession().getAttribute("lang"));
        Locale locale = new Locale((String)request.getSession().getAttribute("lang"));
        return ResourceBundle.getBundle(baseName, locale).getString(key);
    }
}
