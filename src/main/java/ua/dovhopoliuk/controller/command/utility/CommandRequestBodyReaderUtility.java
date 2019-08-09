package ua.dovhopoliuk.controller.command.utility;

import ua.dovhopoliuk.controller.command.Command;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;

public class CommandRequestBodyReaderUtility {
    public static String readRequestBody(HttpServletRequest request) {
        StringBuilder stringBuilder = new StringBuilder();

        try (BufferedReader bufferedReader = request.getReader()) {
            String line;
            while((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
}
