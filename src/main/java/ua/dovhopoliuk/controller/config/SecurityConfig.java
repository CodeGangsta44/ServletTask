package ua.dovhopoliuk.controller.config;

import ua.dovhopoliuk.model.entity.Role;

import java.util.*;

public class SecurityConfig {
    private static final Map<String, Map<Role, Set<String>>> mapConfig = new HashMap<>();

    static {
        init();
    }

    private static void init() {

        Set<String> userGetUrlPatterns = new HashSet<>();
        userGetUrlPatterns.add("/app/home");
        userGetUrlPatterns.add("api/users/me");

        Set<String> adminGetUrlPatterns = new HashSet<>();
        adminGetUrlPatterns.add("/api/users");

        Map<Role, Set<String>> getUrlPatterns = new HashMap<>();

        getUrlPatterns.put(Role.USER, userGetUrlPatterns);
        getUrlPatterns.put(Role.ADMIN, adminGetUrlPatterns);

        mapConfig.put("GET", getUrlPatterns);

        Set<String> userPostUrlPatterns = new HashSet<>();
        userPostUrlPatterns.add("/app/logout");

        Map<Role, Set<String>> postUrlPatterns = new HashMap<>();
        postUrlPatterns.put(Role.USER, userPostUrlPatterns);

        mapConfig.put("POST", postUrlPatterns);

    }


    public static Map<Role, Set<String>> getUrlPatternsForMethod(String method) {
        return mapConfig.get(method);
    }

    public static boolean isUrlSecured(String url, String method) {

        return getUrlPatternsForMethod(method).values()
                .stream()
                .flatMap(Set::stream)
                .anyMatch(pattern -> pattern.equals(url));

    }

    public static boolean isAccessAllowed(String url, String method, Set<Role> roles) {

        return getUrlPatternsForMethod(method).entrySet()
                .stream()
                .filter(entry -> entry.getValue().contains(url))
                .map(Map.Entry::getKey)
                .anyMatch(roles::contains);
    }
}
