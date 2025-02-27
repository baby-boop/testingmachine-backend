package testingmachine_backend.projects.meta.Utils;

public class NoDataLogger {

    public static void logError(String moduleName, String id, String code, String name, String jsonId) {
        System.err.println("metaId: " + id + ", fileName: " + moduleName);
//        MetaMessageStatusService.addMetaStatus(moduleName, id, code, name, "nodata", "",  jsonId);
    }

}
