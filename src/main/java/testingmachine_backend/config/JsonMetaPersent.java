package testingmachine_backend.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static testingmachine_backend.controller.JsonController.BASE_DIRECTORY;

@Slf4j
public class JsonMetaPersent {

    private static final Logger LOGGER = Logger.getLogger(JsonMetaPersent.class.getName());

    private static final String DIRECTORY_PATH_PERCENT = BASE_DIRECTORY + "/percent";
    /**
     * Save a DTO to a single JSON file, appending it to a list of existing DTOs.
     *
     * @param dto  The DTO object to save.
     * @param <T>  The type of the DTO.
     */
    public static <T> void saveToSingleJsonFile(T dto, String jsonId) {

        String jsonFileName = DIRECTORY_PATH_PERCENT + File.separator + jsonId + "_result.json";
        File directory = new File(DIRECTORY_PATH_PERCENT);
        File jsonFile = new File(jsonFileName);

        if (!directory.exists() && !directory.mkdirs()) {
            LOGGER.log(Level.SEVERE, "Failed to create directories: {0}", DIRECTORY_PATH_PERCENT);
            return;
        }

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            List<Object> dtoList = jsonFile.exists()
                    ? objectMapper.readValue(jsonFile, new TypeReference<List<Object>>() {})
                    : new ArrayList<>();

            dtoList.add(dto);
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, dtoList);

            LOGGER.log(Level.INFO, "Successfully saved to {0}", jsonFileName);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving to JSON file: " + jsonFileName, e);
        }
    }
}
