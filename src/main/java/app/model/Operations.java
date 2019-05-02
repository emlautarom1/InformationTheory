package app.model;

public enum Operations {
    PROTECT,
    UNLOCK,
    COMPRESS,
    DECOMPRESS,
    PROTECT_AND_COMPRESS,
    UNLOCK_AND_DECOMPRESS;

    public static Operations fromString(String from) {
        switch (from.trim().toUpperCase()) {
            case "PROTECT":
                return PROTECT;
            case "UNLOCK":
                return UNLOCK;
            case "COMPRESS":
                return COMPRESS;
            case "DECOMPRESS":
                return DECOMPRESS;
            case "PROTECT & COMPRESS":
                return PROTECT_AND_COMPRESS;
            default:
                return UNLOCK_AND_DECOMPRESS;
        }
    }

    public String toString() {
        switch (this) {
            case PROTECT:
                return "PROTECT";
            case UNLOCK:
                return "UNLOCK";
            case COMPRESS:
                return "COMPRESS";
            case DECOMPRESS:
                return "DECOMPRESS";
            case PROTECT_AND_COMPRESS:
                return "PROTECT AND COMPRESS";
            default:
                return "UNLOCK AND DECOMPRESS";
        }
    }
}
