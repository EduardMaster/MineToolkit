package net.eduard.database.testes

import java.util.*

class SimpleUser(var name: String = "Eduard",
                 var uuid: UUID? = null){


    init{
        if (uuid == null){
            uuid = UUID.nameUUIDFromBytes("FakeUUID$name".toByteArray())
        }
    }

    override fun toString(): String {
        return "PlayerConta(name='$name', uuid=$uuid)"
    }
}


inline fun testLag(actionName: String, execution: () -> Unit) {

    println(" Inicio do $actionName")
    val init = System.currentTimeMillis()
    execution.invoke()
    val end = System.currentTimeMillis()
    val dif = end - init
    println("Tempo gasto do $actionName: ${dif}ms")

}


