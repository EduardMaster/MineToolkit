package net.eduard.api.lib.kotlin

import java.lang.reflect.Field

fun <T> Class<T>.field(name : String, action : (Field) -> Unit){

    val field = this.getDeclaredField(name)
    field.isAccessible = true
    action.invoke(field)

}