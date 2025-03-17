package testingmachine_backend.projects.meta.Utils;

import testingmachine_backend.projects.process.Service.ProcessMessageStatusService;

public class ErrorLogger {

    public static void logError(String fileName, String id, String code, String name,  String jsonId, String type, int totalCount, String customerName) {
        System.err.println("metaId: " + id + ", fileName: " + fileName);

        ProcessMessageStatusService.addMetaStatus(fileName, id, code, name, "info", "", jsonId, type, totalCount, customerName);
    }

}