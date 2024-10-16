package testingmachine_backend.meta.DTO;

public class NotFoundRowDTO {
    private String fileName;
    private String id;

    public NotFoundRowDTO(String fileName, String id) {
        this.fileName = fileName;
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
