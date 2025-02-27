package testingmachine_backend.projects.meta.Service;

import testingmachine_backend.projects.meta.DTO.ErrorMessageDTO;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class MetaMessageStatusService {

    private static final ConcurrentHashMap<String, Integer> errorCountMap = new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String, List<ErrorMessageDTO>> metaMessageStatusMap = new ConcurrentHashMap<>();
    /**
     * Алдааны статус нэмэх функц
     */
    public static void addMetaStatus(String moduleName, String id, String code, String name, String type, String messageText, String jsonId, String checkerType, int totalCount, String customerName) {
        ErrorMessageDTO statusDTO = new ErrorMessageDTO(moduleName, id, code, name, type, messageText, jsonId);

        errorCountMap.put(jsonId, errorCountMap.getOrDefault(jsonId, 0) + ("error".equalsIgnoreCase(type) ? 1 : 0));

        metaMessageStatusMap.computeIfAbsent(jsonId, k -> new ArrayList<>()).add(statusDTO);

//        saveToJson(jsonId, totalCount, checkerType, customerName);
    }

    /**
     * JSON хадгалах функц (хуучин өгөгдлийг хадгалах байдлаар)
     */
    public static void saveToJson(String jsonId, int totalCount, String type, String customerName) {
        Map<String, Object> jsonOutput = readExistingJson(jsonId, type);

        // Шинэ мэдээлэл нэмэх
        List<ErrorMessageDTO> existingDetails = (List<ErrorMessageDTO>) jsonOutput.getOrDefault("details", new ArrayList<>());
        existingDetails.addAll(metaMessageStatusMap.getOrDefault(jsonId, new ArrayList<>()));

        jsonOutput.put("jsonId", jsonId);
        jsonOutput.put("customerName", customerName);
        jsonOutput.put("totalCount", totalCount);
        jsonOutput.put("errorCount", errorCountMap.getOrDefault(jsonId, 0));
        jsonOutput.put("metaDetails", existingDetails);

        // Файл руу хадгалах
        JsonFileReaderMeta.saveJsonToFile(jsonId, type, jsonOutput);

    }

    /**
     * Хэрэв өмнө нь JSON файл үүссэн байвал түүнийг уншина
     */
    public static Map<String, Object> readExistingJson(String jsonId, String type) {
        return JsonFileReaderMeta.readJsonFromFile(jsonId, type);
    }

    public static List<ErrorMessageDTO> getMetaStatuses(String jsonId) {
        return metaMessageStatusMap.getOrDefault(jsonId, new ArrayList<>());
    }

    public static void clearMetaStatuses(String jsonId) {
        metaMessageStatusMap.remove(jsonId);
        errorCountMap.remove(jsonId);
    }
}
