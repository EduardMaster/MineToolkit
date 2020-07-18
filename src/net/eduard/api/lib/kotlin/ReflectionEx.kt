package net.eduard.api.lib.kotlin

import java.lang.reflect.Field

fun <T> Class<T>.field(name : String, action : (Field) -> Unit){

    val field = this.getDeclaredField(name)
    if (field == null) this.getField(name)
    if (field == null)return
    field.isAccessible = true
    action.invoke(field)

}