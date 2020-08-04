package net.eduard.api.lib.database

import java.io.File

class StorageManager {
    var type = StorageType.YAML
    var folderBase = File("plugins/database/")
    var sqlManager = SQLManager()

    fun insert(any: Any) {

        if (type.isSQL){
            sqlManager.insertData(any)
        }
    }
    fun update(any : Any ){
        if (type.isSQL){
            sqlManager.updateData(any)
        }
    }

    fun <T> load(clz: Class<T>, key : Any ): T {
        if (type.isSQL){
            return sqlManager.getData(clz, key)
        }
        return null!!

    }

    fun <T> loadAll(clz: Class<T>): List<T> {
        if (type.isSQL){
            return sqlManager.getAllData(clz)
        }
        return listOf()
    }

}