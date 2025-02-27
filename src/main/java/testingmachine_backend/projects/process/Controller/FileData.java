package testingmachine_backend.projects.process.Controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileData {
    private String id;
    private Object data;


    public FileData(String id, Object data) {
        this.id = id;
        this.data = data;

    }
}
