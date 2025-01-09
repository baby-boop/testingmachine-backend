package testingmachine_backend.process.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class ProcessMessageStatusDTO {

    private String moduleName;
    private String metaDataId;
    private String metaDataCode;
    private String metaDataName;
    private String status;
    private String messageText;
    private String jsonId;
    private List<ProcessLogDTO> processLogDTO;
    private List<EmptyDataDTO> emptyDataDTO;
    private List<PopupMessageDTO> popupMessageDTO;
    private List<PopupStandardFieldsDTO> popupStandardFieldsDTO;
    private List<RequiredPathDTO> requiredPathDTO;

    public ProcessMessageStatusDTO(String moduleName, String metaDataId, String metaDataCode, String metaDataName, String status, String messageText,
                                   String jsonId, List<ProcessLogDTO> processLogDTO, List<EmptyDataDTO> emptyDataDTO, List<PopupMessageDTO> popupMessageDTO,
                                   List<PopupStandardFieldsDTO> popupStandardFieldsDTO, List<RequiredPathDTO> requiredPathDTO) {
        this.moduleName = moduleName;
        this.metaDataId = metaDataId;
        this.metaDataCode = metaDataCode;
        this.metaDataName = metaDataName;
        this.status = status;
        this.messageText = messageText;
        this.jsonId = jsonId;
        this.processLogDTO = processLogDTO;
        this.emptyDataDTO = emptyDataDTO;
        this.popupMessageDTO = popupMessageDTO;
        this.popupStandardFieldsDTO = popupStandardFieldsDTO;
        this.requiredPathDTO = requiredPathDTO;
    }
}
