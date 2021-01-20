package net.eduard.api.lib.plugin

import net.eduard.api.lib.config.StorageType

class PluginSettings {
    var storeType = StorageType.YAML
    var debug = true
    var autoSave = false
    var autoSaveSeconds = 1
    var autoBackup = false
    var autoBackupSeconds = 1
    @Transient
    var lastBackup = 0L
    @Transient
    var lastSave = 0L
}