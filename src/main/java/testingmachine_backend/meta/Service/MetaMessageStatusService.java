package testingmachine_backend.meta.Service;

import testingmachine_backend.meta.DTO.ErrorMessageDTO;

import java.util.ArrayList;
import java.util.List;

public class MetaMessageStatusService {

    private static final List<ErrorMessageDTO> metaMessageStatusList = new ArrayList<>();

    public static void addMetaStatus(String moduleName, String id, String code, String name, String type, String messageText) {
        ErrorMessageDTO statusDTO = new ErrorMessageDTO(moduleName, id, code, name, type, messageText);
        metaMessageStatusList.add(statusDTO);
        JsonFileReaderMeta.saveToSingleJsonFile(statusDTO);
    }

    public static List<ErrorMessageDTO> getMetaStatuses() {
        return new ArrayList<>(metaMessageStatusList);
    }
}
