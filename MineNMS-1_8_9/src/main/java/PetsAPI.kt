package net.eduard.api.lib.abstraction

import java.util.UUID
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity
import net.minecraft.server.v1_8_R3.EntityInsentient
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat
import java.lang.IllegalArgumentException
import net.minecraft.server.v1_8_R3.PathfinderGoal
import net.minecraft.server.v1_8_R3.PathEntity
import org.bukkit.Bukkit
import org.bukkit.entity.LivingEntity
import java.lang.Exception
import java.lang.reflect.Field

object PetsAPI {
    private lateinit var pathFinderListField: Field
    private lateinit var goalSelectorField: Field
    private lateinit var targetSelectorField: Field
    fun makePetFollowPlayer(petEntity: LivingEntity, playerUUID: UUID) {
        try {
            val nmsEntity: Any = (petEntity as CraftLivingEntity).handle
            if (nmsEntity is EntityInsentient) {
                val goal = goalSelectorField[nmsEntity] as PathfinderGoalSelector
                val target = targetSelectorField[nmsEntity] as PathfinderGoalSelector
                pathFinderListField[goal] = UnsafeList<Any>()
                pathFinderListField[target] = UnsafeList<Any>()
                goal.a(0, PathfinderGoalFloat(nmsEntity))
                goal.a(1, PathFinderFollowPlayer(nmsEntity, playerUUID))
            } else {
                throw IllegalArgumentException(
                    petEntity.getType().getName() + " is not an instance of an EntityInsentient."
                )
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    class PathFinderFollowPlayer(
        val entity: EntityInsentient,
        val playerID: UUID
    ) : PathfinderGoal() {
        var path: PathEntity? = null
        override fun a(): Boolean {
            val target = Bukkit.getPlayer(playerID) ?: return path != null
            val targetLocation = target.location
            //boolean flag = this.entity.getNavigation().m();
            path = entity.navigation.a(targetLocation.x + 1, targetLocation.y, targetLocation.z + 1)
            if (path != null) {
                c()
            }
            return path != null
        }

        override fun e() {
        }

        override fun d() {
        }

        override fun i(): Boolean {
            return true
        }

        override fun c() {
            entity.navigation.a(path, 1.0)
        }
    }

    init {
        try {
            pathFinderListField = PathfinderGoalSelector::class.java.getDeclaredField("b")
            pathFinderListField.isAccessible = true
            goalSelectorField = EntityInsentient::class.java.getDeclaredField("goalSelector")
            goalSelectorField.isAccessible = true
            targetSelectorField = EntityInsentient::class.java.getDeclaredField("targetSelector")
            targetSelectorField.isAccessible = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}