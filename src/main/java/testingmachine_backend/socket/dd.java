//import org.springframework.stereotype.Service;
//import testingmachine_backend.process.Config.*;
//import testingmachine_backend.process.Controller.ProcessController;
//import testingmachine_backend.process.DTO.ProcessLogDTO;
//import testingmachine_backend.process.DTO.ProcessMessageStatusDTO;

//import java.util.ArrayList;
//import java.util.List;
//
//@Service
//public class ProcessMessageStatusService {
//
//    private static final List<ProcessMessageStatusDTO> processMessageStatusList = new ArrayList<>();
//    private static final List<ProcessLogDTO> processLogList = new ArrayList<>();
//
//    public static void addProcessStatus(String fileName, String id, String code, String name, String status, String messageText) {
//
//        ProcessMessageStatusDTO statusDTO = new ProcessMessageStatusDTO(fileName, id, code, name, status, messageText, ProcessController.getJsonId(), processLogList);
//        processMessageStatusList.add(statusDTO);
//        JsonFileReader.saveToSingleJsonFile(statusDTO);
//
//    }
//
//    public static List<ProcessMessageStatusDTO> getProcessStatuses() {
//        return new ArrayList<>(processMessageStatusList);
//    }
//
//
//}
//
