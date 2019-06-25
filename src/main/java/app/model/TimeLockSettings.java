package app.model;

import java.time.LocalDateTime;

public class TimeLockSettings {
    private final boolean enabled;
    private final LocalDateTime unlockDate;

    public TimeLockSettings(boolean enabled, LocalDateTime unlockDate) {
        this.enabled = enabled;
        this.unlockDate = unlockDate;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public LocalDateTime getUnlockDate() {
        return unlockDate;
    }
}
