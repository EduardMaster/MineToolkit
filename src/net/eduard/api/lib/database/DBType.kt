package net.eduard.api.lib.database

enum class DBType(val isSQL : Boolean = false) {
    YAML, JSON, SQLITE(true), MYSQL(true), MONGODB;


}