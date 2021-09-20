package net.eduard.api.lib.database

import java.sql.Timestamp
import java.util.*

val customTypes = mutableMapOf<Class<*>, CustomType<*>>()
class CustomType<T : Any>{
    var saveMethod : (T.() -> String)?= null
    var reloadMethod : (String.() -> T)? = null
    var sqlType: String = "VARCHAR"
    var sqlSize = 150
    fun withoutSize()  { sqlSize=0 }
    inline fun < reified T> register(){
        customTypes[T::class.java] = this
    }

}
inline fun <reified T : Any> customType(noinline settings : (CustomType<T>.() -> Unit)){
    customTypeRegister(T::class.java,settings)
}
fun <T : Any> customTypeRegister(clz : Class<T>,
  settings : (CustomType<T>.() -> Unit)) : CustomType<T>{
    val customType = CustomType<T>()
    customType.settings()
    customTypes[clz] = customType
    return customType
}

fun Date.toSQLDate() = java.sql.Date(time)
fun String.toSQLDate() = java.sql.Date.valueOf(this)
fun java.sql.Date.toDate() = Date(time)

fun javaTypes() {
    customType<UUID> {
        reloadMethod ={
            UUID.fromString(this)
        }
        saveMethod={
           toString()
        }
    }
    customType<Date> {
        reloadMethod ={
            toSQLDate().toDate()
        }
        saveMethod={
            toSQLDate().toString()
        }
        withoutSize()
        sqlType="DATE"
    }
    customType<java.sql.Date> {
        reloadMethod = {
            toSQLDate()
        }
        saveMethod={
            toString()
        }

        withoutSize()
        sqlType="DATE"
    }
    customType<Timestamp> {
        reloadMethod = {
            Timestamp.valueOf(this)
        }
        saveMethod={
            toString()
        }

        withoutSize()
        sqlType="TIMESTAMP"
    }


}