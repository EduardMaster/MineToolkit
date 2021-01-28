package net.eduard.database.testes

import net.eduard.api.lib.database.*
import net.eduard.database.tables.AccountExample
import net.eduard.database.tables.PartyExample
import net.eduard.database.tables.PartyUserExample
import net.eduard.database.tables.PlayerExample
import org.junit.Test
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
class TestandoSQLManager {


    val db = DBManager()
    val sqlManager = SQLManager(db)

    init {
        save<SimpleUser>(120) { "$name;$uuid" }
        load {
            val split = it.split(";")
            val tempName = split[0]
            val tempUUID = UUID.fromString(split[1])
            SimpleUser(tempName, tempUUID)

        }

        testLag("Abrindo conexão") {
            db.openConnection()
        }
    }



    @Test
    fun testando() {

        testLag("Recriando tabelas exemplo") {
            //   sqlManager.deleteTable(AccountExample::class.java)
            sqlManager.deleteTable(AccountExample::class.java)
            sqlManager.createTable(AccountExample::class.java)
            sqlManager.clearTable(AccountExample::class.java)
            // sqlManager.deleteTable(PartyUserExample::class.java)
            // sqlManager.deleteTable(PartyExample::class.java)
            // sqlManager.createTable(PartyExample::class.java)
            // sqlManager.createTable(PartyUserExample::class.java)
        }
        testLag("Criando referencias das tabelas") {
            //sqlManager.createReferences(PartyUserExample::class.java)
            //sqlManager.createReferences(PartyExample::class.java)
        }

        testLag("Inserir dados") {
            val conta = AccountExample()
            conta.conta = SimpleUser("Eduard")
            sqlManager.insertData(conta)

            conta.conta.uuid = UUID.randomUUID()
            val findConta = sqlManager.getData(
                AccountExample::class.java, "conta", conta.conta
            )

            if (findConta == null) {

                println("Não encontrou a conta")
            } else {

                println("Encontrou conta")
            }


        }


    }


}