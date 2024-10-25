package testingmachine_backend.process.Config;

public class ProcessFieldTypes {

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
