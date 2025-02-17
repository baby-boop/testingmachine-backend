//package testingmachine_backend.process.Service;
//
//import lombok.Getter;
//import lombok.Setter;
//import lombok.extern.slf4j.Slf4j;
//import org.openqa.selenium.WebElement;
//import org.springframework.stereotype.Service;
//import testingmachine_backend.indicator.IndicatorCustomTab;
//import testingmachine_backend.indicator.IsIndicatorMessage;
//import testingmachine_backend.meta.Service.JsonFileReaderMeta;
//import testingmachine_backend.process.DTO.*;
//import testingmachine_backend.process.Messages.PopupMessage;
//import testingmachine_backend.process.utils.ElementsFunctionUtils;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//import java.util.logging.Level;
//
//@Slf4j
//@Service
//public class ProcessMessageStatusService {
//
//    private static final ConcurrentHashMap<String, Integer> errorCountMap = new ConcurrentHashMap<>();
//    private static final ConcurrentHashMap<String, List<ProcessMessageStatusDTO>> processMessageStatusMap = new ConcurrentHashMap<>();
//
//    public static void addProcessStatus(String fileName, String id, String code, String name, String status, String messageText, String TestProcessType, String jsonId) {
//
//        ProcessMessageStatusDTO statusDTO = new ProcessMessageStatusDTO(fileName, id, code, name, status, messageText, jsonId,
//                ElementsFunctionUtils.getProcessLogMessages(),
//                ElementsFunctionUtils.getUniqueEmptyDataPath(),
//                PopupMessage.getUniquePopupMessages(),
//                ElementsFunctionUtils.getPopupStandartMessages(),
//                ElementsFunctionUtils.getRequiredPathMessages(),
//                ElementsFunctionUtils.getComboMessages(),
//                IsIndicatorMessage.getIndicatorTabMesssage()
//        );
//
//        processMessageStatusMap.computeIfAbsent(jsonId, k -> new ArrayList<>()).add(statusDTO);
//        errorCountMap.put(jsonId, errorCountMap.getOrDefault(jsonId, 0) + 1);
//
//    }
//
//    public static void saveToJson(String jsonId, int totalCount, String type, String customerName) {
//        Map<String, Object> jsonOutput = readExistingJson(jsonId, type);
//
//        // Шинэ мэдээлэл нэмэх
//        List<ProcessMessageStatusDTO> existingDetails = (List<ProcessMessageStatusDTO>) jsonOutput.getOrDefault("details", new ArrayList<>());
//        existingDetails.addAll(processMessageStatusMap.getOrDefault(jsonId, new ArrayList<>()));
//
//        jsonOutput.put("jsonId", jsonId);
//        jsonOutput.put("customerName", customerName);
//        jsonOutput.put("totalCount", totalCount);
//        jsonOutput.put("errorCount", errorCountMap.getOrDefault(jsonId, 0));
//        jsonOutput.put("processDetails", existingDetails);
//
//        // Файл руу хадгалах
//        JsonFileReaderMeta.saveJsonToFile(jsonId, type, jsonOutput);
//
//    }
//}
//
//
//if(kpiTypeId != 0L && metaDataId == 0L){
//        if(kpiTypeId == 2008L){
//
////                                        List<WebElement> findAddRowButtons = findAddRowButtonBySidebar(driver, stepId);
////                                        findRow(driver, findAddRowButtons);
////                                        findAddRowButtons.clear();
//
//List<WebElement> dataPathBySidebars = findDataPathBySidebar(driver, stepId);
//processTabElements(driver, dataPathBySidebars, id, systemName, jsonId);
//                                        dataPathBySidebars.clear();
//
//consoleLogChecker(driver, stepId, sideBarText, jsonId);
//
//waitUtils(driver);
//SideBarSaveButton(driver, stepId);
//waitUtils(driver);
//
//                                        Thread.sleep(1000);
//consoleLogRequiredPath(driver, stepId, sideBarText, jsonId);
//                                        if (!IsIndicatorMessage.isErrorMessagePresent(driver, id, stepId, headerTabText, sideBarText, "METHOD", jsonId)) {
//waitUtils(driver);
//
//IndicatorCustomTab customTab = new IndicatorCustomTab(id, stepId, headerTabText, sideBarText, "METHOD", "failed", "Алдаа гарлаа", jsonId,
//        ElementsFunctionUtils.getProcessLogMessages(),
//        ElementsFunctionUtils.getUniqueEmptyDataPath(),
//        PopupMessage.getUniquePopupMessages(),
//        ElementsFunctionUtils.getPopupStandartMessages(),
//        ElementsFunctionUtils.getRequiredPathMessages(),
//        ElementsFunctionUtils.getComboMessages());
//                                            IsIndicatorMessage.addIndicatorMessage(customTab);
//
//                                            LOGGER.log(Level.SEVERE, "Process failed with alert: " +id + "  stepid: " + stepId);
//                                        }
//
//                                                }else if(kpiTypeId == 16641793815766L){
//        System.out.println("stepId: " + stepId + "   sideBarText: "+ sideBarText + "  headerTabText: " + headerTabText);
//                                    }
//                                            }else if(kpiTypeId == 0L && metaTypeId != 0L){
//        System.out.println("Metadata daraa ni hiinee");
//                                }
//
//
//                                        package testingmachine_backend.process.DTO;
//
//import lombok.Getter;
//import lombok.Setter;
//import testingmachine_backend.indicator.IndicatorCustomTab;
//
//import java.util.List;
//
//@Setter
//@Getter
//public class ProcessMessageStatusDTO {
//
//    private String moduleName;
//    private String metaDataId;
//    private String metaDataCode;
//    private String metaDataName;
//    private String status;
//    private String messageText;
//    private String jsonId;
//    private List<ProcessLogDTO> processLogDTO;
//    private List<EmptyDataDTO> emptyDataDTO;
//    private List<PopupMessageDTO> popupMessageDTO;
//    private List<PopupStandardFieldsDTO> popupStandardFieldsDTO;
//    private List<RequiredPathDTO> requiredPathDTO;
//    private List<ComboMessageDTO> comboMessageDTO;
//    private List<IndicatorCustomTab> indicatorCustomTab;
//
//    public ProcessMessageStatusDTO(String moduleName, String metaDataId, String metaDataCode, String metaDataName, String status, String messageText,
//                                   String jsonId, List<ProcessLogDTO> processLogDTO, List<EmptyDataDTO> emptyDataDTO, List<PopupMessageDTO> popupMessageDTO,
//                                   List<PopupStandardFieldsDTO> popupStandardFieldsDTO, List<RequiredPathDTO> requiredPathDTO, List<ComboMessageDTO> comboMessageDTO,
//                                   List<IndicatorCustomTab> indicatorCustomTab) {
//        this.moduleName = moduleName;
//        this.metaDataId = metaDataId;
//        this.metaDataCode = metaDataCode;
//        this.metaDataName = metaDataName;
//        this.status = status;
//        this.messageText = messageText;
//        this.jsonId = jsonId;
//        this.processLogDTO = processLogDTO;
//        this.emptyDataDTO = emptyDataDTO;
//        this.popupMessageDTO = popupMessageDTO;
//        this.popupStandardFieldsDTO = popupStandardFieldsDTO;
//        this.requiredPathDTO = requiredPathDTO;
//        this.comboMessageDTO = comboMessageDTO;
//        this.indicatorCustomTab = indicatorCustomTab;
//    }
//}
//
//package testingmachine_backend.indicator;
//
//
//import lombok.Getter;
//import lombok.Setter;
//import testingmachine_backend.process.DTO.*;
//
//        import java.util.List;
//
//@Getter
//@Setter
//public class IndicatorCustomTab {
//
//    private String parentId;
//    private String indicatorId;
//    private String customTabName;
//    private String sideBarName;
//    private String sideBarType;
//    private String status;
//    private String messageText;
//    private String jsonId;
//    private List<ProcessLogDTO> processLogDTO;
//    private List<EmptyDataDTO> emptyDataDTO;
//    private List<PopupMessageDTO> popupMessageDTO;
//    private List<PopupStandardFieldsDTO> popupStandardFieldsDTO;
//    private List<RequiredPathDTO> requiredPathDTO;
//    private List<ComboMessageDTO> comboMessageDTO;
//
//    public IndicatorCustomTab(String parentId, String indicatorId, String customTabName, String sideBarName, String sideBarType, String status, String messageText, String jsonId
//            ,List<ProcessLogDTO> processLogDTO, List<EmptyDataDTO> emptyDataDTO, List<PopupMessageDTO> popupMessageDTO,
//                              List<PopupStandardFieldsDTO> popupStandardFieldsDTO, List<RequiredPathDTO> requiredPathDTO,
//                              List<ComboMessageDTO> comboMessageDTO
//    ) {
//        this.parentId = parentId;
//        this.indicatorId = indicatorId;
//        this.customTabName = customTabName;
//        this.sideBarName = sideBarName;
//        this.sideBarType = sideBarType;
//        this.status = status;
//        this.messageText = messageText;
//        this.jsonId = jsonId;
//        this.processLogDTO = processLogDTO;
//        this.emptyDataDTO = emptyDataDTO;
//        this.popupMessageDTO = popupMessageDTO;
//        this.popupStandardFieldsDTO = popupStandardFieldsDTO;
//        this.requiredPathDTO = requiredPathDTO;
//        this.comboMessageDTO = comboMessageDTO;
//    }
//}
//
//
//
//Хэрхэн indicatorId эсвэл metaDataId таарч байвал эдгээрийг private List<ProcessLogDTO> processLogDTO;
//private List<EmptyDataDTO> emptyDataDTO;
//private List<PopupMessageDTO> popupMessageDTO;
//private List<PopupStandardFieldsDTO> popupStandardFieldsDTO;
//private List<RequiredPathDTO> requiredPathDTO;
//private List<ComboMessageDTO> comboMessageDTO;
//тухайн датаны detail болгож оруулах вэ?
//
//Одоо бол бүх дата дээр орж ирээд байгаа
//        Жишээ :
//        {
//        "jsonId": "ff654fd2-ff68-4b16-afbf-7d2a786849f8",
//        "processDetails": [
//        {
//        "moduleName": "testIndicator",
//        "metaDataId": "204597849",
//        "metaDataCode": "1",
//        "metaDataName": "indicator",
//        "status": "success",
//        "messageText": "Амжилттай хадгалагдлаа",
//        "jsonId": "ff654fd2-ff68-4b16-afbf-7d2a786849f8",
//        "processLogDTO": [
//        {
//        "moduleName": "Нотлох",
//        "metaDataId": "188014007",
//        "logType": "error",
//        "messageText": "Uncaught ReferenceError: errrTestExpression is not defined",
//        "jsonId": "ff654fd2-ff68-4b16-afbf-7d2a786849f8"
//        }
//        ],
//        "emptyDataDTO": [],
//        "popupMessageDTO": [],
//        "popupStandardFieldsDTO": [],
//        "requiredPathDTO": [
//        {
//        "moduleName": "Байгууллагын бүртгэл",
//        "metaDataId": "187900809",
//        "logType": "required",
//        "messageText": "Path: C139\"",
//        "jsonId": "ff654fd2-ff68-4b16-afbf-7d2a786849f8"
//        },
//        {
//        "moduleName": "Байгууллагын бүртгэл",
//        "metaDataId": "187900809",
//        "logType": "required",
//        "messageText": "Path: C13\"",
//        "jsonId": "ff654fd2-ff68-4b16-afbf-7d2a786849f8"
//        }
//        ],
//        "comboMessageDTO": [],
//        "indicatorCustomTab": [
//        {
//        "parentId": "204597849",
//        "indicatorId": "189852113",
//        "customTabName": "Гэрчилгээний мэдээлэл",
//        "sideBarName": "ЭГ гэрчилгээний мэдээлэл",
//        "sideBarType": "METHOD",
//        "status": "success",
//        "messageText": "Амжилттай хадгалагдлаа.",
//        "jsonId": "ff654fd2-ff68-4b16-afbf-7d2a786849f8",
//        "processLogDTO": [],
//        "emptyDataDTO": [],
//        "popupMessageDTO": [],
//        "popupStandardFieldsDTO": [],
//        "requiredPathDTO": [],
//        "comboMessageDTO": []
//        },
//        {
//        "parentId": "204597849",
//        "indicatorId": "189852170",
//        "customTabName": "Гэрчилгээний мэдээлэл",
//        "sideBarName": "УБ гэрчилгээний мэдээлэл",
//        "sideBarType": "METHOD",
//        "status": "success",
//        "messageText": "Амжилттай хадгалагдлаа.",
//        "jsonId": "ff654fd2-ff68-4b16-afbf-7d2a786849f8",
//        "processLogDTO": [],
//        "emptyDataDTO": [],
//        "popupMessageDTO": [],
//        "popupStandardFieldsDTO": [],
//        "requiredPathDTO": [],
//        "comboMessageDTO": []
//        },
//        {
//        "parentId": "204597849",
//        "indicatorId": "189888874",
//        "customTabName": "Нэмэлт мэдээлэл",
//        "sideBarName": "Хөтөлбөрийн мэдээлэл",
//        "sideBarType": "METHOD",
//        "status": "success",
//        "messageText": "Амжилттай хадгалагдлаа.",
//        "jsonId": "ff654fd2-ff68-4b16-afbf-7d2a786849f8",
//        "processLogDTO": [],
//        "emptyDataDTO": [],
//        "popupMessageDTO": [],
//        "popupStandardFieldsDTO": [],
//        "requiredPathDTO": [],
//        "comboMessageDTO": []
//        },
//        {
//        "parentId": "204597849",
//        "indicatorId": "189888200",
//        "customTabName": "Нэмэлт мэдээлэл",
//        "sideBarName": "Сургуулийн алсын хараа зорилго, зорилт",
//        "sideBarType": "METHOD",
//        "status": "success",
//        "messageText": "Амжилттай хадгалагдлаа.",
//        "jsonId": "ff654fd2-ff68-4b16-afbf-7d2a786849f8",
//        "processLogDTO": [],
//        "emptyDataDTO": [],
//        "popupMessageDTO": [],
//        "popupStandardFieldsDTO": [],
//        "requiredPathDTO": [],
//        "comboMessageDTO": []
//        },
//        {
//        "parentId": "204597849",
//        "indicatorId": "189887852",
//        "customTabName": "Нэмэлт мэдээлэл",
//        "sideBarName": "Шаардлагатай ажилтны мэдээлэл",
//        "sideBarType": "METHOD",
//        "status": "success",
//        "messageText": "Амжилттай хадгалагдлаа.",
//        "jsonId": "ff654fd2-ff68-4b16-afbf-7d2a786849f8",
//        "processLogDTO": [],
//        "emptyDataDTO": [],
//        "popupMessageDTO": [],
//        "popupStandardFieldsDTO": [],
//        "requiredPathDTO": [],
//        "comboMessageDTO": []
//        },
//        {
//        "parentId": "204597849",
//        "indicatorId": "187900809",
//        "customTabName": "Нэмэлт мэдээлэл",
//        "sideBarName": "Байгууллагын бүртгэл",
//        "sideBarType": "METHOD",
//        "status": "info",
//        "messageText": "* Заавал бөглөх талбаруудыг бөглөнө үү",
//        "jsonId": "ff654fd2-ff68-4b16-afbf-7d2a786849f8",
//        "processLogDTO": [],
//        "emptyDataDTO": [],
//        "popupMessageDTO": [],
//        "popupStandardFieldsDTO": [],
//        "requiredPathDTO": [
//        {
//        "moduleName": "Байгууллагын бүртгэл",
//        "metaDataId": "187900809",
//        "logType": "required",
//        "messageText": "Path: C139\"",
//        "jsonId": "ff654fd2-ff68-4b16-afbf-7d2a786849f8"
//        },
//        {
//        "moduleName": "Байгууллагын бүртгэл",
//        "metaDataId": "187900809",
//        "logType": "required",
//        "messageText": "Path: C13\"",
//        "jsonId": "ff654fd2-ff68-4b16-afbf-7d2a786849f8"
//        }
//        ],
//        "comboMessageDTO": []
//        },
//        {
//        "parentId": "204597849",
//        "indicatorId": "189888577",
//        "customTabName": "Нэмэлт мэдээлэл",
//        "sideBarName": "Чанарын баталгаажуулалтын мэдээлэл",
//        "sideBarType": "METHOD",
//        "status": "success",
//        "messageText": "Амжилттай хадгалагдлаа.",
//        "jsonId": "ff654fd2-ff68-4b16-afbf-7d2a786849f8",
//        "processLogDTO": [],
//        "emptyDataDTO": [],
//        "popupMessageDTO": [],
//        "popupStandardFieldsDTO": [],
//        "requiredPathDTO": [
//        {
//        "moduleName": "Байгууллагын бүртгэл",
//        "metaDataId": "187900809",
//        "logType": "required",
//        "messageText": "Path: C139\"",
//        "jsonId": "ff654fd2-ff68-4b16-afbf-7d2a786849f8"
//        },
//        {
//        "moduleName": "Байгууллагын бүртгэл",
//        "metaDataId": "187900809",
//        "logType": "required",
//        "messageText": "Path: C13\"",
//        "jsonId": "ff654fd2-ff68-4b16-afbf-7d2a786849f8"
//        }
//        ],
//        "comboMessageDTO": []
//        },
//        {
//        "parentId": "204597849",
//        "indicatorId": "188014007",
//        "customTabName": "өтөлбөрийн мэдээлэл",
//        "sideBarName": "Нотлох",
//        "sideBarType": "METHOD",
//        "status": "success",
//        "messageText": "Амжилттай хадгалагдлаа.",
//        "jsonId": "ff654fd2-ff68-4b16-afbf-7d2a786849f8",
//        "processLogDTO": [
//        {
//        "moduleName": "Нотлох",
//        "metaDataId": "188014007",
//        "logType": "error",
//        "messageText": "Uncaught ReferenceError: errrTestExpression is not defined",
//        "jsonId": "ff654fd2-ff68-4b16-afbf-7d2a786849f8"
//        }
//        ],
//        "emptyDataDTO": [],
//        "popupMessageDTO": [],
//        "popupStandardFieldsDTO": [],
//        "requiredPathDTO": [
//        {
//        "moduleName": "Байгууллагын бүртгэл",
//        "metaDataId": "187900809",
//        "logType": "required",
//        "messageText": "Path: C139\"",
//        "jsonId": "ff654fd2-ff68-4b16-afbf-7d2a786849f8"
//        },
//        {
//        "moduleName": "Байгууллагын бүртгэл",
//        "metaDataId": "187900809",
//        "logType": "required",
//        "messageText": "Path: C13\"",
//        "jsonId": "ff654fd2-ff68-4b16-afbf-7d2a786849f8"
//        }
//        ],
//        "comboMessageDTO": []
//        }
//        ]
//        }
//        ]
//        }
//