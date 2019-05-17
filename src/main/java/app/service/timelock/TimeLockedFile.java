package app.service.timelock;

import java.io.*;
import java.time.LocalDate;

class TimeLockedFile implements Serializable {
    private final boolean locked;
    private final LocalDate unlockDate;
    private final byte[] data;

    TimeLockedFile(boolean locked, LocalDate unlockDate, byte[] data) {
        this.locked = locked;
        this.unlockDate = unlockDate;
        this.data = data;
    }

    boolean isLocked() {
        return locked;
    }

    LocalDate getUnlockDate() {
        return unlockDate;
    }

    byte[] getData() {
        return data;
    }

    byte[] toByteArray() throws Exception {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream objectStream = new ObjectOutputStream(byteStream);
        objectStream.writeObject(this);
        objectStream.flush();
        objectStream.close();
        return byteStream.toByteArray();
    }

    static TimeLockedFile fromByteArray(byte[] source) throws Exception {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(source);
        ObjectInputStream objectStream = new ObjectInputStream(byteStream);
        return (TimeLockedFile) objectStream.readObject();
    }
}
