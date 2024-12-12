package testingmachine_backend.process.Service;

import org.springframework.stereotype.Service;
import testingmachine_backend.config.JsonFileReader;
import testingmachine_backend.controller.JsonController;
import testingmachine_backend.process.DTO.ProcessMessageStatusDTO;
import testingmachine_backend.process.Messages.PopupMessage;
import testingmachine_backend.process.utils.ElementsFunctionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProcessMessageStatusService {

    private static final List<ProcessMessageStatusDTO> processMessageStatusList = new ArrayList<>();

    public static void addProcessStatus(String fileName, String id, String code, String name, String status, String messageText, String TestProcessType) {

        ProcessMessageStatusDTO statusDTO = new ProcessMessageStatusDTO(fileName, id, code, name, status, messageText, JsonController.getJsonId(),
                ElementsFunctionUtils.getProcessLogMessages(),
                ElementsFunctionUtils.getUniqueEmptyDataPath(),
                PopupMessage.getUniquePopupMessages(),
                ElementsFunctionUtils.getPopupStandartMessages(),
                ElementsFunctionUtils.getRequiredPathMessages());
        processMessageStatusList.add(statusDTO);

        JsonFileReader.saveToSingleJsonFile(statusDTO, TestProcessType);
        clearAllDTOField();
    }

    public static List<ProcessMessageStatusDTO> getProcessStatuses() {
        return new ArrayList<>(processMessageStatusList);
    }

    public static void clearAllDTOField(){
        processMessageStatusList.clear();
        ElementsFunctionUtils.ProcessLogFields.clear();
        ElementsFunctionUtils.emptyPathField.clear();
        PopupMessage.PopupMessageField.clear();
        ElementsFunctionUtils.PopupStandartField.clear();
        ElementsFunctionUtils.RequiredPathField.clear();
    }
}
