package net.eduard.api.server

import net.eduard.api.lib.modules.FakePlayer
import java.util.*

interface ClanSystem {
    interface ClanData{
        val id : UUID
        var tag : String
        var name : String
        val members : List<ClanPlayerData>
        var leader : ClanPlayerData
    }
    interface ClanPlayerData {
        var player : FakePlayer
        var clan : ClanData?
    }
    fun getMember(player: FakePlayer) : ClanPlayerData
    fun getClanByName(clanName : String) : ClanData?
    fun getClanByTag(tagName : String) : ClanData?
    fun hasClan(player: FakePlayer) : Boolean
    fun hasClanByName(clanName : String) : Boolean
    fun hasClanByTag(tagName : String) : Boolean
    fun canPvP(player : FakePlayer , target : FakePlayer) : Boolean
    fun isAlly(player: FakePlayer, target : FakePlayer) : Boolean
    fun isEnemy(player: FakePlayer, target : FakePlayer) : Boolean
    fun isMember(player: FakePlayer, target : FakePlayer) : Boolean
    fun getClanName(player: FakePlayer) : String
    fun getClanTag(player: FakePlayer) : String
}