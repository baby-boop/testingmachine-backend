package testingmachine_backend.meta.Service;

import testingmachine_backend.meta.DTO.ErrorMessageDTO;

import java.util.*;

public class MetaMessageStatusService {

    private static final ThreadLocal<List<ErrorMessageDTO>> metaMessageStatusList = ThreadLocal.withInitial(ArrayList::new);
    private static final ThreadLocal<Integer> errorCount = ThreadLocal.withInitial(() -> 0);

    /**
     * Алдааны статус нэмэх функц
     */
    public static void addMetaStatus(String moduleName, String id, String code, String name, String type, String messageText, String jsonId, String checkerType, int totalCount, String customerName) {
        ErrorMessageDTO statusDTO = new ErrorMessageDTO(moduleName, id, code, name, type, messageText, jsonId);
        metaMessageStatusList.get().add(statusDTO);

        if ("error".equalsIgnoreCase(type)) {
            errorCount.set(errorCount.get() + 1);
        }

        saveToJson(jsonId, totalCount, checkerType, customerName);
    }

    /**
     * JSON хадгалах функц (хуучин өгөгдлийг хадгалах байдлаар)
     */
    private static void saveToJson(String jsonId, int totalCount, String type, String customerName) {
        Map<String, Object> jsonOutput = readExistingJson(jsonId, type);

        // Шинэ мэдээлэл нэмэх
        List<ErrorMessageDTO> existingDetails = (List<ErrorMessageDTO>) jsonOutput.getOrDefault("details", new ArrayList<>());
        existingDetails.addAll(metaMessageStatusList.get());

        jsonOutput.put("jsonId", jsonId);
        jsonOutput.put("customerName", customerName);
        jsonOutput.put("totalCount", totalCount);
        jsonOutput.put("errorCount", errorCount.get() + (int) jsonOutput.getOrDefault("errorCount", 0));
        jsonOutput.put("details", existingDetails);

        // Файл руу хадгалах
        JsonFileReaderMeta.saveJsonToFile(jsonId, type, jsonOutput);

        // ThreadLocal-уудыг цэвэрлэх
        metaMessageStatusList.get().clear();
        errorCount.set(0);
    }

    /**
     * Хэрэв өмнө нь JSON файл үүссэн байвал түүнийг уншина
     */
    private static Map<String, Object> readExistingJson(String jsonId, String type) {
        return JsonFileReaderMeta.readJsonFromFile(jsonId, type);
    }
}
