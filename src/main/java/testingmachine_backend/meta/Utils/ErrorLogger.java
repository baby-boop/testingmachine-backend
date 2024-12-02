package testingmachine_backend.meta.Utils;

import testingmachine_backend.meta.Service.MetaMessageStatusService;

public class ErrorLogger {

    public static void logError(String fileName, String id, String code, String name) {
        System.err.println("metaId: " + id + ", fileName: " + fileName);

        MetaMessageStatusService.addMetaStatus(fileName, id, code, name, "info", "");
    }

}