package app.service.timelock;

import java.time.LocalDate;

public class TimeLocker {
    public static byte[] lock(byte[] dataBytes, LocalDate lockDate) {
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
            throw new Error("Invalid Time Lock Date.");
        }
    }

    public static byte[] unlock(byte[] dataBytes) throws Error {
        try {
            TimeLockedFile timeLockedFile = TimeLockedFile.fromByteArray(dataBytes);
            if (timeLockedFile.isLocked()) {
                LocalDate unlockDate = timeLockedFile.getUnlockDate();
                if (LocalDate.now().isAfter(unlockDate)) {
                    return timeLockedFile.getData();
                } else {
                    throw new Error("File is Time Locked until " + unlockDate.toString() + "!.");
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

    private static boolean isValidLockDate(LocalDate lockDate) {
        return LocalDate.now().isBefore(lockDate);
    }
}
