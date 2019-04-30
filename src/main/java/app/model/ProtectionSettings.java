package app.model;

public enum ProtectionSettings {
    NOTHING,
    PROTECT,
    UNLOCK;

    static public ProtectionSettings fromString(String in) {
        in = in.trim().toUpperCase();
        switch (in) {
            case "PROTECT":
                return PROTECT;
            case "UNLOCK":
                return UNLOCK;
            default:
                return NOTHING;
        }
    }
}
