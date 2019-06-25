package app.service.timelock;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TimeLocker {
    private static final String DATE_FORMATTER = "dd/MM/yyyy HH:mm";

    public static byte[] lock(byte[] dataBytes, LocalDateTime lockDate) {
        if (isValidLockDate(lockDate)) {
            TimeLockedFile lockedFile = new TimeLockedFile(
                    true,
                    lockDate,
                    dataBytes
            );
            try {
                return lockedFile.toByteArray();
            } catch (Exception e) {
                throw new Error("Failed to Time Lock the File.");
            }
        } else {
            throw new Error("Invalid Time Lock Date or Time.");
        }
    }

    public static byte[] unlock(byte[] dataBytes) throws Error {
        try {
            TimeLockedFile timeLockedFile = TimeLockedFile.fromByteArray(dataBytes);
            if (timeLockedFile.isLocked()) {
                LocalDateTime unlockDate = timeLockedFile.getUnlockDate();
                if (LocalDateTime.now().isAfter(unlockDate)) {
                    return timeLockedFile.getData();
                } else {
                    throw new Error("File is Time Locked until "
                            + unlockDate.format(DateTimeFormatter.ofPattern(DATE_FORMATTER))
                            + "!");
                }
            } else {
                return timeLockedFile.getData();
            }
        } catch (Exception e) {
            throw new Error("Failed to read file.");
        }
    }

    public static byte[] buildUnlockedFile(byte[] dataBytes) throws Error {
        try {
            TimeLockedFile lockedFile = new TimeLockedFile(false, null, dataBytes);
            return lockedFile.toByteArray();
        } catch (Exception e) {
            throw new Error("Failed to Time Lock data.");
        }
    }

    private static boolean isValidLockDate(LocalDateTime lockDate) {
        return LocalDateTime.now().isBefore(lockDate);
    }
}
