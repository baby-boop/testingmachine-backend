package testingmachine_backend.projects.meta.Utils;

import testingmachine_backend.projects.meta.Service.MetaMessageStatusService;

public class ErrorLogger {

    public static void logError(String fileName, String id, String code, String name,  String jsonId, String type, int totalCount, String customerName) {
        System.err.println("metaId: " + id + ", fileName: " + fileName);

        MetaMessageStatusService.addMetaStatus(fileName, id, code, name, "info", "", jsonId, type, totalCount, customerName);
    }

}