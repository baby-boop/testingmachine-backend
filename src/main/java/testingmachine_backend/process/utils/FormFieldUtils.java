package testingmachine_backend.process.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static testingmachine_backend.process.Config.ConfigProcess.waitUtils;
import static testingmachine_backend.process.utils.ProcessPath.*;

public class FormFieldUtils {

    public static void handleElementAction(WebDriver driver, WebElement element, String classAttribute,
                                           String valueAttribute, String typeAttribute, String dataPath,
                                           String regexData, String id) {

        if (valueAttribute != null && valueAttribute.isEmpty()) {

            if (classAttribute != null && !classAttribute.isEmpty()) {
                if (isPopupField(classAttribute)) {
                    WebElement popupButton = findPopupButtonForElement(element);
                    if (popupButton != null) {
                        popupButton.click();
                        waitUtils(driver);
                        clickFirstRow(driver, id);
                    }
                }
                else if (!typeAttribute.equals("hidden") && isTextField(classAttribute)) {
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
                        }
                    } else {
                        element.sendKeys("Simple test");
                    }
                }
                else if (!typeAttribute.equals("hidden") && isLongField(classAttribute)) {
                    element.sendKeys("11112222");
                }
                else if (!typeAttribute.equals("hidden") && isDescriptionField(classAttribute)) {
                    element.sendKeys("Description test");
                }
                else if (!typeAttribute.equals("hidden") && isAutoDescriptionField(classAttribute)) {
                    element.sendKeys("Auto description test");
                }
                else if (!typeAttribute.equals("hidden") && isDatetimeField(classAttribute)) {
                    element.sendKeys("2024-10-22 08:00:00");
                }
                else if (!typeAttribute.equals("hidden") && isTimeField(classAttribute)) {
                    element.sendKeys("08:00");
                }
                else if (!typeAttribute.equals("hidden") && isBigDecimalField(classAttribute)) {
                    element.sendKeys("11");
                }
                else if (!typeAttribute.equals("hidden") && isDecimalField(classAttribute)) {
                    element.sendKeys("22");
                }
                else if (!typeAttribute.equals("hidden") && isNumberField(classAttribute)) {
                    element.sendKeys("1122");
                }
                else if (!typeAttribute.equals("hidden") && isDateField(classAttribute)) {
                    element.sendKeys("2024-10-15");
                }
                else if (isTextEditorField(classAttribute)) {
                    findTextEditorInput(driver, dataPath, id);
                }
                else if (isComboField(classAttribute)) {
                    selectComboSecondOption(driver, dataPath, id);
                }
                else if (isBooleanField(classAttribute)) {
                    element.click();
                }
            }
        } else if (isRadioField(classAttribute)) {
            element.click();
        } else if (isCheckBox(typeAttribute)) {
            element.click();
        }
    }


    public static boolean isRegisterRegex(String regexData){
        return regexData.contains("^[ФЦУЖЭНГШҮЗКЪЙЫБӨАХРОЛДПЯЧЁСМИТЬВЮЕЩфцужэнгшүзкъйыбөахролдпячёсмитьвюещ]{2}[0-9]{8}$");
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
    public static boolean isTerminalRegex(String regexData){
        return regexData.contains("^[_A-Za-z0-9-\\+]{8,8}$");
    }
    public static boolean isCompanyStateRegRegex(String regexData){
        return regexData.contains("^[0-9]{10}$");
    }

    public static boolean isLongField(String classAttribute ) {
        return classAttribute.contains("longInit");
    }
    public static boolean isTextField(String classAttribute ) {
        return classAttribute.contains("stringInit");
    }
    public static boolean isDescriptionField(String classAttribute ) {
        return classAttribute.contains("descriptionInit");
    }
    public static boolean isDateField(String classAttribute ) {
        return classAttribute.contains("dateInit");
    }
    public static boolean isBooleanField(String classAttribute ) {
        return classAttribute.contains("booleanInit");
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
    //lookups
    public static boolean isComboField(String classAttribute){
        return classAttribute.contains("dropdownInput");
    }
    public static boolean isPopupField(String classAttribute){
        return classAttribute.contains("popupInit");
    }
    public static boolean isRadioField(String classAttribute){
        return classAttribute.contains("radioInit");
    }
    public static boolean isCheckBox(String typeAttribute){
        return typeAttribute.contains("checkbox");
    }
    public static boolean isNumberField(String classAttribute){
        return classAttribute.contains("numberInit");
    }
    public static boolean isTextEditorField(String classAttribute){
        return classAttribute.contains("text_editorInit");
    }
}
