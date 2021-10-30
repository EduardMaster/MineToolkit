package net.eduard.api.lib.plugin;

import lombok.Getter;
import lombok.Setter;
import net.eduard.api.lib.config.StorageType;

@Getter
@Setter
public class PluginSettings {
    private StorageType storeType = StorageType.YAML;
    private boolean debug = true;
    private boolean autoSave = false;

    public boolean isAutoSave() {
        return autoSave;
    }

    private int autoSaveSeconds = 1;
    private boolean autoBackup = false;

    public boolean isAutoBackup() {
        return autoBackup;
    }

    private int autoBackupSeconds = 1;

    public int getAutoBackupSeconds() {
        return autoBackupSeconds;
    }

    public int getAutoSaveSeconds() {
        return autoSaveSeconds;
    }

    public long getLastBackup() {
        return lastBackup;
    }

    public StorageType getStoreType() {
        return storeType;
    }

    private transient long lastBackup = 0L;
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
