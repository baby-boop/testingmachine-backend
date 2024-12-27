package testingmachine_backend.controller;

import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class FileController {

    private final List<String> baseFolderPaths = Arrays.asList(
//            "C:\\Users\\batde\\Documents\\testingmachine-backend\\src\\json\\process\\header",
//            "C:\\Users\\batde\\Documents\\testingmachine-backend\\src\\json\\process\\result"

            "src/main/java/testingmachine_backend/json/metalistwithprocess/header",
            "src/main/java/testingmachine_backend/json/metalistwithprocess/result"
    );

    @GetMapping("/list-folders")
    public List<String> listFolders() {
        return baseFolderPaths.stream()
                .map(path -> new File(path).getName())
                .collect(Collectors.toList());
    }

    @DeleteMapping("/delete-json-files")
    public String deleteJsonFiles(@RequestParam String folderName) {
        String folderPath = baseFolderPaths.stream()
                .filter(path -> path.endsWith(folderName))
                .findFirst()
                .orElse(null);

        if (folderPath == null) {
            return "Folder олдсонгүй!";
        }

        File folder = new File(folderPath);
        File[] jsonFiles = folder.listFiles((dir, name) -> name.endsWith(".json"));

        if (jsonFiles == null || jsonFiles.length == 0) {
            return "json file олдсонгүй.";
        }

        for (File file : jsonFiles) {
            if (!file.delete()) {
                return "Алдаа: " + file.getName();
            }
        }

        return  folderName + " амжилттай устгагдлаа";
    }
}
