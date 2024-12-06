package testingmachine_backend.process.Controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileData {
    private String fileName;
    private Object data;
    private String customerName;
    private String testURL;

    public FileData(String fileName, Object data, String customerName, String testURL) {
        this.fileName = fileName;
        this.data = data;
        this.customerName = customerName;
        this.testURL = testURL;

    }
}
