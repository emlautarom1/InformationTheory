package app.service;

import app.model.ApplicationExecutionSettings;
import app.model.ProtectionCustomSetting;

class ExtensionBuilder {
    static String buildFrom(ApplicationExecutionSettings settings) {
        int protectionLevel = settings.getProtectionLevel();
        switch (settings.getOperations()) {
            case PROTECT:
            case PROTECT_AND_COMPRESS:
                if (settings.getProtectionCustomSetting() == ProtectionCustomSetting.ADD_RANDOM_ERROR) {
                    return "he" + protectionLevel;
                } else {
                    return "ha" + protectionLevel;
                }
            case UNLOCK:
            case UNLOCK_AND_DECOMPRESS:
                if (settings.getProtectionCustomSetting() == ProtectionCustomSetting.CORRECT_ERRORS) {
                    return "dh" + protectionLevel;
                } else {
                    return "de" + protectionLevel;
                }
            case COMPRESS:
                return "huf";
            default: /* DECOMPRESS case*/
                return "dhu";

        }
    }
}
