package testingmachine_backend.meta.Utils;

import testingmachine_backend.meta.DTO.ErrorTimeoutDTO;

import java.util.ArrayList;
import java.util.List;

public class ErrorLogger {
    private static final List<ErrorTimeoutDTO> errorTimeoutMessages = new ArrayList<>();

    public static void logError(String fileName, String id) {
        System.err.println("metaId: " + id + ", fileName: " + fileName);
        ErrorTimeoutDTO errorTimeoutMessage = new ErrorTimeoutDTO(fileName, id);
        errorTimeoutMessages.add(errorTimeoutMessage);
    }

    public static List<ErrorTimeoutDTO> getErrorTimeoutMessages() {
        return errorTimeoutMessages;
    }
}