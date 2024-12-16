package testingmachine_backend.metaWithProcess.Controller;

import testingmachine_backend.meta.DTO.ErrorMessageDTO;

import java.util.ArrayList;
import java.util.List;

public class MetaWithProcessStatusService {

    private static final List<ErrorMessageDTO> metaMessageStatusList1 = new ArrayList<>();

    public static void addMetaStatus(String moduleName, String id, String code, String name, String type, String messageText) {
        ErrorMessageDTO statusDTO = new ErrorMessageDTO(moduleName, id, code, name, type, messageText);
        metaMessageStatusList1.add(statusDTO);
        System.out.println("hi 1");
        JsonFileReaderMetaWithProcess.saveToSingleJsonFile(statusDTO);

        metaMessageStatusList1.clear();
    }

    public static List<ErrorMessageDTO> getMetaStatuses() {
        return new ArrayList<>(metaMessageStatusList1);
    }
}
