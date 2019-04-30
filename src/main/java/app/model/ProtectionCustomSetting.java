package app.model;

public enum ProtectionCustomSetting {
    NOTHING,
    ADD_RANDOM_ERROR,
    CORRECT_ERRORS;

    static public ProtectionCustomSetting fromString(String in) {
        in = in.trim().toUpperCase();
        switch (in) {
            case "ADD RANDOM ERROR":
                return ADD_RANDOM_ERROR;
            case "CORRECT ERRORS":
                return CORRECT_ERRORS;
            default:
                return NOTHING;
        }
    }
}
