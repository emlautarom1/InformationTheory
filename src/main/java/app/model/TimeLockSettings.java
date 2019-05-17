package app.model;

import java.time.LocalDate;

public class TimeLockSettings {
    private final boolean enabled;
    private final LocalDate unlockDate;

    public TimeLockSettings(boolean enabled, LocalDate unlockDate) {
        this.enabled = enabled;
        this.unlockDate = unlockDate;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public LocalDate getUnlockDate() {
        return unlockDate;
    }
}
