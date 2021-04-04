package net.eduard.api.lib.abstraction

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import net.eduard.api.lib.modules.Extra
import net.minecraft.server.v1_8_R3.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_8_R3.CraftServer
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.lang.Exception
import java.lang.reflect.Field
import kotlin.collections.HashMap
import kotlin.collections.HashSet

/**
 * @author Eduard
 */
class Minecraft_v1_8_R3 : Minecraft() {

    override fun sendPacket(packet: Any, player: Player) {

        (player as CraftPlayer).handle.playerConnection.sendPacket(packet as Packet<*>)
    }

    override fun setHeadSkin(head: ItemStack, texture: String, signature: String) {

    }

    override fun sendParticle(
        player: Player,
        name: String,
        location: Location,
        amount: Int,
        xOffset: Float,
        yOffset: Float,
        zOffset: Float,
        speed: Float
    ) {
        val x = location.x.toFloat()
        val y = location.y.toFloat()
        val z = location.z.toFloat()
        val result = particles[name.toLowerCase()]
        if (result == null) {
            val text = "Particle not exist in this version $name"
            if (blockedMessage.contains(text)) return
            println(text)
            blockedMessage.add(text)
            return
        }
        val particula: EnumParticle = result as EnumParticle
        if (particula.f()) {
            val text = "Particle not sent because need arguments: $name"
            if (blockedMessage.contains(text)) return
            println(text)
            blockedMessage.add(text)
        } else {
            val packet: Any =
                PacketPlayOutWorldParticles(particula, true, x, y, z, xOffset, yOffset, zOffset, speed, amount)
            sendPacket(player, packet)
        }
    }

    override fun getPlayers(): Collection<Player> {
        return Bukkit.getOnlinePlayers()
    }

    override fun performRespawn(player: Player) {
        (player as CraftPlayer).handle.playerConnection
            .a(PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN))
    }

    override fun setPlayerSkin(player: Player, newSkin: String) {
        val entityPlayer: EntityPlayer = (player as CraftPlayer).handle
        val playerProfile: GameProfile = entityPlayer.profile
        playerProfile.properties.clear()
        val uuid: String = Extra.getPlayerUUIDByName(newSkin)
        val skinProperty = Extra.getSkinProperty(uuid)
        val name: String = skinProperty.get("name").asString
        val skin: String = skinProperty.get("value").asString
        val signature: String = skinProperty.get("signature").asString
        try {
            playerProfile.properties.put("textures", Property(name, skin, signature))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        respawnPlayer(player)
        reloadPlayer(player)
    }

    override fun setPlayerName(player: Player, newName: String) {
        val entityPlayer: EntityPlayer = (player as CraftPlayer).handle
        val playerProfile: GameProfile = entityPlayer.profile
        try {
            val field: Field = playerProfile.javaClass.getDeclaredField("name")
            field.isAccessible = true
            field[playerProfile] = newName
            field.isAccessible = false
            entityPlayer.javaClass.getDeclaredField("displayName").set(entityPlayer, newName)
            entityPlayer.javaClass.getDeclaredField("listName").set(entityPlayer, newName)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        respawnPlayer(player)
        addToTab(player)
    }

    override fun respawnPlayer(playerToRespawn: Player) {
        val entityPlayer: EntityPlayer = (playerToRespawn as CraftPlayer).handle
        val destroy = PacketPlayOutEntityDestroy(entityPlayer.id)
        val removePlayerInfo = PacketPlayOutPlayerInfo(
            PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
            playerToRespawn.handle
        )
        val spawn = PacketPlayOutNamedEntitySpawn(entityPlayer)
        val addPlayerInfo = PacketPlayOutPlayerInfo(
            PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
            playerToRespawn.handle
        )
        val metadata = PacketPlayOutEntityMetadata(
            entityPlayer.id,
            entityPlayer.dataWatcher, true
        )
        val headRotation = PacketPlayOutEntityHeadRotation(
            entityPlayer,
            MathHelper.d(entityPlayer.headRotation * 256.0f / 360.0f).toByte()
        )
        sendPacketsToOthers(playerToRespawn, removePlayerInfo, destroy, spawn, addPlayerInfo, metadata, headRotation)
    }

    override fun reloadPlayer(player: Player) {
        sendPacket(player,
            PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, (player as CraftPlayer).handle
            )
        )
        sendPacket(player,
            PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
                player.handle
            )
        )
        (Bukkit.getServer() as CraftServer).handle.moveToWorld(
            player.handle, player
                .handle.dimension, true, player.getLocation(),
            true
        )
    }

    override fun removeFromTab(playerRemoved: Player) {
        val removePlayerInfo = PacketPlayOutPlayerInfo(
            PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
            (playerRemoved as CraftPlayer).handle
        )
        sendPacketsToAll(removePlayerInfo)
    }

    override fun addToTab(playerToAdd: Player) {
        val addPlayerInfo = PacketPlayOutPlayerInfo(
            PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
            (playerToAdd as CraftPlayer).handle
        )
        sendPacketsToAll(addPlayerInfo)
    }

    override fun updateDisplayName(playerToAdd: Player) {
        val updateDisplayName = PacketPlayOutPlayerInfo(
            PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, (playerToAdd as CraftPlayer).handle
        )
        sendPacketsToAll(updateDisplayName)
    }

    override fun getItemNBT(item: ItemStack): Any {
        val nmsItem: net.minecraft.server.v1_8_R3.ItemStack = CraftItemStack.asNMSCopy(item)
        var nbt: NBTTagCompound
        if (nmsItem.getTag().also { nbt = it } == null) nmsItem.setTag(nbt)
        return nbt
    }

    override fun setItemNBT(item: ItemStack, nbt: Any): ItemStack {
        val nmsItem: net.minecraft.server.v1_8_R3.ItemStack = CraftItemStack.asNMSCopy(item)
        nmsItem.setTag(nbt as NBTTagCompound?)
        return CraftItemStack.asCraftMirror(nmsItem)
    }

    override fun disableAI(entity: Entity) {
        TODO("Not yet implemented")
    }

    override fun sendActionBar(player: Player, message: String) {
        val packetPlayOutChat = PacketPlayOutChat(ChatComponentText(message), 2.toByte())
        sendPacket(packetPlayOutChat, player)
    }

   override fun setTabList(player: Player, header: String, footer: String) {
        val packet = PacketPlayOutPlayerListHeaderFooter()
        try {
            val headerComponent: IChatBaseComponent = ChatComponentText(header)
            val headerField: Field = packet.javaClass.getDeclaredField("a")
            headerField.isAccessible = true
            headerField[packet] = headerComponent
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            val footerComponent: IChatBaseComponent = ChatComponentText(footer)
            val headerField: Field = packet.javaClass.getDeclaredField("b")
            headerField.isAccessible = true
            headerField[packet] = footerComponent
        } catch (e: Exception) {
            e.printStackTrace()
        }
        sendPacket(player, packet)
    }

    companion object {
        private val particles: MutableMap<String, Any> = HashMap()
        private val blockedMessage: MutableSet<String> = HashSet()
    }

    init {
        if (particles.isEmpty()) for (particle in EnumParticle.values()) {
            particles[particle.b().toLowerCase()] = particle
        }
    }
}