package testingmachine_backend.controller;

import org.springframework.stereotype.Service;
import testingmachine_backend.process.Controller.SystemData;
import testingmachine_backend.meta.MetaList.MetaMain;
import testingmachine_backend.metaWithProcess.MetaWithProcessMain;
import testingmachine_backend.patch.patchMain;
import testingmachine_backend.process.MainProcess;

@Service
public class ModuleService {

    public String executeModule(String module, SystemData systemData) {
        String result;
        switch (module) {
            case "meta":
                System.out.println("Тест эхлэж байна... meta");
                MetaMain.mainSystem(systemData.getGeneratedId(), String.valueOf(Thread.currentThread().getId()), systemData.getCustomerName(),
                        systemData.getCreatedDate(), systemData.getModuleId(), systemData.getDatabaseName(), systemData.getDatabaseUsername(),
                        systemData.getSystemURL(), systemData.getUsername(), systemData.getPassword());
                result = "Тест хийж дууссан (meta)";
                break;
            case "process":
                System.out.println("Тест эхлэж байна... process");
                MainProcess.mainProcess(systemData.getGeneratedId(), String.valueOf(Thread.currentThread().getId()), systemData.getCustomerName(),
                        systemData.getCreatedDate(), systemData.getModuleId(), systemData.getDatabaseName(), systemData.getDatabaseUsername(),
                        systemData.getSystemURL(), systemData.getUsername(), systemData.getPassword(), systemData.getMetaOrPatchId());
                result = "Тест хийж дууссан (process)";
                break;
            case "metaWithProcess":
                System.out.println("Тест эхлэж байна... metaWithProcess");
                MetaWithProcessMain.mainProcess(systemData.getGeneratedId());
                result = "Тест хийж дууссан (metaWithProcess)";
                break;
            case "patch":
                System.out.println("Patch тест эхлэж байна");
                patchMain.mainProcess(systemData.getGeneratedId(), String.valueOf(Thread.currentThread().getId()), systemData.getCustomerName(),
                        systemData.getCreatedDate(), systemData.getModuleId(), systemData.getDatabaseName(), systemData.getDatabaseUsername(),
                        systemData.getSystemURL(), systemData.getUsername(), systemData.getPassword(), systemData.getMetaOrPatchId());
                result = "Тест хийж дууссан (patch)";
                break;
            default:
                throw new IllegalArgumentException("Модуль олдсонгүй");
        }
        return result;
    }
}
