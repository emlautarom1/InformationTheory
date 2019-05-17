package app.service.timelocker;

import java.time.LocalDate;

class TimeLockedFile {
    private final boolean locked;
    private final LocalDate unlockDate;

    public TimeLockedFile(boolean locked, LocalDate unlockDate) {
        this.locked = locked;
        this.unlockDate = unlockDate;
    }

    public boolean isLocked() {
        return locked;
    }

    public LocalDate getUnlockDate() {
        return unlockDate;
    }
}
