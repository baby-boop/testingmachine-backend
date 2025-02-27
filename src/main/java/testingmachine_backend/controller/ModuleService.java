package testingmachine_backend.controller;

import org.springframework.stereotype.Service;
import testingmachine_backend.projects.indicator.IndicatorMain;
import testingmachine_backend.projects.product.ProductMain;
import testingmachine_backend.projects.metaverse.MetaverseMain;
import testingmachine_backend.projects.process.Controller.SystemData;
import testingmachine_backend.projects.meta.MetaList.MetaMain;
import testingmachine_backend.projects.metaWithProcess.MetaWithProcessMain;
import testingmachine_backend.projects.patch.patchMain;
import testingmachine_backend.projects.process.MainProcess;

@Service
public class ModuleService {

    public String executeModule(String module, SystemData systemData) {
        String result;
        switch (module) {
            case "meta":
                System.out.println("Тест эхлэж байна... meta");
                MetaMain.mainSystem(systemData.getGeneratedId(), String.valueOf(Thread.currentThread().getId()), systemData.getCustomerName(),
                        systemData.getCreatedDate(), systemData.getModuleId(), systemData.getDatabaseName(), systemData.getDatabaseUsername(),
                        systemData.getSystemURL(), systemData.getUsername(), systemData.getPassword(), systemData.getIsCheckBox());
                result = "Тест хийж дууссан (meta)";
                break;
            case "process":
                System.out.println("Тест эхлэж байна... process");
                MainProcess.mainProcess(systemData.getGeneratedId(), String.valueOf(Thread.currentThread().getId()), systemData.getCustomerName(),
                        systemData.getCreatedDate(), systemData.getModuleId(), systemData.getDatabaseName(), systemData.getDatabaseUsername(),
                        systemData.getSystemURL(), systemData.getUsername(), systemData.getPassword(), systemData.getMetaOrPatchId(), systemData.getIsCheckBox());
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
                        systemData.getSystemURL(), systemData.getUsername(), systemData.getPassword(), systemData.getMetaOrPatchId(), systemData.getIsCheckBox());
                result = "Тест хийж дууссан (patch)";
                break;
            case "metaverse":
                System.out.println("Тест эхлэж байна... metaverse");
                MetaverseMain.mainSystem(systemData.getGeneratedId(), String.valueOf(Thread.currentThread().getId()), systemData.getCustomerName(),
                        systemData.getCreatedDate(), systemData.getModuleId(), systemData.getDatabaseName(), systemData.getDatabaseUsername(),
                        systemData.getSystemURL(), systemData.getUsername(), systemData.getPassword(), systemData.getIsCheckBox());
                result = "Тест хийж дууссан (metaverse)";
                break;
            case "indicator":
                System.out.println("Тест эхлэж байна... indicator");
                IndicatorMain.mainProcess(systemData.getGeneratedId(), String.valueOf(Thread.currentThread().getId()), systemData.getCustomerName(),
                        systemData.getCreatedDate(), systemData.getModuleId(), systemData.getDatabaseName(), systemData.getDatabaseUsername(),
                        systemData.getSystemURL(), systemData.getUsername(), systemData.getPassword(), systemData.getMetaOrPatchId(), systemData.getIsCheckBox());
                result = "Тест хийж дууссан (indicator)";
                break;
            case "product":
                System.out.println("Тест эхлэж байна... product");
                ProductMain.mainProcess(systemData.getGeneratedId(), String.valueOf(Thread.currentThread().getId()), systemData.getCustomerName(),
                        systemData.getCreatedDate(), systemData.getModuleId(), systemData.getDatabaseName(), systemData.getDatabaseUsername(),
                        systemData.getSystemURL(), systemData.getUsername(), systemData.getPassword(), systemData.getMetaOrPatchId(), systemData.getIsCheckBox());
                result = "Тест хийж дууссан (product)";
                break;
            default:
                throw new IllegalArgumentException("Модуль олдсонгүй");
        }
        return result;
    }
}
