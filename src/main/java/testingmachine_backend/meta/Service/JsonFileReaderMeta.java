package testingmachine_backend.meta.Service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import testingmachine_backend.controller.JsonController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonFileReaderMeta {

    private static final Logger LOGGER = Logger.getLogger(JsonFileReaderMeta.class.getName());
    private static final String DIRECTORY_PATH = "src/main/java/testingmachine_backend/json/metalist/result";
    private static final String JSON_FILE_NAME = DIRECTORY_PATH + File.separator + JsonController.getJsonId() + "_result.json";

    /**
     * Save a DTO to a single JSON file, appending it to a list of existing DTOs.
     *
     * @param dto The DTO object to save.
     * @param <T> The type of the DTO.
     */
    public static <T> void saveToSingleJsonFile(T dto) {
        File directory = new File(DIRECTORY_PATH);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File jsonFile = new File(JSON_FILE_NAME);
        ObjectMapper objectMapper = new ObjectMapper();
        List<Object> dtoList = new ArrayList<>();

        if (jsonFile.exists()) {
            try {
                dtoList = objectMapper.readValue(jsonFile, new TypeReference<List<Object>>() {});
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Error reading existing JSON file. Creating a new list.", e);
            }
        }

        dtoList.add(dto);

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, dtoList);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving DTO to JSON file", e);
        }
    }
}
