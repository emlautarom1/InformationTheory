package app.model;

public enum CompressionSettings {
    NOTHING,
    COMPRESS,
    DECOMPRESS;

    static public CompressionSettings fromString(String in) {
        in = in.trim().toUpperCase();
        switch (in) {
            case "COMPRESS":
                return COMPRESS;
            case "DECOMPRESS":
                return DECOMPRESS;
            default:
                return NOTHING;
        }
    }
}
