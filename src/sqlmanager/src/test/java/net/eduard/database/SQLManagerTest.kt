package net.eduard.database

import net.eduard.api.lib.database.*
import net.eduard.api.lib.database.annotations.*
import java.util.*

@TableName("exemplo_parties_users")
class PartyUser(userName: String = "Eduard") {
    @ColumnPrimary
    var id: Int = 0

    @ColumnUnique
    @ColumnSize(16)
    var name = userName

    @ColumnRelation
    @ColumnName("party_id")
    var party: Party? = null

}

@TableName("exemplo_parties")
class Party(partyUser: PartyUser? = null) {
    init {
        if (partyUser != null) {
            partyUser.party = this
        }
    }

    @ColumnPrimary
    var id: Int = 0

    @ColumnRelation
    @ColumnName("creator_id")
    var createBy: PartyUser? = partyUser

    @ColumnRelation
    @ColumnName("leader_id")
    var currentLeader: PartyUser? = partyUser

    @ColumnName("creation_time")
    var creationTime = System.currentTimeMillis()
    var capacity = 5
}

@TableName("exemplo_jogadores")
data class Jogador(
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
class PlayerConta(  var name: String = "Eduard",
                    var uuid: UUID? = UUID.randomUUID()){
    override fun toString(): String {
        return "PlayerConta(name='$name', uuid=$uuid)"
    }
}

@TableName("exemplo_tabela")
class ExemploTable(){
    @ColumnPrimary
    var id = 0
    var numeroLongo = 1000L
    var boleano = true
    var floatzin = 1.0f
    var doublozin = 2.0
    var idZin = UUID.randomUUID()

    @ColumnUnique
    var conta = PlayerConta()
    override fun toString(): String {
        return "ExemploTable(id=$id, numeroLongo=$numeroLongo, boleano=$boleano, floatzin=$floatzin, doublozin=$doublozin, idZin=$idZin, conta=$conta)"
    }


}
fun othersTypes(){
    save<PlayerConta>(120) { "$name;$uuid" }
    load {
        val split = it.split(";")
        val conta = PlayerConta()
        conta.name = split[0]
        conta.uuid = UUID.fromString(split[1])
        conta
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


