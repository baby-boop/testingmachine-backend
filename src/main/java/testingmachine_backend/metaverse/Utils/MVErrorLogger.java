package testingmachine_backend.metaverse.Utils;

import testingmachine_backend.metaverse.DTO.MVErrorTimeoutDTO;

import java.util.ArrayList;
import java.util.List;

public class MVErrorLogger {

    private static final List<MVErrorTimeoutDTO> mvErrorTimeoutMessages = new ArrayList<>();

    public static void logError(String fileName, String id, String menuId) {
        System.err.println("metaId: " + id + ", fileName: " + fileName);
        MVErrorTimeoutDTO mvErrorTimeoutMessage = new MVErrorTimeoutDTO(fileName, id, menuId);
        mvErrorTimeoutMessages.add(mvErrorTimeoutMessage);
    }

    public static List<MVErrorTimeoutDTO> getMvErrorTimeoutMessages() {
        return mvErrorTimeoutMessages;
    }
}
