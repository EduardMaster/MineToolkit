package net.eduard.eduardapi.test;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.IMerchant;
import net.minecraft.server.v1_8_R3.MerchantRecipe;
import net.minecraft.server.v1_8_R3.MerchantRecipeList;

/**
* This class allows you to open vilager trade inventories that function as normal for players.
*
* <h1> ---WARNING--- </h1>
* <b>This class is version depended! This means that it will <u>only</u>
* work on servers with the same CraftBukkit version as the one you used to compile your plugin.
* <br>
* Some version changes may affect the function of the class (Like they did in the transaction from 1.7.X to 1.8.X)
* so make sure you check the project page {@link https://bukkit.org/threads/force-open-custom-villager-trade-gui.341546}
* for updates. You can also leave comments for future changes or bugs.
* </b>
*
* @author mine-care (AKA <b>fillpant</b>)
*
*/
public class ForceVillagerTrade {
   
    private String invname;
    private MerchantRecipeList l = new MerchantRecipeList();

    /**
     * @param invname Inventory display name, (May contain color)
     *
     */
    public ForceVillagerTrade(String invname) {
        this.invname = invname;
    }

    /**
     * @param inOne The itemstack in the first input slot.
     * @param out The itemstack output.
     * @return ForceVillagerTrade object so you can invoke the next method like:
     * addTrade(...).addTrade(...).addTrade(...).openTrade(player);
     */
    public ForceVillagerTrade addTrande(ItemStack in, ItemStack out) {
        l.add(new MerchantRecipe(CraftItemStack.asNMSCopy(in), CraftItemStack
                .asNMSCopy(out)));
        return this;
    }

    /**
     * @param inOne The itemstack in the first input slot.
     * @param inTwo The itemstack on the second input slot.
     * @param out The itemstack output.
     * @return ForceVillagerTrade object so you can invoke the next method like:
     * addTrade(...).addTrade(...).addTrade(...).openTrade(player);
     */
    public ForceVillagerTrade addTrande(ItemStack inOne, ItemStack inTwo,
            ItemStack out) {
        l.add(new MerchantRecipe(CraftItemStack.asNMSCopy(inOne),
                CraftItemStack.asNMSCopy(inTwo), CraftItemStack.asNMSCopy(out)));
        return this;
    }

    /**
     * @param who The player who will see the Trade
     */
    public void openTrande(Player who) {
        final EntityHuman e = ((CraftPlayer) who).getHandle();
        e.openTrade(new IMerchant() {
            @Override
            public MerchantRecipeList getOffers(EntityHuman arg0) {
                return l;
            }

            @Override
            public void a_(net.minecraft.server.v1_8_R3.ItemStack arg0) {
            }

            @Override
            public void a_(EntityHuman arg0) {
            }

            @Override
            public IChatBaseComponent getScoreboardDisplayName() {
                return ChatSerializer.a(invname);
            }

            @Override
            public EntityHuman v_() {
                return e;
            }

            @Override
            public void a(MerchantRecipe arg0) {
            }
        });
    }
}