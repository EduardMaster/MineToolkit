package net.eduard.api.lib.config

enum class StorageType(val isSQL : Boolean = false) {
    YAML, JSON,MONGODB, SQLITE(true), MYSQL(true);
}