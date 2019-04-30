package app.model;

public class ApplicationExecutionSettings {
    private final String sourcePath;
    private final String outputPath;
    private final ProtectionSettings protectionSettings;
    private final int protectionLevel;
    private final ProtectionCustomSetting protectionCustomSetting;
    private final CompressionSettings compressionSettings;

    public ApplicationExecutionSettings(
            String sourcePath,
            String outputPath,
            ProtectionSettings protectionSettings,
            int protectionLevel,
            ProtectionCustomSetting protectionCustomSetting,
            CompressionSettings compressionSettings) {

        this.sourcePath = sourcePath;
        this.outputPath = outputPath;
        this.protectionSettings = protectionSettings;
        this.protectionLevel = protectionLevel;
        this.protectionCustomSetting = protectionCustomSetting;
        this.compressionSettings = compressionSettings;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public CompressionSettings getCompressionSettings() {
        return compressionSettings;
    }

    public ProtectionSettings getProtectionSettings() {
        return protectionSettings;
    }

    public int getProtectionLevel() {
        return protectionLevel;
    }

    public ProtectionCustomSetting getProtectionCustomSetting() {
        return protectionCustomSetting;
    }
}
