package testingmachine_backend.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import testingmachine_backend.controller.JsonController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonFileReader {

    private static final Logger LOGGER = Logger.getLogger(JsonFileReader.class.getName());
    private static final String DIRECTORY_PATH1 = "src/main/java/testingmachine_backend/json/process/result";
    private static final String JSON_FILE_NAME1 = DIRECTORY_PATH1 + File.separator + JsonController.getJsonId() + "_result.json";

    private static final String DIRECTORY_PATH2 = "src/main/java/testingmachine_backend/json/metalistwithprocess/result";
    private static final String JSON_FILE_NAME2 = DIRECTORY_PATH2 + File.separator + JsonController.getJsonId() + "_result.json";

    /**
     * Save a DTO to a single JSON file, appending it to a list of existing DTOs.
     *
     * @param dto The DTO object to save.
     * @param <T> The type of the DTO.
     * @param type The type of the JSON file (either "meta" or "process").
     */

    public static <T> void saveToSingleJsonFile(T dto, String type) {
        File directory = null;
        File jsonFile = null;

        if ("process".equals(type)) {
            directory = new File(DIRECTORY_PATH1);
            jsonFile = new File(JSON_FILE_NAME1);
        } else if ("meta".equals(type)) {
            directory = new File(DIRECTORY_PATH2);
            jsonFile = new File(JSON_FILE_NAME2);
        }

        if (directory != null && !directory.exists()) {
            directory.mkdirs();
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Object> dtoList = new ArrayList<>();
            if (jsonFile.exists()) {
                dtoList = objectMapper.readValue(jsonFile, new TypeReference<List<Object>>() {});
            }

            dtoList.add(dto);

            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, dtoList);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving DTO to JSON file", e);
        }
    }

}


