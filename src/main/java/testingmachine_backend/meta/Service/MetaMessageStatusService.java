package testingmachine_backend.meta.Service;

import testingmachine_backend.controller.JsonController;
import testingmachine_backend.meta.DTO.ErrorMessageDTO;

import java.util.ArrayList;
import java.util.List;

public class MetaMessageStatusService {

    private static final List<ErrorMessageDTO> metaMessageStatusList = new ArrayList<>();

    public static void addMetaStatus(String moduleName, String id, String code, String name, String type, String messageText, String jsonId) {
        ErrorMessageDTO statusDTO = new ErrorMessageDTO(moduleName, id, code, name, type, messageText, jsonId);
        metaMessageStatusList.add(statusDTO);
        JsonFileReaderMeta.saveToSingleJsonFile(statusDTO, jsonId);
        metaMessageStatusList.clear();
    }

    public static List<ErrorMessageDTO> getMetaStatuses() {
        return new ArrayList<>(metaMessageStatusList);
    }
}
