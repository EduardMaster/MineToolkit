package net.eduard.api.lib.abstraction

import net.minecraft.server.v1_8_R3.*
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * This class allows you to open vilager trade inventories that function as normal for players.
 *
 * <h1> ---WARNING--- </h1>
 * **This class is version depended! This means that it will <u>only</u>
 * work on servers with the same CraftBukkit version as the one you used to compile your plugin.
 * <br></br>
 * Some version changes may affect the function of the class (Like they did in the transaction from 1.7.X to 1.8.X)
 * so make sure you check the project page http://www.bukkit.org/threads/force-open-custom-villager-trade-gui.341546
 * for updates. You can also leave comments for future changes or bugs.
 ** *
 *
 * @author mine-care (AKA **fillpant**)
 */
class ForceVillagerTrade
/**
 * Não pode ter espaço
 * @param invname Inventory display name, (May contain color)
 */(private val invname: String) {
    private val list = MerchantRecipeList()

    /**
     * @param in The itemstack in the first input slot.
     * @param out The itemstack output.
     * @return ForceVillagerTrade object so you can invoke the next method like:
     * addTrade(...).addTrade(...).addTrade(...).openTrade(player);
     */
    fun addTrande(`in`: ItemStack?, out: ItemStack?): ForceVillagerTrade {
        list.add(
            MerchantRecipe(
                CraftItemStack.asNMSCopy(`in`), CraftItemStack
                    .asNMSCopy(out)
            )
        )
        return this
    }

    /**
     * @param inOne The itemstack in the first input slot.
     * @param inTwo The itemstack on the second input slot.
     * @param out The itemstack output.
     * @return ForceVillagerTrade object so you can invoke the next method like:
     * addTrade(...).addTrade(...).addTrade(...).openTrade(player);
     */
    fun addTrande(
        inOne: ItemStack?, inTwo: ItemStack?,
        out: ItemStack?
    ): ForceVillagerTrade {
        list.add(
            MerchantRecipe(
                CraftItemStack.asNMSCopy(inOne),
                CraftItemStack.asNMSCopy(inTwo), CraftItemStack.asNMSCopy(out)
            )
        )
        return this
    }

    /**
     * @param who The player who will see the Trade
     */
    fun openTrande(who: Player) {
        val e: EntityHuman = (who as CraftPlayer).handle
        e.openTrade(object : IMerchant {
            override fun getOffers(arg0: EntityHuman): MerchantRecipeList {
                return list
            }

            override fun a_(arg0: net.minecraft.server.v1_8_R3.ItemStack) {}
            override fun a_(arg0: EntityHuman) {}
            override fun getScoreboardDisplayName(): IChatBaseComponent {
                return IChatBaseComponent.ChatSerializer.a(invname)
            }

            override fun v_(): EntityHuman {
                return e
            }

            override fun a(arg0: MerchantRecipe) {}
        })
    }
}