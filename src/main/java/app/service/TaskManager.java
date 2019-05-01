package app.service;

import app.model.ApplicationExecutionSettings;
import app.model.ProtectionCustomSetting;
import lib.Compressor;
import lib.Decoder;
import lib.Decompressor;
import lib.Encoder;
import lib.models.CompressionResult;
import lib.services.Indexer;
import lib.services.Intoxicator;

import java.io.File;
import java.nio.file.Files;
import java.util.BitSet;

public class TaskManager {
    public static String runApplicationWithSettings(ApplicationExecutionSettings settings) throws Error {
        validatePath(settings.getSourcePath(), "Invalid Source Path");
        validatePath(settings.getOutputPath(), "Invalid Output Path");
        long startTime = System.nanoTime();
        try {
            File sourceFile = new File(settings.getSourcePath());
            byte[] dataBytes = Files.readAllBytes(sourceFile.toPath());
            Indexer.buildIndices(); // TODO: This should be done automatically
            switch (settings.getOperations()) {
                case PROTECT: {
                    dataBytes = protectData(
                            dataBytes,
                            settings.getProtectionLevel(),
                            settings.getProtectionCustomSetting());
                    break;
                }
                case UNLOCK: {
                    dataBytes = unlockData(
                            dataBytes,
                            settings.getProtectionLevel(),
                            settings.getProtectionCustomSetting()
                    );
                    break;
                }
                case COMPRESS: {
                    dataBytes = compressData(dataBytes);
                    break;
                }
                case DECOMPRESS: {
                    dataBytes = decompressData(dataBytes);
                    break;
                }
                case PROTECT_AND_COMPRESS: {
                    dataBytes = protectData(compressData(dataBytes),
                            settings.getProtectionLevel(),
                            settings.getProtectionCustomSetting()
                    );
                    break;
                }
                case UNLOCK_AND_DECOMPRESS: {
                    dataBytes = decompressData(unlockData(
                            dataBytes,
                            settings.getProtectionLevel(),
                            settings.getProtectionCustomSetting()
                    ));
                    break;
                }
            }
            // TODO: Add file writting

            return "Operation(s) finished successfully in " + ((System.nanoTime() - startTime) / 10000) + " ms.";
        } catch (Exception e) {
            throw new Error(e.getMessage());
        }
    }


    private static byte[] protectData(byte[] dataBytes, int protectionLevel, ProtectionCustomSetting customSetting) {
        BitSet dataBits = BitSet.valueOf(dataBytes);
        BitSet outputBits = Encoder.encode(dataBits, protectionLevel);
        if (customSetting == ProtectionCustomSetting.ADD_RANDOM_ERROR) {
            Intoxicator.flipRandomBit(outputBits);
        }
        return outputBits.toByteArray();
    }

    private static byte[] unlockData(byte[] dataBytes, int protectionLevel, ProtectionCustomSetting customSetting) {
        BitSet dataBits = BitSet.valueOf(dataBytes);
        boolean correct = customSetting == ProtectionCustomSetting.CORRECT_ERRORS;
        BitSet outputBits = Decoder.decode(dataBits, protectionLevel, correct);
        return outputBits.toByteArray();
    }

    private static byte[] compressData(byte[] dataBytes) throws Exception {
        return Compressor.compress(dataBytes).toByteArray();
    }

    private static byte[] decompressData(byte[] dataBytes) throws Exception {
        CompressionResult cr = CompressionResult.fromByteArray(dataBytes);
        return Decompressor.decompress(cr);
    }

    private static void validatePath(String path, String message) {
        if (path == null || path.trim().equals("")) {
            throw new Error(message);
        }
    }


}
