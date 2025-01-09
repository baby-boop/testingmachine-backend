package testingmachine_backend.schedule;

import java.util.HashMap;
import java.util.Map;

public class ScheduleService {

    private Map<String, ScheduleDTO> scheduleDataMap = new HashMap<>();

    public void storeUserData(String username, String password, String unitName, String moduleId, String scheduleTime) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setUsername(username);
        scheduleDTO.setPassword(password);
        scheduleDTO.setUnitName(unitName);
        scheduleDTO.setModuleId(moduleId);
        scheduleDTO.setScheduleTime(scheduleTime);

        scheduleDataMap.put(username, scheduleDTO);
    }

    // Өгөгдлийг авах
    public ScheduleDTO getScheduleData(String username) {
        return scheduleDataMap.get(username);
    }

    // Бүх хэрэглэгчийн өгөгдлийг авах
    public Map<String, ScheduleDTO> getAllScheduleData() {
        return scheduleDataMap;
    }

}
