package testingmachine_backend.controller;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import testingmachine_backend.projects.meta.DTO.ErrorMessageDTO;
import testingmachine_backend.projects.meta.Service.MetaMessageStatusService;
import testingmachine_backend.projects.process.Controller.SystemData;
import testingmachine_backend.projects.process.DTO.ProcessMessageStatusDTO;
import testingmachine_backend.projects.process.Service.ProcessMessageStatusService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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


                List<ProcessMessageStatusDTO> processStatuses = ProcessMessageStatusService.getProcessStatuses(systemData.getGeneratedId());
                List<ErrorMessageDTO> metaStatuses = MetaMessageStatusService.getMetaStatuses(systemData.getGeneratedId());

                boolean hasFailedOrError = processStatuses.stream()
                        .anyMatch(p -> p.getStatus().equalsIgnoreCase("failed") || p.getStatus().equalsIgnoreCase("error"))
                        || metaStatuses.stream()
                        .anyMatch(m -> m.getStatus().equalsIgnoreCase("failed") || m.getStatus().equalsIgnoreCase("error"));

                int statusMessage = hasFailedOrError ? 0 : 1;
                String fullUrl = "http://172.169.88.222:3000/result/" + systemData.getGeneratedId();

                Map<String, Object> combinedResponse = new HashMap<>();

                if (!processStatuses.isEmpty() && metaStatuses.isEmpty()) {
                    combinedResponse.put("jsonId", systemData.getGeneratedId());
                    combinedResponse.put("statusMessage", statusMessage);
                    combinedResponse.put("fullUrl", fullUrl);
                    combinedResponse.put("processDetails", processStatuses);
                } else if (!metaStatuses.isEmpty() && processStatuses.isEmpty()) {
                    combinedResponse.put("jsonId", systemData.getGeneratedId());
                    combinedResponse.put("statusMessage", statusMessage);
                    combinedResponse.put("fullUrl", fullUrl);
                    combinedResponse.put("metaDetails", metaStatuses);
                } else if (!processStatuses.isEmpty() && !metaStatuses.isEmpty()) {
                    combinedResponse.put("jsonId", systemData.getGeneratedId());
                    combinedResponse.put("statusMessage", statusMessage);
                    combinedResponse.put("fullUrl", fullUrl);
                    combinedResponse.put("processDetails", processStatuses);
                    combinedResponse.put("metaDetails", metaStatuses);
                } else {
                    combinedResponse.put("message", "Тестийг амжилттай хүлээж авлаа");
                }

                ProcessMessageStatusService.saveToJson(systemData.getGeneratedId(), 9, systemData.getSelectedModule(), systemData.getCustomerName(), statusMessage, fullUrl);
                MetaMessageStatusService.saveToJson(systemData.getGeneratedId(),  systemData.getSelectedModule(), systemData.getCustomerName(),statusMessage, fullUrl);

                // Ашигласан өгөгдлийг цэвэрлэх
                ProcessMessageStatusService.clearAllDTOField(systemData.getGeneratedId());
                MetaMessageStatusService.clearMetaStatuses(systemData.getGeneratedId());

                if(systemData.getSelectedModule().equals("patch")){
                    PatchAdditionResult.getProcessMetaDataList(systemData.getDatabaseName(), systemData.getSystemURL(),
                            systemData.getUsername(), systemData.getPassword(), fullUrl, systemData.getMetaOrPatchId(), statusMessage);
                }

                return CompletableFuture.completedFuture(result);
            } else {
                throw new RuntimeException("Өгөгдөл олдсонгүй");
            }
        } catch (Exception e) {
            return CompletableFuture.failedFuture(new RuntimeException("Модуль ажиллуулахад алдаа гарлаа: " + e.getMessage(), e));
        }
    }
}

