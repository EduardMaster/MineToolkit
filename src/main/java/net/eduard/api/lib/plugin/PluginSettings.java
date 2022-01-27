package net.eduard.api.lib.plugin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PluginSettings {
    private boolean debug = true;
    private boolean autoSave = false;

    public boolean isAutoSave() {
        return autoSave;
    }

    private int autoSaveSeconds = 60;
    private boolean autoBackup = false;

    public boolean isAutoBackup() {
        return autoBackup;
    }

    private int autoBackupSeconds = 60*10;

    public int getAutoBackupSeconds() {
        return autoBackupSeconds;
    }

    public int getAutoSaveSeconds() {
        return autoSaveSeconds;
    }

    public long getLastBackup() {
        return lastBackup;
    }

    private long lastBackup = 0L;
    private transient long lastSave = 0L;

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
