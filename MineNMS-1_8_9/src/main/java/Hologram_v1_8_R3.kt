package net.eduard.api.lib.abstraction
import net.minecraft.server.v1_8_R3.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld
import org.bukkit.entity.Player

/**
 * Criado em 28/11/2019
 * Atualizado em 07/05/2020
 *
 * @author Eduard
 * @version 1.1
 */
class Hologram_v1_8_R3 : Hologram {


    var players= mutableListOf<Player>()
    var location: Location? = null
    private var holo: EntityArmorStand? = null
    override var text: String = ""
        set(text) {
            field = text
            if (getHolo() != null) {
                holo!!.customName = text
                holo!!.customNameVisible = true
            }
        }
        get() = holo?.customName?: ""


    override var playersSeeing = mutableListOf<Player>()

    constructor() {}

    fun updateAll(distance: Int) {
        for (player in Bukkit.getOnlinePlayers()) {
            if (player.world == location!!.world) {
                if (player.location.distance(location) <= distance) {
                    update(player)
                } else {
                    if (playersSeeing.contains(player)) {
                        hide(player)
                    }
                }
            }
        }
    }

    constructor(loc: Location) {
        location = loc
        text = "§fHolograma"
        spawn()
    }



    override fun show(player: Player) {
        log("§aMostrando holograma1")
        playersSeeing.add(player)
        val pacote = PacketPlayOutSpawnEntityLiving(holo)
        Minecraft.instance.sendPacket(player, pacote)
    }

    fun exists(): Boolean {
        return holo != null
    }

    override fun hide(player: Player) {
        log("§cEscondendo holograma1")
        playersSeeing.remove(player)
        if (exists()) {
            val pacote = PacketPlayOutEntityDestroy(getHolo()!!.id)
            Minecraft.instance.sendPacket(player, pacote)
        }
    }

    fun update(player: Player) {
        // tentativas de mandar packets de atualização da entidade
        text = text
        //val updateNBT = PacketPlayOutUpdateEntityNBT(holo!!.id, holo!!.nbtTag)
        val updateMetadata = PacketPlayOutEntityMetadata(holo!!.id, holo!!.dataWatcher, false)


        // PacketPlayOutTitle a = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, IChatBaseComponent.ChatSerializer.a("title linha 1"));
        // PacketPlayOutTitle a = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, IChatBaseComponent.ChatSerializer.a("action bar"));
        if (playersSeeing.contains(player)) {
            log("§bAtualizando holograma2")
            //sendPacket(p, pacote);
            Minecraft.instance.sendPacket(player, updateMetadata)
        } else {
            show(player)
        }
    }

    private fun getHolo(): EntityArmorStand? {
        if (holo == null) {
            if (location != null) {
                val nmsWorld: World = (location!!.world as CraftWorld).handle
                holo = EntityArmorStand(nmsWorld, location!!.x, location!!.y, location!!.z)
                holo!!.isInvisible = true
                holo!!.isSmall = true
            }
        }
        return holo
    }

    override fun spawn(location: Location) {
        this.location = location
        spawn()
    }

    fun spawn() {
        getHolo()
        text = text
        // metodo que faz a entidade spawnar no servidor e ser contralada por ele
        // nmsWorld.addEntity(armorstand, CreatureSpawnEvent.SpawnReason.CUSTOM);
        updateAll(10)
    }

    override fun canSee(player: Player): Boolean {
        return false
    }


    override val isSpawned get() = holo != null

    override fun move(local: Location) {

    }

    companion object {
        private const val debug = false
        private fun log(msg: String) {
            if (debug) println("[Hologram_v1_8_R3] $msg")
        }
    }
}