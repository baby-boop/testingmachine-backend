package testingmachine_backend.process.DTO;

public class ProcessDTO {

    private int processCount;
    private int totalProcessCount;
    public ProcessDTO(int processCount, int totalProcessCount) {
        this.processCount = processCount;
        this.totalProcessCount = totalProcessCount;
    }

    public int getProcessCount() {
        return processCount;
    }
    public int getTotalProcessCount() {
        return totalProcessCount;
    }

}
