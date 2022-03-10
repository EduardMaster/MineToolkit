package net.eduard.api.server

import net.eduard.api.lib.game.Tag
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard

interface TagSystem : PluginSystem {
    /**
     * Pega a tag registrada do jogador
     */
    fun getTag(player: Player): Tag

    /**
     *  Define uma tag para o jogador personalizada
     */
    fun setTag(player: Player, tag: Tag)

    fun setTagOnlyFor(player: Player, tag: Tag, players: List<Player>)

    fun getTagsOf(player: Player): MutableMap<Player, Tag>
    /**
     * Define a tag Membro para o jogador
     */
    fun setDefaultTag(player: Player)

    /**
     * Retira a tag do jogador
     */
    fun removeTag(player: Player)

    /**
     * Define uma Tag em branca sem Prefixo ou Suffixo
     */
    fun setEmptyTag(player: Player)

    /**
     * Define uma tag de acordo com o Cargo do Plugin de Permissao que o jogador esta
     *
     */
    fun setGroupTag(player: Player) : Boolean
    /**
     *
     * Define Tag do Grupo do jogador caso o grupo do jogador mudar do ultimo grupo definido
     */
    fun setPlayerTagsByGroups()

    /**
     * Aplica as tags registradas nas scoreboards dos jogadores
     */
    fun updateTags()
    /**
     * Aplica as tags registradas em uma scoreboard de um jogador
     */
    fun updateTags(scoreboard : Scoreboard)

}