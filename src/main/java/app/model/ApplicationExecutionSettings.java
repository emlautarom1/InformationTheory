package app.model;

public class ApplicationExecutionSettings {
    private final String sourcePath;
    private final String outputPath;
    private final Operations operations;
    private final int protectionLevel;
    private final ProtectionCustomSetting protectionCustomSetting;

    public ApplicationExecutionSettings(String sourcePath,
                                        String outputPath,
                                        Operations operations,
                                        int protectionLevel,
                                        ProtectionCustomSetting protectionCustomSetting) {

        this.sourcePath = sourcePath;
        this.outputPath = outputPath;
        this.operations = operations;
        this.protectionLevel = protectionLevel;
        this.protectionCustomSetting = protectionCustomSetting;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public String getSourcePath() {
        return sourcePath;
    }

    public int getProtectionLevel() {
        return protectionLevel;
    }

    public ProtectionCustomSetting getProtectionCustomSetting() {
        return protectionCustomSetting;
    }

    public Operations getOperations() {
        return operations;
    }
}
