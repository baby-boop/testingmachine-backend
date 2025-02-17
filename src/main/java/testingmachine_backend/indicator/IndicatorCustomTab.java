package testingmachine_backend.indicator;


import lombok.Getter;
import lombok.Setter;
import testingmachine_backend.process.DTO.*;

import java.util.List;

@Getter
@Setter
public class IndicatorCustomTab {

    private String parentId;
    private String indicatorId;
    private String customTabName;
    private String sideBarName;
    private String sideBarType;
    private String status;
    private String messageText;
    private String jsonId;
    private List<ProcessLogDTO> processLogDTO;
    private List<EmptyDataDTO> emptyDataDTO;
    private List<PopupMessageDTO> popupMessageDTO;
    private List<PopupStandardFieldsDTO> popupStandardFieldsDTO;
    private List<RequiredPathDTO> requiredPathDTO;
    private List<ComboMessageDTO> comboMessageDTO;

    public IndicatorCustomTab(String parentId, String indicatorId, String customTabName, String sideBarName, String sideBarType, String status, String messageText, String jsonId
                              ,List<ProcessLogDTO> processLogDTO, List<EmptyDataDTO> emptyDataDTO, List<PopupMessageDTO> popupMessageDTO,
                              List<PopupStandardFieldsDTO> popupStandardFieldsDTO, List<RequiredPathDTO> requiredPathDTO,
                              List<ComboMessageDTO> comboMessageDTO
    ) {
        this.parentId = parentId;
        this.indicatorId = indicatorId;
        this.customTabName = customTabName;
        this.sideBarName = sideBarName;
        this.sideBarType = sideBarType;
        this.status = status;
        this.messageText = messageText;
        this.jsonId = jsonId;
        this.processLogDTO = processLogDTO;
        this.emptyDataDTO = emptyDataDTO;
        this.popupMessageDTO = popupMessageDTO;
        this.popupStandardFieldsDTO = popupStandardFieldsDTO;
        this.requiredPathDTO = requiredPathDTO;
        this.comboMessageDTO = comboMessageDTO;
    }
}
