package testingmachine_backend.controller;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import testingmachine_backend.TestingmachineBackendApplication;
import testingmachine_backend.process.Controller.SystemData;

import java.util.concurrent.CompletableFuture;

@Service
public class ModuleExecutionService {

    private final TestingmachineBackendApplication application;

    public ModuleExecutionService(TestingmachineBackendApplication application) {
        this.application = application;
    }
    @Async
    public CompletableFuture<String> executeModuleAsync(String module, SystemData systemData) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if(systemData != null) {
                    return application.executeModule(module, systemData);
                } else {
                    throw new RuntimeException("Өгөгдөл олдсонгүй");  // Data not found error
                }
            } catch (Exception e) {
                throw new RuntimeException("Модуль ажиллуулахад алдаа гарлаа: " + e.getMessage(), e);
            }
        });
    }

}
