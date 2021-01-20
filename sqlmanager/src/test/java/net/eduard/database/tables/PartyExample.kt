package net.eduard.database.tables

import net.eduard.api.lib.database.annotations.ColumnName
import net.eduard.api.lib.database.annotations.ColumnPrimary
import net.eduard.api.lib.database.annotations.ColumnRelation
import net.eduard.api.lib.database.annotations.TableName

@TableName("exemplo_parties")
class PartyExample(partyUser: PartyUserExample? = null) {
    init {
        if (partyUser != null) {
            partyUser.party = this

        }
    }

    @ColumnPrimary
    var id: Int = 0

    @ColumnRelation
    @ColumnName("creator_id")
    var createBy: PartyUserExample? = partyUser

    @ColumnRelation
    @ColumnName("leader_id")
    var currentLeader: PartyUserExample? = partyUser

    @ColumnName("creation_time")
    var creationTime = System.currentTimeMillis()
    var capacity = 5
}