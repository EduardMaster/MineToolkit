package net.eduard.api.lib.abstraction

import net.minecraft.server.v1_7_R4.*
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.Chest
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
import org.bukkit.entity.Creature
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Eduard
 */
class Minecraft_v1_7_R4 : Minecraft() {

    override fun setBlock(block: Block, chunk: Chunk, material: Material, data: Int, updateLightning: Boolean) {

    }
    override fun sendPacket(packet: Any, player: Player) {
        (player as CraftPlayer).handle.playerConnection
            .sendPacket(packet as Packet)
    }

    override fun forceOpen(chest: Chest, player: Player) {

    }

    override fun canAttackMelee(creature: Creature, classEntityName: String, priority: Int) {}
    override fun followTarget(creature: Creature) {
    }

    override fun followLocation(creature: Creature, location: Location) {
    }

    override fun canTarget(creature: Creature, classEntityName: String, priority: Int) {}

    override fun removeGoals(creature: Creature) {}

    override fun removeTargetGoals(creature: Creature) {}
    override fun removeFromTab(playerRemoved: Player) {
        val removePlayerInfo = PacketPlayOutPlayerInfo
            .removePlayer((playerRemoved as CraftPlayer).handle)
        sendPacketsToOthers(playerRemoved, removePlayerInfo)
    }

    override fun addToTab(playerToAdd: Player) {
        val addPlayerInfo = PacketPlayOutPlayerInfo
            .addPlayer((playerToAdd as CraftPlayer).handle)
        val updatePlayerInfo = PacketPlayOutPlayerInfo
            .updateDisplayName(playerToAdd.handle)
        sendPacketsToOthers(playerToAdd, addPlayerInfo, updatePlayerInfo)
    }

    override fun updateDisplayName(playerToAdd: Player) {}
    override fun getItemNBT(item: ItemStack): Any {
        return 1
    }

    override fun setItemNBT(item: ItemStack, nbt: Any): ItemStack {
        return item
    }

    override fun disableAI(entity: Entity) {}
    override fun respawnPlayer(playerToRespawn: Player) {
        val entityPlayer = (playerToRespawn as CraftPlayer).handle
        val destroy = PacketPlayOutEntityDestroy(*intArrayOf(entityPlayer.id))
        val spawn = PacketPlayOutNamedEntitySpawn(entityPlayer)
        val addPlayerInfo = PacketPlayOutPlayerInfo
            .addPlayer(playerToRespawn.handle)
        val metadata = PacketPlayOutEntityMetadata(
            entityPlayer.id,
            entityPlayer.dataWatcher, true
        )
        val headRotation = PacketPlayOutEntityHeadRotation(
            entityPlayer,
            MathHelper.d(entityPlayer.headRotation * 256.0f / 360.0f).toByte()
        )
        val removePlayerInfo = PacketPlayOutPlayerInfo
            .removePlayer(playerToRespawn.handle)
        sendPacketsToOthers(playerToRespawn, removePlayerInfo, addPlayerInfo, destroy, spawn, metadata, headRotation)
    }

    override fun reloadPlayer(player: Player) {}
    override fun setTabList(player: Player, header: String, footer: String) {
        TODO("Not yet implemented")
    }

    override fun performRespawn(player: Player) {
        (player as CraftPlayer).handle.playerConnection
            .a(PacketPlayInClientCommand(EnumClientCommand.PERFORM_RESPAWN))
    }

    override fun setHeadSkin(head: ItemStack, texture: String, signature: String) {}
    override fun setPlayerName(player: Player, newName: String) {}
    override fun setPlayerSkin(player: Player, newSkin: String) {}
    override fun sendActionBar(player: Player, message: String) {}
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
    }

    override fun getPlayers(): Collection<Player> {
        return mutableListOf()
    }
}