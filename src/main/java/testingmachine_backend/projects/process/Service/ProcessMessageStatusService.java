package testingmachine_backend.projects.process.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import testingmachine_backend.projects.indicator.IsIndicatorMessage;
import testingmachine_backend.projects.meta.Service.JsonFileReaderMeta;
import testingmachine_backend.projects.process.DTO.ProcessMessageStatusDTO;
import testingmachine_backend.projects.process.Messages.PopupMessage;
import testingmachine_backend.projects.process.utils.ElementsFunctionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ProcessMessageStatusService {

    private static final ConcurrentHashMap<String, Integer> errorCountMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, List<ProcessMessageStatusDTO>> processMessageStatusMap = new ConcurrentHashMap<>();

    public static void addProcessStatus(String fileName, String id, String code, String name, String status, String messageText, String TestProcessType, String jsonId) {

        ProcessMessageStatusDTO statusDTO = new ProcessMessageStatusDTO(fileName, id, code, name, status, messageText, jsonId,
                ElementsFunctionUtils.getProcessLogMessages()
                        .stream().filter(detail -> detail.getMetaDataId().equals(id)).collect(Collectors.toList()),
                ElementsFunctionUtils.getUniqueEmptyDataPath()
                        .stream().filter(detail -> detail.getMetaDataId().equals(id)).collect(Collectors.toList()),
                PopupMessage.getUniquePopupMessages()
                        .stream().filter(detail -> detail.getMetaDataId().equals(id)).collect(Collectors.toList()),
                ElementsFunctionUtils.getPopupStandartMessages()
                        .stream().filter(detail -> detail.getMetaDataId().equals(id)).collect(Collectors.toList()),
                ElementsFunctionUtils.getRequiredPathMessages()
                        .stream().filter(detail -> detail.getMetaDataId().equals(id)).collect(Collectors.toList()),
                ElementsFunctionUtils.getComboMessages()
                        .stream().filter(detail -> detail.getMetaDataId().equals(id)).collect(Collectors.toList()),
                IsIndicatorMessage.getIndicatorTabMesssage()
                        .stream().filter(detail -> detail.getParentId().equals(id)).collect(Collectors.toList())
        );

//        ObjectMapper objectMapper = new ObjectMapper();
//
//        try {
//            String json = objectMapper.writeValueAsString(statusDTO);
//            System.out.println(json);
//        } catch (JsonProcessingException e) {
//
//        }

        processMessageStatusMap.computeIfAbsent(jsonId, k -> new ArrayList<>()).add(statusDTO);
        errorCountMap.put(jsonId, errorCountMap.getOrDefault(jsonId, 0) + 1);

//        String customerName = "Test patch";
//        int totalCount = 11;
//
//        saveToJson(jsonId, totalCount, TestProcessType, customerName);

    }

    public static void saveToJson(String jsonId, int totalCount, String type, String customerName) {
        Map<String, Object> jsonOutput = readExistingJson(jsonId, type);

        // Шинэ мэдээлэл нэмэх
        List<ProcessMessageStatusDTO> existingDetails = (List<ProcessMessageStatusDTO>) jsonOutput.getOrDefault("details", new ArrayList<>());
        existingDetails.addAll(processMessageStatusMap.getOrDefault(jsonId, new ArrayList<>()));

        jsonOutput.put("jsonId", jsonId);
        jsonOutput.put("customerName", customerName);
        jsonOutput.put("totalCount", totalCount);
        jsonOutput.put("errorCount", errorCountMap.getOrDefault(jsonId, 0));
        jsonOutput.put("processDetails", existingDetails);

        // Файл руу хадгалах
        JsonFileReaderMeta.saveJsonToFile(jsonId, type, jsonOutput);

    }

    private static Map<String, Object> readExistingJson(String jsonId, String type) {
        return JsonFileReaderMeta.readJsonFromFile(jsonId, type);
    }

    public static List<ProcessMessageStatusDTO> getProcessStatuses(String jsonId) {
        return processMessageStatusMap.getOrDefault(jsonId, new ArrayList<>());
    }

    public static void clearAllDTOField(String jsonId) {
        processMessageStatusMap.remove(jsonId);
        errorCountMap.remove(jsonId);

    }
}
