package testingmachine_backend.meta.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import testingmachine_backend.controller.JsonController;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static testingmachine_backend.controller.JsonController.BASE_DIRECTORY;

public class JsonFileReaderMeta {

    private static final Logger LOGGER = Logger.getLogger(JsonFileReaderMeta.class.getName());

    private static final String DIRECTORY_PATH_META = BASE_DIRECTORY + "/metalist/result";
    private static final String DIRECTORY_PATH_PATCH = BASE_DIRECTORY + "/patch/result";
    /**
     * Save a DTO to a single JSON file, appending it to a list of existing DTOs.
     *
     * @param dto The DTO object to save.
     * @param <T> The type of the DTO.
     * @param type The type of the JSON file (either "meta" or "patch").
     */
    public static <T> void saveToSingleJsonFile(T dto, String jsonId, String type) {
        String directoryPath;
        String currentJsonFileName;
        switch (type) {
            case "meta":
                directoryPath = DIRECTORY_PATH_META;
                break;
            case "patch":
                directoryPath = DIRECTORY_PATH_PATCH;
                break;
            default:
                LOGGER.log(Level.WARNING, "Invalid type: {0}. Must be 'meta' or 'process'.", type);
                return;
        }
        currentJsonFileName = directoryPath + File.separator + jsonId + "_result.json";

        File directory = new File(directoryPath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File jsonFile = new File(currentJsonFileName);
        ObjectMapper objectMapper = new ObjectMapper();
        List<Object> dtoList = new ArrayList<>();

        if (jsonFile.exists()) {
            try {
                dtoList = objectMapper.readValue(jsonFile, new TypeReference<List<Object>>() {});
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Error reading JSON file.", e);
            }
        } else {
            LOGGER.log(Level.INFO, "Successfully saved to: " + jsonId);
        }

        dtoList.add(dto);

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, dtoList);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving to JSON file", e);
        }
    }

}
