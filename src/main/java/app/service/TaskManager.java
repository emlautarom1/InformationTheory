package app.service;

import app.model.ApplicationExecutionSettings;
import app.model.ProtectionCustomSetting;
import hamming.lib.Decoder;
import hamming.lib.Encoder;
import hamming.lib.services.Indexer;
import hamming.lib.services.Intoxicator;
import huffman.lib.Compressor;
import huffman.lib.Decompressor;
import huffman.lib.models.CompressionResult;

import java.io.File;
import java.nio.file.Files;
import java.util.BitSet;

public class TaskManager {
    public static long runApplicationWithSettings(ApplicationExecutionSettings settings) throws Error {
        validatePath(settings.getSourcePath(), "Invalid Source Path");
        validatePath(settings.getOutputPath(), "Invalid Output Path");

        long startTime = System.nanoTime();

        Indexer.buildIndices(); // TODO: This should be done automatically
        File sourceFile = new File(settings.getSourcePath());
        byte[] dataBytes;
        try {
            dataBytes = Files.readAllBytes(sourceFile.toPath());
        } catch (Exception e) {
            throw new Error("Failed to read file.");
        }
        switch (settings.getOperations()) {
            case PROTECT:
                dataBytes = protectData(
                        dataBytes,
                        settings.getProtectionLevel(),
                        settings.getProtectionCustomSetting());
                break;
            case UNLOCK:
                dataBytes = unlockData(
                        dataBytes,
                        settings.getProtectionLevel(),
                        settings.getProtectionCustomSetting()
                );
                break;
            case COMPRESS:
                dataBytes = compressData(dataBytes);
                break;
            case DECOMPRESS:
                dataBytes = decompressData(dataBytes);
                break;
            case PROTECT_AND_COMPRESS:
                dataBytes = protectData(compressData(dataBytes),
                        settings.getProtectionLevel(),
                        settings.getProtectionCustomSetting()
                );
                break;
            case UNLOCK_AND_DECOMPRESS:
                dataBytes = decompressData(unlockData(
                        dataBytes,
                        settings.getProtectionLevel(),
                        settings.getProtectionCustomSetting()
                ));
                break;
        }
        try {
            Files.write(new File(settings.getOutputPath()).toPath(), dataBytes);
        } catch (Exception e) {
            throw new Error("Failed to write file.");
        }
        return ((System.nanoTime() - startTime) / 10000);
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

    private static byte[] compressData(byte[] dataBytes) throws Error {
        try {
            return Compressor.compress(dataBytes).toByteArray();
        } catch (Exception e) {
            throw new Error("Failed to compress data");
        }
    }

    private static byte[] decompressData(byte[] dataBytes) throws Error {
        try {
            CompressionResult cr = CompressionResult.fromByteArray(dataBytes);
            return Decompressor.decompress(cr);
        } catch (Exception e) {
            throw new Error("Failed to decompress data.");
        }

    }

    private static void validatePath(String path, String message) {
        if (path == null || path.trim().equals("")) {
            throw new Error(message);
        }
    }
}
