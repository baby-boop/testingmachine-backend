package testingmachine_backend.projects.meta.DTO;

public class ListDTO {
    private int metaCount;
    private int workflowCount;
    public ListDTO(int metaCount, int workflowCount) {
        this.metaCount = metaCount;
        this.workflowCount = workflowCount;
    }

    public int getMetaCount() {
        return metaCount;
    }
    public int getWorkflowCount() {
        return workflowCount;
    }

}
