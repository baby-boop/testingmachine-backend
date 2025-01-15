package testingmachine_backend.controller;

import org.springframework.web.bind.annotation.*;
import testingmachine_backend.TestingmachineBackendApplication;

import java.util.Map;

@RestController
public class ModuleController {

    private final TestingmachineBackendApplication application;

    public ModuleController(TestingmachineBackendApplication application) {
        this.application = application;
    }



    @PostMapping("/module")
    public String executeModule(@RequestBody Map<String, String> request) {
        String module = request.get("module");
        String errorMessage = "";
        String successMessage = "";

        try {
//            String moduleMessage = application.executeModule(module);



        } catch (Exception e) {
            errorMessage = "Модуль ажиллуулахад алдаа гарлаа! " + e;
            return errorMessage;
        }

        return "Модуль бүрэн ажиллаж дуусаагүй";
    }
}
