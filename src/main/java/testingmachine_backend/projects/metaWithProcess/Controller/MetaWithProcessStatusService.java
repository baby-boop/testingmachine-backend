package testingmachine_backend.projects.metaWithProcess.Controller;

import testingmachine_backend.config.JsonFileReader;
import testingmachine_backend.projects.meta.DTO.ErrorMessageDTO;

import java.util.ArrayList;
import java.util.List;

public class MetaWithProcessStatusService {

    private static final List<ErrorMessageDTO> metaMessageStatusList1 = new ArrayList<>();

    public static void addMetaStatus(String moduleName, String id, String code, String name, String type, String messageText, String jsonId) {
        ErrorMessageDTO statusDTO = new ErrorMessageDTO(moduleName, id, code, name, type, messageText, jsonId);
        metaMessageStatusList1.add(statusDTO);

        JsonFileReader.saveToSingleJsonFile(statusDTO, type, jsonId); /* Тусдаа фолдер */
//        JsonPersentResult.saveToSingleJsonFile(jsonId, jsonId); /* Нэг фолдер */

        metaMessageStatusList1.clear();
    }

    public static List<ErrorMessageDTO> getMetaStatuses() {
        return new ArrayList<>(metaMessageStatusList1);
    }
}
