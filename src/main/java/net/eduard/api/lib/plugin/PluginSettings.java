package net.eduard.api.lib.plugin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PluginSettings {
    public static long BACKUP_MIN_SECONDS = 60 * 60;
    private boolean debug = true;
    private boolean autoSave = false;
    private long autoSaveSeconds = 60;
    private long autoBackupSeconds = BACKUP_MIN_SECONDS;
    private long lastBackup = System.currentTimeMillis();
    private transient long lastSave = System.currentTimeMillis();

    public long getAutoBackupSeconds() {
        if (autoBackupSeconds < BACKUP_MIN_SECONDS) {
            autoBackupSeconds = BACKUP_MIN_SECONDS;
        }
        return autoBackupSeconds;
    }
    public boolean isAutoSave() {
        return autoSave;
    }
    public long getLastBackup() {
        return lastBackup;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setLastBackup(long lastBackup) {
        this.lastBackup = lastBackup;
    }

    public void setLastSave(long lastSave) {
        this.lastSave = lastSave;
    }

    public long getLastSave() {
        return lastSave;
    }
}
