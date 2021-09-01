package net.eduard.api.lib.abstraction

import net.minecraft.server.v1_8_R3.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftArmorStand
import org.bukkit.entity.Player

/**
 * Criado em 28/11/2019
 * Atualizado em 07/05/2020
 *
 * @author Eduard
 * @version 1.1
 */
class Hologram_v1_8_R3 : Hologram {


    var players = mutableListOf<Player>()
    override var location: Location = Bukkit.getWorlds().first().spawnLocation
        set(value) {
            field = value
            holo?.setPosition(value.x, value.y, value.z)
        }
    var holo: EntityArmorStand? = null
        get() {
            if (field == null) {
                val nmsWorld: World = (location.world as CraftWorld).handle
                field = EntityArmorStand(nmsWorld, location.x, location.y, location.z)
                field?.apply {
                    isInvisible = true
                    isSmall = true
                    //funcao setMarker
                    n(true)
                    customName = "§b§lHolograma EduardAPI"
                    setGravity(false)
                    customNameVisible = true
                    setArms(false)
                }

            }
            return field
        }


    override var text: String = ""
        set(text) {
            field = text
            holo?.customName = text
        }
        get() = holo?.customName ?: ""


    override var playersSeeing = mutableSetOf<Player>()

    override fun toggle(player: Player) {
        if (canSee(player)) {
            hide(player)
        } else show(player)
    }

    override fun showAllIn(distance: Int) {
        for (player in Bukkit.getOnlinePlayers()) {
            if (player.world != location.world) continue
            if (player.location.distance(location) <= distance) {
                show(player)
            }

        }
    }

    override fun updateAllIn(distance: Int) {
        for (player in Bukkit.getOnlinePlayers()) {
            if (player.world != location.world) continue
            if (player.location.distance(location) <= distance) {
                update(player)
            }

        }
    }


    override fun show(player: Player) {
        Hologram.log("Mostrando para ${player.name}")
        playersSeeing.add(player)
        val pacote = PacketPlayOutSpawnEntityLiving(holo)
        Minecraft.instance.sendPacket(player, pacote)
    }


    override fun hide(player: Player) {
        Hologram.log("Escondendo de ${player.name}")
        playersSeeing.remove(player)
        if (isSpawned) {
            val pacote = PacketPlayOutEntityDestroy(id)
            Minecraft.instance.sendPacket(player, pacote)
        }
    }

    override val id: Int
        get() = holo?.id ?: -1

    override fun update(player: Player) {
        // tentativas de mandar packets de atualização da entidade
        val updateNBT = PacketPlayOutUpdateEntityNBT(holo!!.id, holo!!.nbtTag)
        val updateMetadata = PacketPlayOutEntityMetadata(holo!!.id, holo!!.dataWatcher, false)
        if (canSee(player)) {
            Hologram.log("Atualizando para $player")
            Minecraft.instance.sendPacket(player, updateMetadata)
            Minecraft.instance.sendPacket(player, updateNBT)
        } else {
            show(player)
        }
    }


    override fun hideAllIn(distance: Int) {
        for (player in Bukkit.getOnlinePlayers()) {
            if (player.world != location.world) continue
            if (player.location.distance(location) <= distance) {
                hide(player)
            }

        }
    }

    override fun spawn(local: Location) {
        this.location = local
        showAllIn(100)
    }


    override fun canSee(player: Player): Boolean {
        return player in playersSeeing
    }


    override val isSpawned get() = holo != null

    override fun move(local: Location) {

    }

}