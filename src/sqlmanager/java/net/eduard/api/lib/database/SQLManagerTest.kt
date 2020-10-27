package net.eduard.api.lib.database

import net.eduard.api.lib.database.annotations.*
import net.eduard.api.lib.database.impl.MySQLEngine
import net.eduard.api.lib.modules.Extra

lateinit var dados : List<Jogador>
fun main() {

    val db = DBManager()
    val sqlManager = SQLManager(db)

    testLag("Abrindo conexão") {
        db.openConnection()
    }
    sqlManager.deleteTable(PartyUser::class.java)
    sqlManager.deleteTable(Party::class.java)
    testLag("Criando tabela party") {
        sqlManager.createTable(Party::class.java)
    }
    testLag("Criando tabela Party membros"){
        sqlManager.createTable(PartyUser::class.java)
    }
    testLag("Criando as conexões das 2 tabelas"){
        sqlManager.createReferences(PartyUser::class.java)
    }

    /*
    testLag("Limpando a tabela") {
        sqlManager.clearTable(Jogador::class.java)
    }
    testLag("Deletando tabela") {
        sqlManager.deleteTable(Jogador::class.java)
    }
    testLag("Criando tabela") {
        sqlManager.createTable(Jogador::class.java)
    }


    val limit = 1000
    testLag("Inserindo $limit contas.") {
        for (x in 0..limit) {
            val jogador = Jogador()
            jogador.name = Extra.newKey(Extra.KeyType.LETTER, 16)
            jogador.saldo = Extra.getRandomDouble(1.0, 5000.0)
            sqlManager.insertData(jogador)
        }
    }


    testLag("Trazendo $limit dados") {
        dados = sqlManager.getAllData(Jogador::class.java)
        println("Todas Contas abaixo")
        var id = 1
        for (dado in dados) {
            //println("Conta #$id: $dado")
            id++
        }

    }
    testLag("Fazendo $limit updates"){
        for (conta in dados){
            conta.saldo = Extra.getRandomDouble(1.0, 5000.0)
            sqlManager.updateData(conta)

        }
    }
    testLag("Buscando conta perto") {
        val novaConta = sqlManager.getData(Jogador::class.java, 1)
        println("Conta com ID 1# $novaConta")
    }

    testLag("Buscando conta longe") {
        val novaConta = sqlManager.getData(Jogador::class.java, limit-1)
        println("Conta com ID #${limit-1}# $novaConta")
    }


    */

}

 inline fun testLag(actionName: String, execution: () -> Unit) {
    println(" Inicio do $actionName")
    val init = System.currentTimeMillis()
    execution.invoke()
    val end = System.currentTimeMillis()
    val dif = end - init
    println("Tempo gasto do $actionName: ${dif}ms")

}
@TableName("parties_users")
class PartyUser{
    @ColumnPrimary
    var id: Int = 0
    @ColumnUnique
    @ColumnSize(16)
    var name = "Eduard"
    @ColumnRelation
    @ColumnName("party_id")
    var party : Party? = null

}
@TableName("parties")
class Party{
    @ColumnPrimary
    var id: Int = 0
    @ColumnRelation
    @ColumnName("creator_id")
    var createBy : PartyUser? = null
    @ColumnRelation
    @ColumnName("leader_id")
     var currentLeader : PartyUser? = null
    @ColumnName("creation_time")
    var creationTime = System.currentTimeMillis()
    var capacity = 5
}

@TableName("jogadores")
class Jogador(
    @ColumnUnique
    @ColumnSize(16)
    var name: String = "Edu",
    var saldo: Double = 10.0
) : Cloneable {
    @ColumnPrimary
    var id: Int = 0
    public override fun clone(): Jogador {
        return super.clone() as Jogador
    }

    override fun toString(): String {
        return "Jogador(id=$id, name='$name', saldo=$saldo)"
    }


}