package testingmachine_backend.metaverse.DTO;

public class MVErrorTimeoutDTO {

    private String fileName;
    private String id;
    private String menuId;

    public MVErrorTimeoutDTO(String fileName, String id, String menuId) {
        this.fileName = fileName;
        this.id = id;
        this.menuId = menuId;
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

    public String getMenuId() {
        return menuId;
    }

    public void setMenuId(String menuId) {
        this.menuId = menuId;
    }
}
