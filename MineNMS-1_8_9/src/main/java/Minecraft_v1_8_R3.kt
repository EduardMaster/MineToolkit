package net.eduard.api.lib.abstraction

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import net.eduard.api.lib.modules.Extra
import net.minecraft.server.v1_8_R3.*
import org.bukkit.Bukkit
import org.bukkit.Chunk
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.block.Chest
import org.bukkit.craftbukkit.v1_8_R3.CraftChunk
import org.bukkit.craftbukkit.v1_8_R3.CraftServer
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld
import org.bukkit.craftbukkit.v1_8_R3.block.CraftChest
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftCreature
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.entity.Creature
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Field

/**
 * @author Eduard
 */
class Minecraft_v1_8_R3 : Minecraft() {
    override fun setBlock(block: Block, chunk: Chunk, material: Material, dataAsInt: Int, updateLightning: Boolean) {
        try {
            val chunkHandle = (chunk as CraftChunk).handle
            val worldHandle = (block.world as CraftWorld).handle
            // val chunk = worldServer.chunkProviderServer.originalGetChunkAt(x shr 4, z shr 4)
            val y = block.y
            val x = block.x
            val z = block.z
            val typeID = material.id
            val yBitShifted = (y shr 4) and 0xF
            if (yBitShifted >= 16) {
                return;
            }
            var chunkSection = chunkHandle.sections[yBitShifted]
            if (chunkSection == null) {
                chunkSection = ChunkSection(y shr 4 shl 4, !worldHandle.worldProvider.o())
                chunkHandle.sections[yBitShifted] = chunkSection
                //chunkSection = chunk.sections[y shr 4]
            }
            val xSec = x and 0xF
            val ySec = y and 0xF
            val zSec = z and 0xF

            val combined: Int = typeID + (dataAsInt shl 12)
            val blockData = net.minecraft.server.v1_8_R3.Block.getByCombinedId(combined)
            if (blockData === chunkSection.getType(xSec, ySec, zSec)) {
                //println("Travou aqui type $type  / $data")
                return
            }
            val plugin = Bukkit.getPluginManager().plugins.first()
            chunkSection.setType(xSec, ySec, zSec, blockData)
            val position = BlockPosition(x, y, z)

            if (updateLightning) {
                Bukkit.getScheduler().runTaskAsynchronously(plugin) {
                    worldHandle.x(position)
                    //worldHandle.getLightLevel(position)
                    Bukkit.getScheduler().runTask(plugin) {
                        worldHandle.notify(position)
                        /*
                        // Este pacote é muito gastoso causa bastante lag na GPU
                        // Acho que precisa usar outro pacote o MultiBlockChange
                        for (player in Bukkit.getOnlinePlayers()){
                            val craft = (player as CraftPlayer)
                            val packet = PacketPlayOutBlockChange(
                                worldHandle,
                                position)
                            packet.block = blockData //CraftMagicNumbers.getBlock(material).fromLegacyData(data)
                            craft.handle.playerConnection.sendPacket(packet)
                        }
                         */

                    };
                };
            }
            worldHandle.notify(position)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    override fun canTarget(creature: Creature, classEntityName: String, priority: Int) {

        try {
            val nmsCreature = (creature as CraftCreature).handle
            val entityClass =
                Extra.getClassFrom("#mEntity$classEntityName") as Class<out net.minecraft.server.v1_8_R3.EntityLiving>
            val target = PathfinderGoalNearestAttackableTarget(nmsCreature, entityClass, true)
            nmsCreature.targetSelector.a(target)
            nmsCreature.targetSelector.a(priority, target)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun canAttackMelee(creature: Creature, classEntityName: String, priority: Int) {
        try {
            val nmsCreature = (creature as CraftCreature).handle
            val entityClass = Extra.getClassFrom("#mEntity$classEntityName")
                    as Class<out net.minecraft.server.v1_8_R3.Entity>

            val melee = PathfinderGoalMeleeAttack(nmsCreature, entityClass, 1.0, true)
            nmsCreature.goalSelector.a(melee)
            nmsCreature.goalSelector.a(priority, melee)


        } catch (ex: Exception) {
            ex.printStackTrace()
        }

    }

    override fun followTarget(creature: Creature) {
        try {
            val nmsCreature = (creature as CraftCreature).handle
            nmsCreature.goalSelector.a(PathfinderGoalMoveTowardsTarget(nmsCreature, 1.0, 1.0f))
            /*
            val clz = Extra.getClassFrom("#mEntity$classEntityName")
                    as Class<out net.minecraft.server.v1_8_R3.Entity>
            val melee = PathfinderGoalMeleeAttack(nmsCreature, clz, 1.0, true)
            nmsCreature.goalSelector.a(melee)
            nmsCreature.goalSelector.a(priority, melee)
            */
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun followLocation(creature: Creature, targetLocation: Location) {
        try {
            val nmsCreature = (creature as CraftCreature).handle
            nmsCreature.goalSelector.a(0, PathFinderFollowLocation(creature, targetLocation))
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }


    override fun removeGoals(creature: Creature) {

    }

    override fun forceOpen(chest: Chest, player: Player) {
        val nmsPlayer = ((player as CraftPlayer).handle)
        val nmsChestTile = ((chest as CraftChest).tileEntity)
        nmsPlayer.openTileEntity(nmsChestTile)
    }

    override fun removeTargetGoals(creature: Creature) {

    }

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
        val result = particlesEnumsByName[name.toLowerCase()]
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
        val uuid = Extra.getPlayerUUIDByName(newSkin)
        val skinProperty = Extra.getSkinProperty(uuid)
        val name = skinProperty.get("name").asString
        val skin = skinProperty.get("value").asString
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
        sendPacketsToOthers(playerToRespawn, removePlayerInfo, destroy, metadata, addPlayerInfo, spawn, headRotation)
    }

    override fun reloadPlayer(player: Player) {
        sendPacket(
            player, PacketPlayOutPlayerInfo(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
                (player as CraftPlayer).handle
            )
        )
        sendPacket(
            player, PacketPlayOutPlayerInfo(
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
            PacketPlayOutPlayerInfo.EnumPlayerInfoAction.UPDATE_DISPLAY_NAME,
            (playerToAdd as CraftPlayer).handle
        )
        sendPacketsToAll(updateDisplayName)
    }

    override fun getItemNBT(item: ItemStack): Any {
        val nmsItem: net.minecraft.server.v1_8_R3.ItemStack = CraftItemStack.asNMSCopy(item)
        var nbt: NBTTagCompound
        if (nmsItem.tag.also { nbt = it } == null) nmsItem.tag = nbt
        return nbt
    }

    override fun setItemNBT(item: ItemStack, nbt: Any): ItemStack {
        val nmsItem: net.minecraft.server.v1_8_R3.ItemStack = CraftItemStack.asNMSCopy(item)
        nmsItem.tag = nbt as NBTTagCompound?
        return CraftItemStack.asCraftMirror(nmsItem)
    }

    override fun disableAI(entity: Entity) {
        val nmsEntity = ((entity as CraftEntity).handle)
        val compound = NBTTagCompound()
        nmsEntity.c(compound)
        compound.setByte("NoAI", 1.toByte())
        nmsEntity.f(compound)
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
        var particlesEnumsByName: MutableMap<String, Any> = HashMap()
        var blockedMessage: MutableSet<String> = HashSet()
    }

    init {
        if (particlesEnumsByName.isEmpty()) for (particle in EnumParticle.values()) {
            particlesEnumsByName[particle.b().toLowerCase()] = particle
        }
    }
}