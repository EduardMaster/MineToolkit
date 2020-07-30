package net.eduard.api.lib.abstraction

import com.mojang.authlib.GameProfile
import net.eduard.api.lib.manager.EventsManager
import net.eduard.api.lib.modules.Extra
import net.minecraft.server.v1_8_R3.*
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList
import org.bukkit.entity.Enderman
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.inventory.ItemStack
import java.util.*
import java.util.concurrent.ConcurrentLinkedQueue

class EntidadesNMSTeste : EventsManager() {


    @EventHandler
    fun event(e: PlayerCommandPreprocessEvent) {
        if (e.message.startsWith("/cu")) {
            e.isCancelled = true
            removerBlocosErrados(e.player)
        }

    }
    val queue = ConcurrentLinkedQueue<Block>()

    fun limparBlocos(){
        
    }

    fun removerBlocosErrados(p: Player) {
        val variation = 500
        for (x in -variation until +variation) {
            for (y in 0 until 100) {
                for (z in -variation until +variation) {
                    val block = p.world.getBlockAt(x,y,z)
                    if (block.typeId == 22){
                        queue.add(block)
                    }

                }
            }
        }

    }

    fun spawnNpcPlayer(p: Player) {
        p.sendMessage("§aTentando spawnar npc player")

        val world = ((p.world) as CraftWorld).handle
        val gameProfile = GameProfile(UUID.randomUUID(), "§aZeBurceta")
        val playerInteract = PlayerInteractManager(world)
        val connection = ((p as CraftPlayer).handle).playerConnection
        val playerNpc = EntityPlayer(world.minecraftServer, world, gameProfile, playerInteract)

        playerNpc.setLocation(p.location.x, p.location.y, p.location.z, p.location.yaw, p.location.pitch)
        val packetSpawn = PacketPlayOutNamedEntitySpawn(playerNpc)
        val packetAdd = PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, playerNpc)
        val packetRemove = PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, playerNpc)
        connection.sendPacket(packetAdd)
        connection.sendPacket(packetSpawn)
        connection.sendPacket(packetRemove)
    }

    fun spawnGuerra(p: Player) {
        p.world.livingEntities.filterIsInstance<Enderman>().forEach(Enderman::remove)
        p.world.livingEntities.filterIsInstance<Zombie>().forEach(Zombie::remove)
        val connection = ((p as CraftPlayer).handle).playerConnection
        val world = ((p.world) as CraftWorld).handle
        p.sendMessage("§aGuerra de entidades")
        for (x in 0..50) {
            val zombie = EntityZombie(world)
            zombie.setLocation(p.location.x, p.location.y, p.location.z, p.location.yaw, p.location.pitch)
            zombie.setEquipment(0, CraftItemStack.asNMSCopy(ItemStack(Material.DIAMOND_SWORD)))
            clearSelector(zombie.goalSelector)
            clearSelector(zombie.targetSelector)

            //zombie.goalSelector.a(8, PathfinderGoalLookAtPlayer(zombie, EntityEnderman::class.java, 8F));
            zombie.targetSelector.a(2, PathfinderGoalNearestAttackableTarget(zombie, EntityEnderman::class.java, true));
            zombie.goalSelector.a(2, PathfinderGoalMeleeAttack(zombie, EntityEnderman::class.java, 1.0, false))


            zombie.targetSelector.a(3, PathfinderGoalNearestAttackableTarget(zombie, EntitySkeleton::class.java, true));
            zombie.goalSelector.a(3, PathfinderGoalMeleeAttack(zombie, EntitySkeleton::class.java, 1.0, false))
            zombie.goalSelector.a(5, PathfinderGoalMoveTowardsRestriction(zombie, 1.0))


            world.addEntity(zombie)
        }
        for (x in 0..40) {
            val enderman = EntityEnderman(world)
            enderman.setLocation(p.location.x, p.location.y, p.location.z, p.location.yaw, p.location.pitch)
            enderman.setEquipment(0, CraftItemStack.asNMSCopy(ItemStack(Material.DIAMOND_SWORD)))
            clearSelector(enderman.goalSelector)
            clearSelector(enderman.targetSelector)
            enderman.targetSelector.a(3, PathfinderGoalNearestAttackableTarget(enderman, EntityZombie::class.java, true));
            enderman.goalSelector.a(3, PathfinderGoalMeleeAttack(enderman, EntityZombie::class.java, 1.0, false))
            enderman.targetSelector.a(2, PathfinderGoalNearestAttackableTarget(enderman, EntitySkeleton::class.java, true));
            enderman.goalSelector.a(2, PathfinderGoalMeleeAttack(enderman, EntitySkeleton::class.java, 1.0, false))
            enderman.goalSelector.a(5, PathfinderGoalMoveTowardsRestriction(enderman, 1.0))
            world.addEntity(enderman)
        }
        for (x in 0..50) {
            val skeleton = EntitySkeleton(world)
            skeleton.setEquipment(0, CraftItemStack.asNMSCopy(ItemStack(Material.DIAMOND_SWORD)))
            skeleton.setLocation(p.location.x, p.location.y, p.location.z, p.location.yaw, p.location.pitch)
            clearSelector(skeleton.goalSelector)
            clearSelector(skeleton.targetSelector)
            skeleton.targetSelector.a(2, PathfinderGoalNearestAttackableTarget(skeleton, EntityZombie::class.java, true));
            skeleton.goalSelector.a(2, PathfinderGoalMeleeAttack(skeleton, EntityZombie::class.java, 1.0, false))
            skeleton.targetSelector.a(3, PathfinderGoalNearestAttackableTarget(skeleton, EntityEnderman::class.java, true));
            skeleton.goalSelector.a(3, PathfinderGoalMeleeAttack(skeleton, EntityEnderman::class.java, 1.0, false))
            skeleton.goalSelector.a(5, PathfinderGoalMoveTowardsRestriction(skeleton, 1.0))
            world.addEntity(skeleton)
        }
    }

    fun clearSelector(selector: PathfinderGoalSelector) {
        try {
            Extra.setFieldValue(selector, "b", UnsafeList<Any>())
            Extra.setFieldValue(selector, "c", UnsafeList<Any>())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

}