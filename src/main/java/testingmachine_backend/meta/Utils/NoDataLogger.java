package testingmachine_backend.meta.Utils;

import testingmachine_backend.meta.DTO.ErrorMessageDTO;
import testingmachine_backend.meta.Service.MetaMessageStatusService;

import java.util.ArrayList;
import java.util.List;

public class NoDataLogger {

    public static void logError(String moduleName, String id, String code, String name, String jsonId) {
        System.err.println("metaId: " + id + ", fileName: " + moduleName);
        MetaMessageStatusService.addMetaStatus(moduleName, id, code, name, "nodata", "", jsonId);
    }

}
