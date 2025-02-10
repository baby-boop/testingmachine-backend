package testingmachine_backend.controller;

import org.springframework.stereotype.Service;
import testingmachine_backend.process.Controller.SystemData;

import java.util.concurrent.CompletableFuture;

import java.util.concurrent.Executor;

@Service
public class ModuleExecutionService {

    private final ModuleService moduleService;
    private final Executor asyncExecutor;

    public ModuleExecutionService(ModuleService moduleService, Executor asyncExecutor) {
        this.moduleService = moduleService;
        this.asyncExecutor = asyncExecutor;
    }

    public CompletableFuture<String> executeModuleAsync(String module, SystemData systemData) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (systemData != null) {
                    return moduleService.executeModule(module, systemData);
                } else {
                    throw new RuntimeException("Өгөгдөл олдсонгүй");
                }
            } catch (Exception e) {
                throw new RuntimeException("Модуль ажиллуулахад алдаа гарлаа: " + e.getMessage(), e);
            }
        }, asyncExecutor);
    }
}
