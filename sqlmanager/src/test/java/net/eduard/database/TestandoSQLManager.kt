package net.eduard.database

import net.eduard.api.lib.database.DBManager
import net.eduard.api.lib.database.SQLManager
import net.eduard.api.lib.database.javaTypes
import org.junit.Test

class TestandoSQLManager {


    val db = DBManager()
    val sqlManager = SQLManager(db)

    init{

        javaTypes()
        othersTypes()
        testLag("Abrindo conexão") {
            db.openConnection()
        }
    }

    internal lateinit var dados: List<Jogador>
    internal lateinit var user: PartyUser
    internal lateinit var party: Party
   // @Test
    fun testando(){

        testLag("Criando tabela exemplo") {

            sqlManager.deleteTable(ExemploTable::class.java)
            sqlManager.createTable(ExemploTable::class.java)
            val valor = ExemploTable()
            sqlManager.insertData(valor)

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


}