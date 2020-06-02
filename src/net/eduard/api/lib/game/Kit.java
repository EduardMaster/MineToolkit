package net.eduard.api.lib.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.lib.storage.Storable;

public class Kit   {

    private String name;
    private long cooldown;
    private boolean clearInventory;
    private boolean fillSoup;
    private boolean autoEquip = true;
    private ItemStack icon;
    private int menuPosition;
    private double price;
    private int level;
    private List<ItemStack> items = new ArrayList<>();
    private List<Item> extras = new ArrayList<>();
    private List<Kit> upgrades = new ArrayList<>();

    public Kit(Kit kit, int price, int level) {
        kit.upgrades.add(this);
        setPrice(price);
        setLevel(level);

    }

    public Kit() {

    }

    public void give(Player p) {
        give(p, 1);
    }

    public void give(Player p, int level) {
        PlayerInventory inv = p.getInventory();
        if (clearInventory) {
            Mine.clearInventory(p);
        }
        for (ItemStack item : items) {
            String type = item.getType().name();
            if (autoEquip) {
                if (type.contains("LEGGINGS")) {
                    inv.setLeggings(item);
                } else if (type.contains("CHESTPLATE")) {
                    inv.setChestplate(item);
                } else if (type.contains("BOOTS")) {
                    inv.setBoots(item);
                } else if (type.contains("HELMET")) {
                    inv.setHelmet(item);
                } else {
                    inv.addItem(item);
                }
            } else {
                inv.addItem(item);
            }
        }
        for (int id = 2; id <= level; id++) {
            Kit upgrade = getUpgrade(id);
            for (ItemStack item : upgrade.getItems()) {
                String type = item.getType().name();
                if (autoEquip) {
                    if (type.contains("LEGGINGS")) {
                        inv.setLeggings(item);
                    } else if (type.contains("CHESTPLATE")) {
                        inv.setChestplate(item);
                    } else if (type.contains("BOOTS")) {
                        inv.setBoots(item);
                    } else if (type.contains("HELMET")) {
                        inv.setHelmet(item);
                    } else {
                        inv.addItem(item);
                    }
                } else {
                    inv.addItem(item);
                }
            }
        }
        for (Item item : extras) {
            inv.addItem(item.create());
        }
        if (fillSoup)
            Mine.fill(inv, new ItemStack(Material.MUSHROOM_SOUP));
    }

    public Kit getUpgrade(int level) {
        for (Kit upgrade : upgrades) {
            if (upgrade.getLevel() == level) {
                return upgrade;
            }
        }
        return null;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public void setItems(List<ItemStack> items) {
        this.items = items;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ItemStack getIcon() {
        return icon;
    }

    public void setIcon(ItemStack icon) {
        this.icon = icon;
    }

    public boolean isClearInventory() {
        return clearInventory;
    }

    public void setClearInventory(boolean clearInventory) {
        this.clearInventory = clearInventory;
    }

    public boolean isFillSoup() {
        return fillSoup;
    }

    public void setFillSoup(boolean fillSoup) {
        this.fillSoup = fillSoup;
    }

    public long getCooldown() {
        return cooldown;
    }

    public void setCooldown(long cooldown) {
        this.cooldown = cooldown;
    }


    public List<Item> getExtras() {
        return extras;
    }

    public void setExtras(List<Item> extras) {
        this.extras = extras;
    }

    public boolean isAutoEquip() {
        return autoEquip;
    }

    public void setAutoEquip(boolean autoEquip) {
        this.autoEquip = autoEquip;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMenuPosition() {
        return menuPosition;
    }

    public void setMenuPosition(int menuPosition) {
        this.menuPosition = menuPosition;
    }
}
