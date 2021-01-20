package net.eduard.database.tables

import net.eduard.api.lib.database.annotations.*

@TableName("exemplo_parties_users")
class PartyUserExample(userName: String = "Eduard") {
    @ColumnPrimary
    var id: Int = 0

    @ColumnUnique
    @ColumnSize(16)
    var name = userName

    @ColumnRelation
    @ColumnName("party_id")
    var party: PartyExample? = null

}