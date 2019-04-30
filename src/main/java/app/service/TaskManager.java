package app.service;

import app.model.ApplicationExecutionSettings;

public class TaskManager {
    public static String runApplicationWithSettings(ApplicationExecutionSettings settings) throws Error {
        String sourcePath = settings.getSourcePath();
        String outputPath = settings.getOutputPath();
        validatePath(sourcePath, "Invalid Source Path");
        validatePath(outputPath, "Invalid Output Path");

        return "Ok";
    }

    private static void validatePath(String path, String message) {
        if (path == null || path.trim().equals("")) {
            throw new Error(message);
        }
    }


}
