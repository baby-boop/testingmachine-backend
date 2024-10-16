package testingmachine_backend.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import testingmachine_backend.TestingmachineBackendApplication;
import testingmachine_backend.meta.MetaList.MetaLists;

import java.util.Map;

@RestController
@RequestMapping("/api")
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
            String moduleMessage = application.executeModule(module);

            int metaCount = MetaLists.getCheckCount();
            int totalCount = MetaLists.getTotalCount();

            if (metaCount == totalCount) {
                successMessage = moduleMessage;
                return successMessage;
            }

        } catch (Exception e) {
            errorMessage = "Модуль ажиллуулахад алдаа гарлаа! ";
            return errorMessage;
        }

        return "Модуль бүрэн ажиллаж дуусаагүй";
    }
}
