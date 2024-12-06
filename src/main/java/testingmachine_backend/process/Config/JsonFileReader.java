package testingmachine_backend.process.Config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import testingmachine_backend.process.Controller.ProcessController;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class JsonFileReader {

    private static final Logger LOGGER = Logger.getLogger(JsonFileReader.class.getName());
    private static final String DIRECTORY_PATH = "C:\\Users\\batde\\Documents\\testingmachine-backend\\src\\json\\process\\result";
    private static final String JSON_FILE_NAME = DIRECTORY_PATH + File.separator + ProcessController.getJsonId() + "_result.json";

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
