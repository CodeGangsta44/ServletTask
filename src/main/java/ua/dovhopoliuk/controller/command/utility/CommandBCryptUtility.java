package ua.dovhopoliuk.controller.command.utility;

import org.mindrot.jbcrypt.BCrypt;

public class CommandBCryptUtility {

    public static String encodePassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean isPasswordMatches(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
