package testingmachine_backend.meta.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static testingmachine_backend.controller.JsonController.BASE_DIRECTORY;

public class JsonFileReaderMeta {

    private static final Logger LOGGER = Logger.getLogger(JsonFileReaderMeta.class.getName());

    private static final String DIRECTORY_PATH_META = BASE_DIRECTORY + "/metalist/result";
    private static final String DIRECTORY_PATH_PATCH = BASE_DIRECTORY + "/patch/result";

    /**
     * JSON файл руу хадгалах функц
     */
    public static void saveJsonToFile(String jsonId, String type, Map<String, Object> jsonOutput) {
        String directoryPath = getDirectoryPath(type);
        if (directoryPath == null) return;

        String currentJsonFileName = directoryPath + File.separator + jsonId + "_result.json";
        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        ObjectMapper objectMapper = new ObjectMapper();
        File jsonFile = new File(currentJsonFileName);

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, jsonOutput);
            LOGGER.log(Level.INFO, "JSON successfully saved: " + currentJsonFileName);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving to JSON file", e);
        }
    }

    /**
     * Хэрэв JSON файл байвал унших
     */
    public static Map<String, Object> readJsonFromFile(String jsonId, String type) {
        String directoryPath = getDirectoryPath(type);
        if (directoryPath == null) return new HashMap<>();

        String currentJsonFileName = directoryPath + File.separator + jsonId + "_result.json";
        File jsonFile = new File(currentJsonFileName);
        ObjectMapper objectMapper = new ObjectMapper();

        if (jsonFile.exists()) {
            try {
                return objectMapper.readValue(jsonFile, new TypeReference<Map<String, Object>>() {});
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Error reading JSON file, returning empty map.", e);
            }
        }
        return new HashMap<>();
    }

    private static String getDirectoryPath(String type) {
        switch (type) {
            case "meta":
                return DIRECTORY_PATH_META;
            case "patch":
                return DIRECTORY_PATH_PATCH;
            default:
                LOGGER.log(Level.WARNING, "Invalid type: {0}. Must be 'meta' or 'patch'.", type);
                return null;
        }
    }
}
