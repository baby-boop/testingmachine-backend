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

import static testingmachine_backend.controller.JsonController.BASE_DIRECTORY;

public class JsonFileReader {

    private static final Logger LOGGER = Logger.getLogger(JsonFileReader.class.getName());

    private static final String DIRECTORY_PATH_PROCESS = BASE_DIRECTORY + "/process/result";
    private static final String DIRECTORY_PATH_META = BASE_DIRECTORY + "/metalistwithprocess/result";

    /**
     * Save a DTO to a single JSON file, appending it to a list of existing DTOs.
     *
     * @param dto  The DTO object to save.
     * @param <T>  The type of the DTO.
     * @param type The type of the JSON file (either "meta" or "process").
     */
    public static <T> void saveToSingleJsonFile(T dto, String type, String jsonId) {
        String directoryPath;
        String jsonFileName;

        switch (type) {
            case "process":
                directoryPath = DIRECTORY_PATH_PROCESS;
                break;
            case "meta":
                directoryPath = DIRECTORY_PATH_META;
                break;
            default:
                LOGGER.log(Level.WARNING, "Invalid type: {0}. Must be 'meta' or 'process'.", type);
                return;
        }

        jsonFileName = directoryPath + File.separator + jsonId + "_result.json";
        File directory = new File(directoryPath);
        File jsonFile = new File(jsonFileName);

        if (!directory.exists() && !directory.mkdirs()) {
            LOGGER.log(Level.SEVERE, "Failed to create directories: {0}", directoryPath);
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Object> dtoList = jsonFile.exists()
                    ? objectMapper.readValue(jsonFile, new TypeReference<List<Object>>() {})
                    : new ArrayList<>();

            dtoList.add(dto);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, dtoList);

            LOGGER.log(Level.INFO, "DTO successfully saved to {0}", jsonFileName);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving DTO to JSON file: " + jsonFileName, e);
        }
    }
}
