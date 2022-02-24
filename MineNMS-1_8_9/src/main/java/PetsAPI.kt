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
    private var pathFinderListField: Field? = null
    private var goalSelector: Field? = null
    private var targetSelector: Field? = null
    fun makePet(e: LivingEntity, toFollow: UUID?) {
        try {
            val nmsEntity: Any = (e as CraftLivingEntity).handle
            if (nmsEntity is EntityInsentient) {
                val goal = goalSelector!![nmsEntity] as PathfinderGoalSelector
                val target = targetSelector!![nmsEntity] as PathfinderGoalSelector
                pathFinderListField!![goal] = UnsafeList<Any>()
                pathFinderListField!![target] = UnsafeList<Any>()
                goal.a(0, PathfinderGoalFloat(nmsEntity))
                goal.a(1, PathfinderGoalWalktoTile(nmsEntity, toFollow))
            } else {
                throw IllegalArgumentException(e.getType().getName() + " is not an instance of an EntityInsentient.")
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    class PathfinderGoalWalktoTile(private val entity: EntityInsentient, private val uuid: UUID?) : PathfinderGoal() {
        private var path: PathEntity? = null
        override fun a(): Boolean {
            val alvo = Bukkit.getPlayer(uuid)
            if (Bukkit.getPlayer(uuid) == null) {
                return path != null
            }
            val targetLocation = alvo.location
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
            pathFinderListField!!.isAccessible = true
            goalSelector = EntityInsentient::class.java.getDeclaredField("goalSelector")
            goalSelector!!.isAccessible = true
            targetSelector = EntityInsentient::class.java.getDeclaredField("targetSelector")
            targetSelector!!.isAccessible = true
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}