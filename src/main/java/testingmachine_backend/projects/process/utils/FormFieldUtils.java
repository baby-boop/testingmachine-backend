package testingmachine_backend.projects.process.utils;

import org.openqa.selenium.*;
import testingmachine_backend.projects.process.Config.ConfigProcess;

import java.util.Objects;

import static testingmachine_backend.projects.process.Config.ConfigProcess.waitUtils;
import static testingmachine_backend.projects.process.utils.ElementsFunctionUtils.*;


public class FormFieldUtils {

    public static void handleElementAction(WebDriver driver, WebElement element, String classAttribute,
                                           String valueAttribute, String typeAttribute, String dataPath,
                                           String regexData, String required, String id, String fileName, String jsonId, String indicatorType) {
        waitUtils(driver);
        consoleLogChecker(driver, id, fileName, jsonId);
        if (valueAttribute != null && valueAttribute.isEmpty() ) {
            handleElementSubAction(driver, element, classAttribute, dataPath, regexData, required, id, fileName, jsonId, indicatorType);}
        else if (isRadioField(classAttribute)) {
            if (!element.findElement(By.xpath("..")).getAttribute("class").contains("checked")) {
                element.click();
            }
        }else if(isBigDecimalField(classAttribute) && valueAttribute.contains("0.0")) {
            element.sendKeys("11");
        }
        else if (isCheckBox(typeAttribute) ) {
            if (!element.findElement(By.xpath("..")).getAttribute("class").contains("checked")) {
                element.click();
            }
        }
    }

    private static void handleElementSubAction(WebDriver driver, WebElement element, String classAttribute, String dataPath, String regexData, String required, String id, String fileName, String jsonId, String indicatorType) {
        if (classAttribute != null && !classAttribute.isEmpty()) {

            if (isPopupField(classAttribute)) {
                waitUtils(driver);
                findElementWithPopup(driver, element, dataPath, required, id, fileName, jsonId);
            }  else {
                handleFieldByType(element, classAttribute, regexData, driver, dataPath, required, id, fileName, jsonId, indicatorType);
            }
        }

    }

    private static void handleFieldByType(WebElement element, String classAttribute, String regexData,
                                          WebDriver driver, String dataPath, String required, String id, String fileName, String jsonId, String indicatorType) {
        if (isStringField(classAttribute)) {
            sendKeysBasedOnRegex(element, regexData);
        } else if (isLongField(classAttribute)) {
            element.sendKeys("12");
        } else if (isFileField(classAttribute)) {
//            element.sendKeys("C:\\Users\\batde\\Downloads\\pngForTest.png");
//            element.sendKeys("C:\\pngForTest.png");
//            element.sendKeys("src/main/java/testingmachine_backend/config/pngForTest.png");

        } else if (isIntegerField(classAttribute)) {
            element.sendKeys("2025");
        } else if (isDescriptionField(classAttribute)) {
            element.sendKeys("Description test");
        } else if (isAutoDescriptionField(classAttribute)) {
            element.sendKeys("Auto description test");
        } else if (isDatetimeField(classAttribute)) {
            element.sendKeys(ConfigProcess.DateUtils.getCurrentDateTime());
            element.sendKeys(Keys.ENTER);
        } else if (isTimeField(classAttribute)) {
            element.sendKeys("08:00");
        } else if (isBigDecimalField(classAttribute)) {
            element.sendKeys("11");
        } else if (isDecimalField(classAttribute)) {
            element.sendKeys("22");
        } else if (isNumberField(classAttribute)) {
            element.sendKeys("12");
        } else if (isDateField(classAttribute)) {
            element.sendKeys(ConfigProcess.DateUtils.getCurrentDate());
        } else if (isPayrollExpressionField(classAttribute)) {
            element.sendKeys("expressionPayrollCheck");
        } else if (isExpressionEditorField(classAttribute)) {
            element.sendKeys("expressionEditorCheck");
        } else if (isPasswordField(classAttribute)) {
            element.sendKeys("2024");
        } else if (isTextEditorField(classAttribute)) {
            findTextEditorInput(driver, dataPath, id);
        } else if (isComboField(classAttribute)) {
//            if(!dataPath.equals("C258.C3")){
                comboboxFunction(driver, dataPath, required, id, fileName, jsonId, indicatorType);
//            }
        }else if (isComboGridField(classAttribute)) {
            comboGridFunction(driver, element, dataPath, id , fileName);
        }else if (isIconField(classAttribute)) {
            iconFirstField( element, dataPath, id, fileName);
        }else if (isClobField(classAttribute)) {
            element.sendKeys("test");
        }
    }

    public static boolean isRegisterRegex(String regexData){
        return regexData.contains("^[ФЦУЖЭНГШҮЗКЪЙЫБӨАХРОЛДПЯЧЁСМИТЬВЮЕЩфцужэнгшүзкъйыбөахролдпячёсмитьвюещ]{2}[0-9]{8}$");
    }
    public static boolean isMongolianRegex(String regexData){
        return regexData.contains("^[ФЦУЖЭНГШҮЗКЪЙЫБӨАХРОЛДПЯЧЁСМИТЬВЮЕЩфцужэнгшүзкъйыбөахролдпячёсмитьвюещ ,.-]{1,1024}$");
    }
    public static boolean isEnglishRegex(String regexData){
        return regexData.contains("^[QWERTYUIOPASDFGHJKLZXCVBNMqwertyuiopasdfghjklzxcvbnm -]{1,256}$");
    }
    public static boolean isPhoneRegex(String regexData){
        return regexData.contains("^[0-9]{1}[0-9]{1}[0-9]{1}[0-9]{1}[0-9]{1}[0-9]{1}[0-9]{1}[0-9]{1}$");
    }
    public static boolean isEmailRegex(String regexData){
        return regexData.contains("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
    }
    public static boolean isCompanyRegex(String regexData){
        return regexData.contains("^[0-9]{7}$");
    }
    public static boolean isLengthRegex(String regexData){
        return regexData.contains("^[0-9]{4}$");
    }
    public static boolean isTerminalRegex(String regexData){
        return regexData.contains("^[_A-Za-z0-9-\\+]{8,8}$");
    }
    public static boolean isCompanyStateRegRegex(String regexData){
        return regexData.contains("^[0-9]{10}$");
    }

    public static boolean isLongField(String classAttribute ) {
        return classAttribute.contains("longInit");
    }
    public static boolean isStringField(String classAttribute ) {
        return classAttribute.contains("stringInit");
    }
    public static boolean isIntegerField(String classAttribute ) {
        return classAttribute.contains("integerInit");
    }
    public static boolean isFileField(String classAttribute ) {return classAttribute.contains("fileInit");}
    public static boolean isDescriptionField(String classAttribute ) {
        return classAttribute.contains("descriptionInit");
    }
    public static boolean isDateField(String classAttribute ) {
        return classAttribute.contains("dateInit");
    }
    public static boolean isAutoDescriptionField(String classAttribute ) {
        return classAttribute.contains("description_autoInit");
    }
    public static boolean isTimeField(String classAttribute ) {
        return classAttribute.contains("timeInit");
    }
    public static boolean isDecimalField(String classAttribute ) {
        return classAttribute.contains("decimalInit");
    }
    public static boolean isBigDecimalField(String classAttribute ) {
        return classAttribute.contains("bigdecimalInit");
    }
    public static boolean isDatetimeField(String classAttribute ) {
        return classAttribute.contains("datetimeInit");
    }
    public static boolean isPayrollExpressionField(String classAttribute ) {
        return classAttribute.contains("payroll_expressionInit");
    }
    public static boolean isExpressionEditorField(String classAttribute ) {
        return classAttribute.contains("expression_editorInit");
    }
    public static boolean isPasswordField(String classAttribute ) {
        return classAttribute.contains("passwordInit");
    }
    public static boolean isIconField(String classAttribute ) {
        return classAttribute.contains("iconInit");
    }
    public static boolean isComboGridField(String classAttribute ) { return classAttribute.contains("combogridInit"); }

    //lookups
    public static boolean isComboField(String classAttribute){
        return classAttribute.contains("dropdownInput");
    }
    public static boolean isPopupField(String classAttribute){
        return classAttribute.contains("popupInit");
    }
    public static boolean isRadioField(String classAttribute){
        return classAttribute.contains("radioInit");
//        return classAttribute.contains("radio");
    }
    public static boolean isCheckBox(String typeAttribute){
        return typeAttribute.contains("checkbox");
    }
    public static boolean isNumberField(String classAttribute){
        return classAttribute.contains("numberInit");
    }
    public static boolean isClobField(String classAttribute){
        return classAttribute.contains("clobInit");
    }
    public static boolean isTextEditorField(String classAttribute){
        return classAttribute.contains("text_editorInit");
    }

    //Regex
    public static void sendKeysBasedOnRegex(WebElement element, String regexData){
        if (regexData != null && !regexData.isEmpty()) {
            if (isRegisterRegex(regexData)) {
                element.sendKeys("АА03021838");
            } else if (isPhoneRegex(regexData)) {
                element.sendKeys("99110011");
            } else if (isEmailRegex(regexData)) {
                element.sendKeys("test@gmail.com");
            } else if (isCompanyRegex(regexData)){
                element.sendKeys("7777771");
            } else if (isTerminalRegex(regexData)){
                element.sendKeys("11000110");
            } else if (isCompanyStateRegRegex(regexData)){
                element.sendKeys("1100001000");
            } else if (isMongolianRegex(regexData)){
                element.sendKeys("Тест шүү");
            } else if (isEnglishRegex(regexData)){
                element.sendKeys("test shuu");
            } else if (isLengthRegex(regexData)){
                element.sendKeys("2024");
            }
        } else {
            element.sendKeys("12");
        }
    }
}
