package net.eduard.api.lib.database

import net.eduard.api.lib.database.annotations.*
import net.eduard.api.lib.database.impl.MySQLEngine
import java.util.*

@TableName("exemplo_parties_users")
internal class PartyUser(userName: String = "Eduard") {
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
internal class Party(partyUser: PartyUser? = null) {
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
internal data class Jogador(
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

    var conta = PlayerConta()
    override fun toString(): String {
        return "ExemploTable(id=$id, numeroLongo=$numeroLongo, boleano=$boleano, floatzin=$floatzin, doublozin=$doublozin, idZin=$idZin, conta=$conta)"
    }


}
fun othersTypes(){
    save<PlayerConta> { "$name;$uuid" }
    load{
        val split = it.split(";")
        val conta = PlayerConta()
        conta.name  = split[0]
        conta.uuid = UUID.fromString(split[1])
        conta
    }
}




internal lateinit var dados: List<Jogador>
internal lateinit var user: PartyUser
internal lateinit var party: Party
fun main() {
    javaTypes()
    othersTypes()
    val db = DBManager()
    val sqlManager = SQLManager(db)

    testLag("Abrindo conexão") {
        db.openConnection()
    }
    testLag("Criando tabela exemplo"){

      //  sqlManager.deleteTable(ExemploTable::class.java)
        sqlManager.createTable(ExemploTable::class.java)
        val valor = ExemploTable()
        sqlManager.insertData(valor)
        valor.doublozin =500.0;
        sqlManager.updateCache(valor)
        println(valor)
    }


   // sqlManager.deleteTable(PartyUser::class.java)
   // sqlManager.deleteTable(Party::class.java)
    /*
   testLag("Criando tabela party") {
       sqlManager.createTable(Party::class.java)
   }
   testLag("Criando tabela Party membros") {
       sqlManager.createTable(PartyUser::class.java)
   }
   testLag("Criando as conexões das 2 tabelas") {
       sqlManager.createReferences(PartyUser::class.java)
       sqlManager.createReferences(Party::class.java)
   }

  testLag("Inserindo party e usuario") {
      user = PartyUser()
      sqlManager.insertData(user)
      party = Party(user)
      sqlManager.insertData(party)


  }
  testLag("Atualizando usuario que não tinha party agora tem"){
      sqlManager.updateData(user)
  }


   testLag("Trazendo usuario e Party do mysql") {
       val user = sqlManager.getData(PartyUser::class.java, 1)!!
       println(user.party)
       val party = sqlManager.getData(Party::class.java, 1)!!
       println(party.createBy)
       sqlManager.updateReferences()
       println("-----")
       println(user.party)
       println(party.createBy)
   }
*/
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


