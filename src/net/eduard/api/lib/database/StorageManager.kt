package net.eduard.api.lib.database

import net.eduard.api.lib.database.api.SQLEngineType
import net.eduard.api.lib.database.api.SQLManager
import java.io.File

class StorageManager {
    var storeType = DBType.YAML
    var folderBase = File("plugins/database/")
    var sqlManager = SQLManager(null, SQLEngineType.SQLITE)

    fun insert(any: Any) {
        if (storeType.isSQL){
            sqlManager.insertData(any)
        }
    }
    fun update(any : Any ){
        if (storeType.isSQL){
            sqlManager.updateData(any)
        }
    }

    fun <T> load(clz: Class<T>, key : Any ): T {
        if (storeType.isSQL){
            return sqlManager.getData(clz, key)
        }
        return null!!

    }

    fun <T> loadAll(clz: Class<T>): List<T> {
        if (storeType.isSQL){
            return sqlManager.getAllData(clz)
        }
        return listOf()
    }

}