package net.eduard.api.lib.game;

import net.eduard.api.lib.modules.Extra;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.modules.Mine;

public class ItemRandom {

    private int minAmount = 1;

    private int maxAmount = 1;

    private double chance = 1.0;

    private ItemStack item;

    public ItemRandom() {
    }

    public ItemRandom(ItemStack item, int min, int max) {
        this(item, min, max, 1);
    }

    public ItemRandom(ItemStack item, int min) {
        this(item, min, min);
    }

    public ItemRandom(ItemStack item, int min, int max, double chance) {
        setMinAmount(min);
        setMaxAmount(max);
        setChance(chance);
        setItem(item);
    }

    public ItemStack create() {
        if (Mine.getChance(chance)) {

            ItemStack clone = item.clone();
            int amount = Extra.getRandomInt(getMinAmount(), getMaxAmount());

            clone.setAmount(amount);
            return clone;
        }
        return new ItemStack(Material.AIR);

    }


    public double getChance() {

        return chance;
    }

    public int getMaxAmount() {

        return maxAmount;
    }

    public int getMinAmount() {

        return minAmount;
    }

    public ItemStack getItem() {

        return this.item;
    }

    public void setItem(ItemStack item) {

        this.item = item;
    }

    public void setChance(double chance) {

        this.chance = chance;
    }

    public void setMaxAmount(int maxAmount) {

        this.maxAmount = maxAmount;
    }

    public void setMinAmount(int minAmount) {

        this.minAmount = minAmount;

    }


}
