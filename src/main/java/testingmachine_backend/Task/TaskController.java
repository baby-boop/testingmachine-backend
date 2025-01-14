package testingmachine_backend.Task;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/module-schedule")
    public ResponseEntity<String> saveOrUpdateTask(@RequestBody TaskDTO task) {
        try {
            taskService.saveOrUpdateTask(task);
            return ResponseEntity.ok("Амжилттай хадгалагдлаа.");
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error saving or updating task: " + e.getMessage());
        }
    }
}
