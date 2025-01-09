package testingmachine_backend.meta.Service;

import testingmachine_backend.controller.JsonController;
import testingmachine_backend.meta.DTO.ErrorMessageDTO;

import java.util.ArrayList;
import java.util.List;

public class MetaMessageStatusService {

    private static final ThreadLocal<List<ErrorMessageDTO>> metaMessageStatusList = ThreadLocal.withInitial(ArrayList::new);

    public static void addMetaStatus(String moduleName, String id, String code, String name, String type, String messageText, String jsonId, String checkerType) {
        ErrorMessageDTO statusDTO = new ErrorMessageDTO(moduleName, id, code, name, type, messageText, jsonId);
        metaMessageStatusList.get().add(statusDTO);
        JsonFileReaderMeta.saveToSingleJsonFile(statusDTO, jsonId, checkerType);
        metaMessageStatusList.get().clear();
    }

    public static List<ErrorMessageDTO> getMetaStatuses() {
        return new ArrayList<>(metaMessageStatusList.get());
    }
}
