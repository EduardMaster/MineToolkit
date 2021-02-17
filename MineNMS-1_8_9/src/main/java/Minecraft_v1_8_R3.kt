package net.eduard.api.lib.abstraction

import com.google.gson.JsonObject
import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import net.minecraft.server.v1_8_R3.*
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.craftbukkit.v1_8_R3.CraftServer
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import java.lang.Exception
import java.lang.reflect.Field
import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

/**
 * @author Eduard
 */
class Minecraft_v1_8_R3 : Minecraft() {

    override fun sendPacket(packet: Any, player: Player) {

        (player as CraftPlayer).getHandle().playerConnection.sendPacket(packet as Packet<*>)
    }

    override fun setHeadSkin(head: ItemStack, texture: String, signature: String) {

    }

    fun setInventoryName(inventory: Inventory, name: String) {

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
        return Bukkit.getOnlinePlayers();
    }

    override fun performRespawn(player: Player) {
        (player as CraftPlayer).getHandle().playerConnection
            .a(PacketPlayInClientCommand(PacketPlayInClientCommand.EnumClientCommand.PERFORM_RESPAWN))
    }

    override fun setPlayerSkin(player: Player, newSkin: String) {
        val entityPlayer: EntityPlayer = (player as CraftPlayer).getHandle()
        val playerProfile: GameProfile = entityPlayer.getProfile()
        playerProfile.getProperties().clear()
        val uuid: String = "" //Extra.getPlayerUUIDByName(newSkin)
        val skinProperty: JsonObject = JsonObject()//Extra.getSkinProperty(uuid)
        val name: String = skinProperty.get("name").getAsString()
        val skin: String = skinProperty.get("value").getAsString()
        val signature: String = skinProperty.get("signature").getAsString()
        try {
            playerProfile.getProperties().put("textures", Property(name, skin, signature))
        } catch (e: Exception) {
            e.printStackTrace()
        }
        respawnPlayer(player)
        reloadPlayer(player)
    }

    override fun setPlayerName(player: Player, newName: String) {
        val entityPlayer: EntityPlayer = (player as CraftPlayer).getHandle()
        val playerProfile: GameProfile = entityPlayer.getProfile()
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

    override fun respawnPlayer(player: Player) {
        val entityPlayer: EntityPlayer = (player as CraftPlayer).getHandle()
        val destroy = PacketPlayOutEntityDestroy(entityPlayer.getId())
        val removePlayerInfo = PacketPlayOutPlayerInfo(
            PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
            player.getHandle()
        )
        val spawn = PacketPlayOutNamedEntitySpawn(entityPlayer)
        val addPlayerInfo = PacketPlayOutPlayerInfo(
            PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
            player.getHandle()
        )
        val metadata = PacketPlayOutEntityMetadata(
            entityPlayer.getId(),
            entityPlayer.getDataWatcher(), true
        )
        val headRotation = PacketPlayOutEntityHeadRotation(
            entityPlayer,
            MathHelper.d(entityPlayer.getHeadRotation() * 256.0f / 360.0f).toByte()
        )
        sendPacketsToOthers(player, removePlayerInfo, destroy, spawn, addPlayerInfo, metadata, headRotation)
    }

    override fun reloadPlayer(player: Player) {
        sendPacket(player,
            PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, (player as CraftPlayer).getHandle()
            )
        )
        sendPacket(player,
            PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
                (player as CraftPlayer).getHandle()
            )
        )
        (Bukkit.getServer() as CraftServer).getHandle().moveToWorld(
            (player as CraftPlayer).getHandle(), (player as CraftPlayer)
                .getHandle().dimension, true, player.getLocation(),
            true
        )
    }

    override fun removeFromTab(playerRemoved: Player) {
        val removePlayerInfo = PacketPlayOutPlayerInfo(
            PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
            (playerRemoved as CraftPlayer).getHandle()
        )
        sendPacketsToAll(removePlayerInfo)
    }

    override fun addToTab(playerToAdd: Player) {
        val addPlayerInfo = PacketPlayOutPlayerInfo(
            PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER,
            (playerToAdd as CraftPlayer).getHandle()
        )
        sendPacketsToAll(addPlayerInfo)
    }

    override fun updateDisplayName(player: Player) {
        val updateDisplayName = PacketPlayOutPlayerInfo(
            PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME, (player as CraftPlayer).getHandle()
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