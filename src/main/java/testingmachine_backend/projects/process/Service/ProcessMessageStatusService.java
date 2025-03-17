package testingmachine_backend.projects.process.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import testingmachine_backend.projects.indicator.IsIndicatorMessage;
import testingmachine_backend.projects.meta.DTO.ErrorMessageDTO;
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
    private static final ConcurrentHashMap<String, Integer> totalCountMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, List<ProcessMessageStatusDTO>> processMessageStatusMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, List<ErrorMessageDTO>> metaMessageStatusMap = new ConcurrentHashMap<>();

    /**
     * Процесс мессеж
     * */
    public static void addProcessStatus(String fileName, String id, String code, String name, String status, String messageText, String TestProcessType, String jsonId, int totalCount) {

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

        errorCountMap.put(jsonId, errorCountMap.getOrDefault(jsonId, 0) + ("error".equalsIgnoreCase(status) ? 1 : 0));
        totalCountMap.put(jsonId, totalCount);
        processMessageStatusMap.computeIfAbsent(jsonId, k -> new ArrayList<>()).add(statusDTO);

    }

    /**
     * Мета мессеж
     */
    public static void addMetaStatus(String moduleName, String id, String code, String name, String type, String messageText, String jsonId, String checkerType, int totalCount, String customerName) {
        ErrorMessageDTO statusDTO = new ErrorMessageDTO(moduleName, id, code, name, type, messageText, jsonId);

        errorCountMap.put(jsonId, errorCountMap.getOrDefault(jsonId, 0) + ("error".equalsIgnoreCase(type) ? 1 : 0));
        totalCountMap.put(jsonId, totalCount);

        metaMessageStatusMap.computeIfAbsent(jsonId, k -> new ArrayList<>()).add(statusDTO);

    }

    public static void saveToJson(String jsonId, String type, String customerName, int statusMessage, String fullUrl) {
        Map<String, Object> jsonOutput = readExistingJson(jsonId, type);

        // Шинэ мэдээлэл нэмэх
        List<ProcessMessageStatusDTO> processDetails = (List<ProcessMessageStatusDTO>) jsonOutput.getOrDefault("details", new ArrayList<>());
        processDetails.addAll(processMessageStatusMap.getOrDefault(jsonId, new ArrayList<>()));

        List<ErrorMessageDTO> metaDetails = (List<ErrorMessageDTO>) jsonOutput.getOrDefault("details", new ArrayList<>());
        metaDetails.addAll(metaMessageStatusMap.getOrDefault(jsonId, new ArrayList<>()));

        jsonOutput.put("jsonId", jsonId);
        jsonOutput.put("customerName", customerName);
        jsonOutput.put("totalCount", totalCountMap.getOrDefault(jsonId, 0));
        jsonOutput.put("errorCount", errorCountMap.getOrDefault(jsonId, 0));
        jsonOutput.put("statusMessage", statusMessage);
        jsonOutput.put("fullUrl", fullUrl);
        jsonOutput.put("processDetails", processDetails);
        jsonOutput.put("metaDetails", metaDetails);

        // Файл руу хадгалах
        JsonFileReaderMeta.saveJsonToFile(jsonId, type, jsonOutput);

    }

    private static Map<String, Object> readExistingJson(String jsonId, String type) {
        return JsonFileReaderMeta.readJsonFromFile(jsonId, type);
    }

    public static List<ProcessMessageStatusDTO> getProcessStatuses(String jsonId) {
        return processMessageStatusMap.getOrDefault(jsonId, new ArrayList<>());
    }

    public static List<ErrorMessageDTO> getMetaStatuses(String jsonId) {
        return metaMessageStatusMap.getOrDefault(jsonId, new ArrayList<>());
    }

    public static void clearAllDTOField(String jsonId) {
        processMessageStatusMap.remove(jsonId);
        metaMessageStatusMap.remove(jsonId);
        errorCountMap.remove(jsonId);
        totalCountMap.remove(jsonId);
    }
}
