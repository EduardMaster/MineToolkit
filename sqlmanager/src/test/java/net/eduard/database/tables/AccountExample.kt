package net.eduard.database.tables

import net.eduard.api.lib.database.annotations.ColumnPrimary
import net.eduard.api.lib.database.annotations.ColumnUnique
import net.eduard.api.lib.database.annotations.TableName
import net.eduard.database.testes.SimpleUser

import java.util.*

@TableName("exemplo_contas")
class AccountExample(){
    @ColumnPrimary
    var id = 0
    var logaritimo = 1000L
    var boleano = true
    var flutuando = 1.0f
    var dublando = 2.0

    @ColumnUnique
    var conta = SimpleUser()
    override fun toString(): String {
        return "AccountExample(id=$id, conta=$conta)"
    }


}