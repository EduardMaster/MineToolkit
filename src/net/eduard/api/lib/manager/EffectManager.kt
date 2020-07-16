package net.eduard.api.lib.manager

import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.potion.PotionEffect

import net.eduard.api.lib.modules.Mine
import net.eduard.api.lib.click.PlayerEffect
import net.eduard.api.lib.game.VisualEffect
import net.eduard.api.lib.game.Explosion
import net.eduard.api.lib.game.Jump
import net.eduard.api.lib.game.SoundEffect

open class EffectManager : TimeManager(), PlayerEffect {


    @Transient
    open var CUSTOM_EFFECT: PlayerEffect? = null
    var REQUIRE_PERMISSION: String? = null
    var SEND_MESSAGE: String? = null
    val MAKE_PLAYER_COMMANDS = mutableListOf<String>()
    val MAKE_CONSOLE_COMMANDS = mutableListOf<String>()
    var TELEPORT_TO: Location? = null
    val ITEMS_TO_GIVE = mutableListOf<ItemStack>()
    var PLAY_VISUAL: VisualEffect? = null
    val APPLY_POTIONS = mutableListOf<PotionEffect>()
    var PLAY_SOUND: SoundEffect? = null
    var PLAY_JUMP: Jump? = null
    var MAKE_EXPLOSION: Explosion? = null
    var CLOSE_INVENTORY: Boolean = false
    var CLEAR_INVENTORY: Boolean = false


    override fun effect(p: Player) {

        CUSTOM_EFFECT?.effect(p)
        if (REQUIRE_PERMISSION != null)
            if (!p.hasPermission(REQUIRE_PERMISSION))
                return
        for (cmd in MAKE_CONSOLE_COMMANDS) {
            Mine.makeCommand(cmd.replace("\$player", p.name))
        }
        for (cmd in MAKE_PLAYER_COMMANDS) {
            p.performCommand(cmd.replace("\$player", p.name).replaceFirst("/".toRegex(), ""))
        }


        PLAY_SOUND?.create(p)

        SEND_MESSAGE?.apply(p::sendMessage)

        if (CLOSE_INVENTORY)
            p.closeInventory()
        if (CLEAR_INVENTORY) {
            Mine.clearInventory(p)
        }

        TELEPORT_TO?.apply{ p.teleport(TELEPORT_TO)}


        PLAY_JUMP?.create(p)


        PLAY_VISUAL?.create(p)


        p.inventory.addItem(*ITEMS_TO_GIVE.toTypedArray())


        APPLY_POTIONS.forEach { it.apply(p) }


        MAKE_EXPLOSION?.create(p)


    }

}
