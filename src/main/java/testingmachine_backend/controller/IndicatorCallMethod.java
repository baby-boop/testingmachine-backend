package testingmachine_backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import testingmachine_backend.config.SslDisableClass;
import testingmachine_backend.projects.meta.DTO.MetadataDTO;
import testingmachine_backend.projects.process.DTO.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static testingmachine_backend.config.ConfigForAll.getLocalIpAddress;
import static testingmachine_backend.projects.process.Messages.PopupMessage.PopupMessageField;
import static testingmachine_backend.projects.process.utils.ElementsFunctionUtils.*;

@Slf4j
public class IndicatorCallMethod {

    public static List<MetadataDTO> getProcessMetaDataList(String systemUrl, String username, String password,
                                                           String parentId, String indicatorId, String customTabName, String groupName, String sideBarName,
                                                           String status, String indicatorType, String messageText, String jsonId) {

        SslDisableClass.SslDisabler();

        List<MetadataDTO> metaList = new ArrayList<>();

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            // 1. Parameters JSON
            ObjectNode parametersNode = objectMapper.createObjectNode();
            parametersNode.put("indicatorId", "17431342098261");
            parametersNode.put("crudIndicatorId", "206419256");
            parametersNode.put("CUSTOMTABNAME", customTabName);
            parametersNode.put("GROUPNAME", groupName);
            parametersNode.put("SIDEBARNAME", sideBarName);
            parametersNode.put("STATUS", status);
            parametersNode.put("INDICATORTYPE", indicatorType);
            parametersNode.put("INDICATORID", indicatorId);
            parametersNode.put("PARENTID", parentId);
            parametersNode.put("PRODUCT_ID", parentId);
            parametersNode.put("JSONID", jsonId);
            parametersNode.put("MESSAGETEXT", messageText);
            parametersNode.put("URL", "http://cloud.veritech.mn  /trial/");
            parametersNode.put("PDF_URL", "http://"+ getLocalIpAddress() +":3001/result/" + jsonId);

            ArrayNode processLogArray = objectMapper.createArrayNode();
            for (ProcessLogDTO log : Optional.ofNullable(ProcessLogFields.get()).orElse(Collections.emptyList())) {
                if (indicatorId.equals(log.getMetaDataId())) {
                    ObjectNode logNode = objectMapper.createObjectNode();
                    logNode.put("ID", log.getMetaDataId());
                    logNode.put("TYPE", log.getLogType());
                    logNode.put("MESSAGETEXT", log.getMessageText());
                    processLogArray.add(logNode);
                }

            }

            ArrayNode requiredPathArray = objectMapper.createArrayNode();
            for (RequiredPathDTO required : Optional.ofNullable(RequiredPathField.get()).orElse(Collections.emptyList())) {
                if(indicatorId.equals(required.getMetaDataId())) {
                    ObjectNode requiredNode = objectMapper.createObjectNode();
                    requiredNode.put("ID", required.getMetaDataId());
                    requiredNode.put("TYPE", required.getLogType());
                    requiredNode.put("MESSAGETEXT", required.getMessageText());
                    requiredPathArray.add(requiredNode);
                }
            }

            ArrayNode emptyDataArray = objectMapper.createArrayNode();
            for (EmptyDataDTO empty : Optional.ofNullable(emptyPathField.get()).orElse(Collections.emptyList())) {
                if(indicatorId.equals(empty.getMetaDataId())) {
                    ObjectNode emptyNode = objectMapper.createObjectNode();
                    emptyNode.put("ID", empty.getMetaDataId());
                    emptyNode.put("TYPE", empty.getDataType());
                    emptyNode.put("DATAPATH", empty.getDataPath());
                    emptyDataArray.add(emptyNode);
                }
            }

            ArrayNode popupMessageArray = objectMapper.createArrayNode();
            for (PopupMessageDTO popup : Optional.ofNullable(PopupMessageField.get()).orElse(Collections.emptyList())) {
                if(indicatorId.equals(popup.getMetaDataId())) {
                    ObjectNode popupNode = objectMapper.createObjectNode();
                    popupNode.put("ID", popup.getMetaDataId());
                    popupNode.put("MESSAGETEXT", popup.getMessageText());
                    popupNode.put("DATAPATH", popup.getDataPath());
                    popupMessageArray.add(popupNode);
                }
            }

            ArrayNode popupStandartFieldsArray = objectMapper.createArrayNode();
            for (PopupStandardFieldsDTO popupField : Optional.ofNullable(PopupStandartField.get()).orElse(Collections.emptyList())) {
                if(indicatorId.equals(popupField.getMetaDataId())) {
                    ObjectNode popupFieldNode = objectMapper.createObjectNode();
                    popupFieldNode.put("ID", popupField.getMetaDataId());
                    popupFieldNode.put("TYPE", popupField.getDataType());
                    popupFieldNode.put("DATAPATH", popupField.getDataPath());
                    popupStandartFieldsArray.add(popupFieldNode);
                }
            }

            ArrayNode comboMessageArray = objectMapper.createArrayNode();
            for (ComboMessageDTO comboMessage : Optional.ofNullable(ComboMessageField.get()).orElse(Collections.emptyList())) {
                if(indicatorId.equals(comboMessage.getMetaDataId())) {
                    ObjectNode comboNode = objectMapper.createObjectNode();
                    comboNode.put("ID", comboMessage.getMetaDataId());
                    comboNode.put("MESSAGETEXT", comboMessage.getMessage());
                    comboNode.put("DATAPATH", comboMessage.getDataPath());
                    comboMessageArray.add(comboNode);
                }
            }

            parametersNode.set("PROCESSLOGDTO", processLogArray);
            parametersNode.set("REQUIREDPATHDTO", requiredPathArray);
            parametersNode.set("EMPTYDATADTO", emptyDataArray);
            parametersNode.set("POPUPMESSAGEDTO", popupMessageArray);
            parametersNode.set("POPUPSTANDARDFIELDSDTO", popupStandartFieldsArray);
            parametersNode.set("COMBOMESSAGEDTO", comboMessageArray);

            ObjectNode requestJson = objectMapper.createObjectNode();
            requestJson.put("username", username);
            requestJson.put("password", password);
            requestJson.put("command", "kpiIndicatorDataSave");
            requestJson.set("parameters", parametersNode);

            ObjectNode rootWrapper = objectMapper.createObjectNode();
            rootWrapper.set("request", requestJson);

            String payload = objectMapper.writeValueAsString(rootWrapper);

            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");

            HttpEntity<String> requestEntity = new HttpEntity<>(payload, headers);
            restTemplate.exchange(systemUrl, HttpMethod.POST, requestEntity, String.class);

            log.info("🟢 JSON Payload to Send: \n{}", payload);

        } catch (Exception e) {
            log.error("Error while calling service: ", e);
        }
        return metaList;
    }
}