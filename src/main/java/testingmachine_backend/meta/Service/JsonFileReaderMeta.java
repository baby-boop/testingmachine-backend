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

import static testingmachine_backend.controller.JsonController.BASE_DIRECTORY;

public class JsonFileReaderMeta {

    private static final Logger LOGGER = Logger.getLogger(JsonFileReaderMeta.class.getName());

    private static final String DIRECTORY_PATH = BASE_DIRECTORY + "/metalist/result";
    private static final String JSON_FILE_NAME = DIRECTORY_PATH + File.separator + JsonController.getJsonId() + "_result.json";

    /**
     * Save a DTO to a single JSON file, appending it to a list of existing DTOs.
     *
     * @param dto The DTO object to save.
     * @param <T> The type of the DTO.
     */
    public static <T> void saveToSingleJsonFile(T dto) {
        // Шинэ JSON ID-г авах
        String jsonId = JsonController.getJsonId();
        String currentJsonFileName = DIRECTORY_PATH + File.separator + jsonId + "_result.json";
        File directory = new File(DIRECTORY_PATH);
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
            LOGGER.log(Level.INFO, "Creating new JSON file for ID: " + jsonId);
        }

        dtoList.add(dto);

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(jsonFile, dtoList);
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error saving DTO to JSON file", e);
        }
    }

}

//Энэхүү код эхний удаа зөв үүсгээд ажиллаж байгаа боловч 2 дахь удаа шинээр үүсгэхгүй эхний үүсгэсэн дээр датагаа нэмэж байна. JsonController.getJsonId()-оор шалгаж хэрэв байхгүй бол үүсгэх. Үүссэн байвал одоо байгаа JsonController.getJsonId()-аар шалгаж тухайн Json дээр л датаг нэмэх хэрэгтэй
