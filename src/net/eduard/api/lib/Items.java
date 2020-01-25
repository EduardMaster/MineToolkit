package net.eduard.api.lib;

import net.eduard.api.lib.modules.Extra;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Sistema para guardar dados extras nos {@link ItemStack}
 *
 * @author Eduard
 * @since 24/01/2020
 */
public class Items {
    public static ItemStack toStack(ItemStack original, double amount) {

        List<String> lore = Mine.getLore(original);
        lore.add(Mine.MSG_ITEM_STACK.replace("$stack",Extra.MONEY.format( amount)));
        Mine.setLore(original, lore);
        Items.ItemExtraData data = Items.getData(original);
        data.setCustomStack(amount);
        original = Items.setData(original, data);
        return original;
    }

    public static class ItemExtraData {

        private ItemExtraData() {
            // net.minecraft.server.v1_8_R3.ItemStack

        }

        private Object nbtcompound;

        public String getUniqueId() {
            return getString("UNIQUE_ID");
        }

        public double getCustomStack() {
            return getDouble("CUSTOM_STACK");
        }

        public void setUniqueId(String uniqueId) {
            setString("UNIQUE_ID", uniqueId);
        }

        public void setCustomStack(double stack) {
            setDouble("CUSTOM_STACK", stack);
        }

        public boolean has(String key) {


            try {
                Method getString = Extra.getMethod(Mine.classMineNBTTagCompound, "hasKey", String.class);
                return (boolean) getString.invoke(nbtcompound, key);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        public String getString(String key) {
            if (has(key)) {
                try {
                    Method getString = Extra.getMethod(Mine.classMineNBTTagCompound, "getString", String.class);
                    return (String) getString.invoke(nbtcompound, key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;

        }

        public double getDouble(String key) {
            if (has(key)) {
                try {
                    Method getString = Extra.getMethod(Mine.classMineNBTTagCompound, "getDouble", String.class);
                    return (double) getString.invoke(nbtcompound, key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return 0;

        }

        public String setDouble(String key, double value) {
            try {
                Method getString = Extra.getMethod(Mine.classMineNBTTagCompound, "setDouble", String.class, double.class);
                return (String) getString.invoke(nbtcompound, key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        public String setString(String key, String value) {
            try {
                Method getString = Extra.getMethod(Mine.classMineNBTTagCompound, "setString", String.class, String.class);
                return (String) getString.invoke(nbtcompound, key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        private void setNBT(Object nbtcompound) {
            this.nbtcompound = nbtcompound;
        }

        private Object getNBT() {
            return nbtcompound;
        }
    }

    private static Object emptyNBT() {
        Object nbt = null;
        try {
            nbt = Extra.getNew(Mine.classMineNBTTagCompound);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nbt;
    }

    public static ItemExtraData getData(ItemStack item) {
        ItemExtraData data = new ItemExtraData();
        if (item != null) {
            Object nbt = getNBT(item);
            if (nbt == null) {
                nbt = emptyNBT();
            }
            data.setNBT(nbt);
        } else {
            data.setNBT(emptyNBT());
        }


        return data;
    }

    private static Object getNBT(ItemStack item) {

        try {
            Method asNMSCopy = Extra.getMethod(Mine.classCraftItemStack, "asNMSCopy", ItemStack.class);
            Object itemCopia = asNMSCopy.invoke(0, item);
            if (itemCopia == null) {
                return null;
            }
            Method getTag = Extra.getMethod(Mine.classMineItemStack, "getTag");
            Object tag = getTag.invoke(itemCopia);
            return tag;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ItemStack setData(ItemStack item, ItemExtraData data) {
        try {
            Method asNMSCopy = Extra.getMethod(Mine.classCraftItemStack, "asNMSCopy", ItemStack.class);
            Object itemCopia = asNMSCopy.invoke(0, item);
            Method setTag = Extra.getMethod(Mine.classMineItemStack, "setTag", Mine.classMineNBTTagCompound);
            setTag.invoke(itemCopia, data.getNBT());
            Method asCraftMirror = Extra.getMethod(Mine.classCraftItemStack, "asCraftMirror", Mine.classMineItemStack);
            Object itemModified = asCraftMirror.invoke(0, itemCopia);

            Method asBukkitCopy = Extra.getMethod(Mine.classCraftItemStack, "asBukkitCopy", Mine.classMineItemStack);
            itemModified = asBukkitCopy.invoke(0, itemCopia);
            return (ItemStack) itemModified;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
