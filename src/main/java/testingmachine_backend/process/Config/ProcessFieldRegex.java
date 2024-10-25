package testingmachine_backend.process.Config;

public class ProcessFieldRegex {

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
}
