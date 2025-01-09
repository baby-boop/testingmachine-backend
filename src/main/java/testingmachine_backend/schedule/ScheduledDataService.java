package testingmachine_backend.schedule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.ArrayList;
import java.time.LocalDateTime;

@Service
public class ScheduledDataService {

    private List<ScheduledData> scheduledDataList = new ArrayList<>();

    public void initializeData() {
        scheduledDataList.add(new ScheduledData("Data1", LocalDateTime.now().plusSeconds(10)));
        scheduledDataList.add(new ScheduledData("Data2", LocalDateTime.now().plusSeconds(20)));
    }

    @Scheduled(fixedRate = 4000)
    public void checkAndRunScheduledTasks() {
        LocalDateTime now = LocalDateTime.now();

        for (ScheduledData scheduledData : scheduledDataList) {
            if (scheduledData.getScheduledTime().isBefore(now)) {
                printData(scheduledData);
                scheduledDataList.remove(scheduledData);
            }
        }
    }

    public void printData(ScheduledData scheduledData) {
        System.out.println("Printing Data: " + scheduledData.getDataId());
    }
}
