package testingmachine_backend.process.Service;

import org.springframework.stereotype.Service;
import testingmachine_backend.config.JsonFileReader;
import testingmachine_backend.controller.JsonController;
import testingmachine_backend.process.DTO.ProcessMessageStatusDTO;
import testingmachine_backend.process.Messages.PopupMessage;
import testingmachine_backend.process.utils.ElementsFunctionUtils;
import testingmachine_backend.schedule.JsonPersentResult;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProcessMessageStatusService {

    private static final ThreadLocal<List<ProcessMessageStatusDTO>> processMessageStatusList = ThreadLocal.withInitial(ArrayList::new);

    public static void addProcessStatus(String fileName, String id, String code, String name, String status, String messageText, String TestProcessType, String jsonId) {

        ProcessMessageStatusDTO statusDTO = new ProcessMessageStatusDTO(fileName, id, code, name, status, messageText, jsonId,
                ElementsFunctionUtils.getProcessLogMessages(),
                ElementsFunctionUtils.getUniqueEmptyDataPath(),
                PopupMessage.getUniquePopupMessages(),
                ElementsFunctionUtils.getPopupStandartMessages(),
                ElementsFunctionUtils.getRequiredPathMessages(),
                ElementsFunctionUtils.getComboMessages()
        );
        processMessageStatusList.get().add(statusDTO);

        JsonFileReader.saveToSingleJsonFile(statusDTO, TestProcessType, jsonId); /* Тусдаа фолдер */
//        JsonPersentResult.saveToSingleJsonFile(jsonId, jsonId); /* Нэг фолдер */
        clearAllDTOField();
    }

    public static List<ProcessMessageStatusDTO> getProcessStatuses() {
        return new ArrayList<>(processMessageStatusList.get());
    }

    public static void clearAllDTOField(){
        processMessageStatusList.get().clear();
        ElementsFunctionUtils.ProcessLogFields.get().clear();
        ElementsFunctionUtils.emptyPathField.get().clear();
        PopupMessage.PopupMessageField.get().clear();
        ElementsFunctionUtils.PopupStandartField.get().clear();
        ElementsFunctionUtils.RequiredPathField.get().clear();
        ElementsFunctionUtils.ComboMessageField.get().clear();

    }
}
