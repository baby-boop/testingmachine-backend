package testingmachine_backend.process.Controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileData {
    private String fileName;
    private Object data;


    public FileData(String fileName, Object data) {
        this.fileName = fileName;
        this.data = data;

    }
}
