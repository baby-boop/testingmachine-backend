package testingmachine_backend.Task;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static testingmachine_backend.controller.JsonController.BASE_DIRECTORY;

@Service
public class TaskService {
    private final ObjectMapper mapper = new ObjectMapper();
    private final File jsonFile = new File(BASE_DIRECTORY + "/schedule/schedule.json");

    public TaskService() {
        mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        mapper.configure(com.fasterxml.jackson.databind.SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        mapper.setDateFormat(new java.text.SimpleDateFormat("yyyy-MM-dd"));
    }

    public List<TaskDTO> getTasks() throws IOException {
        if (jsonFile.exists() && jsonFile.length() > 0) {
            return mapper.readValue(jsonFile, new TypeReference<>() {
            });
        } else {
            return new ArrayList<>();
        }
    }

    public TaskDTO saveOrUpdateTask(TaskDTO task) throws IOException {
        List<TaskDTO> tasks = getTasks();
        Optional<TaskDTO> existingTask = tasks.stream()
                .filter(t -> t.getModuleId().equals(task.getModuleId()))
                .findFirst();

        if (existingTask.isPresent()) {
            TaskDTO taskToUpdate = existingTask.get();
            taskToUpdate.setTaskName(task.getTaskName());
            taskToUpdate.setInterval(task.getInterval());
            taskToUpdate.setLastUpdatedDate(task.getLastUpdatedDate());
        } else {
            tasks.add(task);
        }

        mapper.writeValue(jsonFile, tasks);
        return task;

    }
}
