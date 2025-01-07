package testingmachine_backend.controller;

import org.springframework.web.bind.annotation.*;
import testingmachine_backend.TestingmachineBackendApplication;
import testingmachine_backend.config.CounterDTO;
import testingmachine_backend.config.CounterService;

import java.util.Map;

@RestController
public class ModuleController {

    private final TestingmachineBackendApplication application;

    public ModuleController(TestingmachineBackendApplication application) {
        this.application = application;
    }

    @GetMapping("/meta-counter")
    public CounterDTO getLatestCounter() {
        CounterDTO latestCounter = CounterService.getLatestCounter();
        if (latestCounter == null) {
            return null;
        }
        return latestCounter;
    }

    @PostMapping("/module")
    public String executeModule(@RequestBody Map<String, String> request) {
        String module = request.get("module");
        String errorMessage = "";
        String successMessage = "";

        try {
            String moduleMessage = application.executeModule(module);

            int count = CounterService.getLatestCounter() != null ? CounterService.getLatestCounter().getCount() : 0;
            int totalCount = CounterService.getLatestCounter() != null ? CounterService.getLatestCounter().getTotalCount() : 1;

            if (count == totalCount) {
                successMessage = moduleMessage;
                return successMessage;
            }

        } catch (Exception e) {
            errorMessage = "Модуль ажиллуулахад алдаа гарлаа! " + e;
            return errorMessage;
        }

        return "Модуль бүрэн ажиллаж дуусаагүй";
    }
}
