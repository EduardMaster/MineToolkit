package net.eduard.api.lib.config

import net.eduard.api.lib.database.SQLManager
import java.io.File
import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

class StorageManager(private var sqlManager: SQLManager) {
    @Target(AnnotationTarget.FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    annotation class StoreKey

    var type = StorageType.YAML
    var configBases = mutableMapOf<Class<*> , Config>()


    fun config(clz : Class<*>) : Config{
        return configBases[clz]?:
        Config(File("database/"),"storage.yml")
    }



    fun getPrimaryKey(any : Any) : Any?{
        for (field in any.javaClass.declaredFields){
           if (field.isAnnotationPresent(StoreKey::class.java))
           {
                return field.get(any);
           }
        }
        return null
    }

    fun insert(any: Any) {

        if (type.isSQL){
            sqlManager.insertData(any)
        }else{
            update(any)
        }



    }
    fun update(any : Any ){
        if (type.isSQL){
            sqlManager.updateData(any)
        }
        if (type == StorageType.YAML){
            val configBase = config(any.javaClass)
            val key = getPrimaryKey(any)
            val config= configBase.createConfig("$key.yml")
            config.set(any)
            config.saveConfig()

        }
    }

    fun <T> load(clz: Class<T>, key : Any ): T? {
        if (type.isSQL){
            return sqlManager.getData(clz, key)
        }
        if (type == StorageType.YAML){
            val configBase = config(clz)

            val config= configBase.createConfig("$key.yml")
            return config.get(clz)
        }
        return null

    }

    fun <T> loadAll(clz: Class<T>): List<T> {
        if (type.isSQL){
            return sqlManager.getAllData(clz)
        }
        if (type == StorageType.YAML){

            val list = mutableListOf<T>()
            val configBase = config(clz)

            for (config in configBase.configsChildren){
                list.add(config.get(clz))
            }
            return list
        }
        return listOf()
    }

}