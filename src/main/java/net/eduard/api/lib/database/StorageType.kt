package net.eduard.api.lib.database

enum class StorageType(val isSQL : Boolean = false) {
    YAML, JSON,MONGODB, SQLITE(true), MYSQL(true);
}