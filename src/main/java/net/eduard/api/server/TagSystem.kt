package net.eduard.api.server

import net.eduard.api.lib.game.Tag
import org.bukkit.entity.Player

interface TagSystem : PluginSystem {

    fun getTag(player : Player) : Tag
    fun setTag(player : Player, tag : Tag)
    fun setTagOnlyFor(player : Player, tag : Tag , players : List<Player>)
    fun setDefaultTag(player : Player)
    fun getTagsOf(player : Player) : MutableMap<Player, Tag>




}