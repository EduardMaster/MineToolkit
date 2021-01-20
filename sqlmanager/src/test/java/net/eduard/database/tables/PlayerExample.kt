package net.eduard.database.tables

import net.eduard.api.lib.database.annotations.ColumnPrimary
import net.eduard.api.lib.database.annotations.ColumnSize
import net.eduard.api.lib.database.annotations.ColumnUnique
import net.eduard.api.lib.database.annotations.TableName

@TableName("exemplo_jogadores")
data class PlayerExample(
    @ColumnUnique
    @ColumnSize(16)
    var name: String = "Edu",
    var saldo: Double = 10.0
) : Cloneable {
    @ColumnPrimary
    var id: Int = 0
    public override fun clone(): PlayerExample {
        return super.clone() as PlayerExample
    }

    override fun toString(): String {
        return "Jogador(id=$id, name='$name', saldo=$saldo)"
    }
}