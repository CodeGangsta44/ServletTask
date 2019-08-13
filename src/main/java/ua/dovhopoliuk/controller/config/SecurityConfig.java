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
        userGetUrlPatterns.add("/api/conferences");
        userGetUrlPatterns.add("/api/conferences/me");
        userGetUrlPatterns.add("/api/conferences/id");
        userGetUrlPatterns.add("/api/conferences/totalNumber");
        userGetUrlPatterns.add("/api/conferences/id/changeRegistration");
        userGetUrlPatterns.add("/api/notifications/me");
        userGetUrlPatterns.add("/api/users/me");
        userGetUrlPatterns.add("/api/user/speakerStatistics/id");
        userGetUrlPatterns.add("/api/votes/id");
        userGetUrlPatterns.add("/api/votes/id/me");
        userGetUrlPatterns.add("/conferences");
        userGetUrlPatterns.add("/notifications");
        userGetUrlPatterns.add("/users");

        Set<String> speakerGetUrlPatterns = new HashSet<>();
        speakerGetUrlPatterns.add("/api/reportRequests/me");
        speakerGetUrlPatterns.add("/reportRequests");
        speakerGetUrlPatterns.add("/reports");

        Set<String> moderGetUrlPatterns = new HashSet<>();
        moderGetUrlPatterns.add("/api/conferences/requests");
        moderGetUrlPatterns.add("/api/conferences/finished");
        moderGetUrlPatterns.add("/api/conferences/id/registeredGuests");
        moderGetUrlPatterns.add("/api/notifications");
        moderGetUrlPatterns.add("/api/reports");
        moderGetUrlPatterns.add("/api/reportRequests");

        Set<String> adminGetUrlPatterns = new HashSet<>();
        adminGetUrlPatterns.add("/api/users");

        Map<Role, Set<String>> getUrlPatterns = new HashMap<>();

        getUrlPatterns.put(Role.USER, userGetUrlPatterns);
        getUrlPatterns.put(Role.SPEAKER, speakerGetUrlPatterns);
        getUrlPatterns.put(Role.MODER, moderGetUrlPatterns);
        getUrlPatterns.put(Role.ADMIN, adminGetUrlPatterns);

        mapConfig.put("GET", getUrlPatterns);



        Set<String> userPostUrlPatterns = new HashSet<>();
        userPostUrlPatterns.add("/api/conferences");
        userPostUrlPatterns.add("/api/votes/id");

        Set<String> speakerPostUrlPatterns = new HashSet<>();
        speakerPostUrlPatterns.add("/api/reportRequests/request/id");

        Set<String> moderPostUrlPatterns = new HashSet<>();
        moderPostUrlPatterns.add("/api/conferences/id/processRequest");
        moderPostUrlPatterns.add("/api/reportRequests/id");

        Map<Role, Set<String>> postUrlPatterns = new HashMap<>();
        postUrlPatterns.put(Role.USER, userPostUrlPatterns);
        postUrlPatterns.put(Role.SPEAKER, speakerPostUrlPatterns);
        postUrlPatterns.put(Role.MODER, moderPostUrlPatterns);

        mapConfig.put("POST", postUrlPatterns);



        Set<String> userPutUrlPatterns = new HashSet<>();
        userPutUrlPatterns.add("/api/users");

        Set<String> moderPutUrlPatterns = new HashSet<>();
        moderPutUrlPatterns.add("/api/conferences/id");
        moderPutUrlPatterns.add("/api/conferences/id/finish");
        moderPutUrlPatterns.add("/api/reports");

        Map<Role, Set<String>> putUrlPatterns = new HashMap<>();
        putUrlPatterns.put(Role.USER, userPutUrlPatterns);
        putUrlPatterns.put(Role.MODER, moderPutUrlPatterns);

        mapConfig.put("PUT", putUrlPatterns);



        Set<String> userDeleteUrlPatterns = new HashSet<>();
        userDeleteUrlPatterns.add("/api/notifications/id");

        Set<String> moderDeleteUrlPatterns = new HashSet<>();
        moderDeleteUrlPatterns.add("/api/reports/id");

        Set<String> adminDeleteUrlPatterns = new HashSet<>();
        adminDeleteUrlPatterns.add("/api/users/id");

        Map<Role, Set<String>> deleteUrlPatterns = new HashMap<>();
        deleteUrlPatterns.put(Role.USER, userDeleteUrlPatterns);
        deleteUrlPatterns.put(Role.MODER, moderDeleteUrlPatterns);
        deleteUrlPatterns.put(Role.ADMIN, adminDeleteUrlPatterns);

        mapConfig.put("DELETE", deleteUrlPatterns);
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
