package testingmachine_backend.controller;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import testingmachine_backend.process.Controller.SystemData;

import java.util.concurrent.CompletableFuture;

@Service
public class ModuleExecutionService {

    private final ModuleService moduleService;

    public ModuleExecutionService(ModuleService moduleService) {
        this.moduleService = moduleService;
    }

    @Async("asyncExecutor")  // Thread Pool ашиглана
    public CompletableFuture<String> executeModuleAsync(String module, SystemData systemData) {
        try {
            if (systemData != null) {
                String result = moduleService.executeModule(module, systemData);
                return CompletableFuture.completedFuture(result);
            } else {
                throw new RuntimeException("Өгөгдөл олдсонгүй");
            }
        } catch (Exception e) {
            return CompletableFuture.failedFuture(new RuntimeException("Модуль ажиллуулахад алдаа гарлаа: " + e.getMessage(), e));
        }
    }
}

