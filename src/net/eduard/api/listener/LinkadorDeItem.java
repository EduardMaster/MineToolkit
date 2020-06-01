package net.eduard.api.listener;

import net.eduard.api.lib.manager.EventsManager;
import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.modules.MineReflect;
import net.eduard.api.lib.util.fancyful.FancyMessage;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Base do sistema https://www.spigotmc.org/threads/tut-item-tooltips-with-the-chatcomponent-api.65964/
 */

public class LinkadorDeItem extends EventsManager {
    /**
     * Converts an {@link org.bukkit.inventory.ItemStack} to a Json string
     * for sending with {@link net.md_5.bungee.api.chat.BaseComponent}'s.
     *
     * @param itemStack the item to convert
     * @return the Json string representation of the item
     */
    public String convertItemStackToJsonRegular(ItemStack itemStack) {
        // First we convert the item stack into an NMS itemstack
        net.minecraft.server.v1_7_R4.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(itemStack);
        net.minecraft.server.v1_7_R4.NBTTagCompound compound = new NBTTagCompound();
        compound = nmsItemStack.save(compound);

        return compound.toString();
    }

    /**
     * Converts an {@link org.bukkit.inventory.ItemStack} to a Json string
     * for sending with {@link net.md_5.bungee.api.chat.BaseComponent}'s.
     *
     * @param itemStack the item to convert
     * @return the Json string representation of the item
     */
    public String convertItemStackToJson(ItemStack itemStack) {
        /*
        // ItemStack methods to get a net.minecraft.server.ItemStack object for serialization
        Class<?> craftItemStackClazz = MineReflect.("#cinventory.CraftItemStack");
        Method asNMSCopyMethod = ReflectionUtil.getMethod(craftItemStackClazz, "asNMSCopy", ItemStack.class);

        // NMS Method to serialize a net.minecraft.server.ItemStack to a valid Json string
        Class<?> nmsItemStackClazz = ReflectionUtil.getNMSClass("ItemStack");
        Class<?> nbtTagCompoundClazz = ReflectionUtil.getNMSClass("NBTTagCompound");
        Method saveNmsItemStackMethod = ReflectionUtil.getMethod(nmsItemStackClazz, "save", nbtTagCompoundClazz);

        Object nmsNbtTagCompoundObj; // This will just be an empty NBTTagCompound instance to invoke the saveNms method
        Object nmsItemStackObj; // This is the net.minecraft.server.ItemStack object received from the asNMSCopy method
        Object itemAsJsonObject; // This is the net.minecraft.server.ItemStack after being put through saveNmsItem method

        try {
            nmsNbtTagCompoundObj = nbtTagCompoundClazz.newInstance();
            nmsItemStackObj = asNMSCopyMethod.invoke(null, itemStack);
            itemAsJsonObject = saveNmsItemStackMethod.invoke(nmsItemStackObj, nmsNbtTagCompoundObj);
        } catch (Throwable t) {
            Bukkit.getLogger().log(Level.SEVERE, "failed to serialize itemstack to nms item", t);
            return null;
        }
                // Return a string representation of the serialized object
         return itemAsJsonObject.toString();
        */
        try {
            Object itemCopia = Extra.getResult(MineReflect.classCraftItemStack, "asNMSCopy", new Object[]{ItemStack.class}, itemStack);
            Object nbt = Extra.getNew(MineReflect.classMineNBTTagCompound);
            Object json = Extra.getResult(itemCopia, "save", nbt);
            return json.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }


        return null;


    }

    /**
     * Sends a message to a player with an item as it's tooltip
     *
     * @param player  the player
     * @param message the message to send
     * @param item    the item to display in the tooltip
     */
    public void sendItemTooltipMessage(Player player, String message, ItemStack item) {
        String itemJson = convertItemStackToJson(item);

        // Prepare a BaseComponent array with the itemJson as a text component
        BaseComponent[] hoverEventComponents = new BaseComponent[]{
                new TextComponent(itemJson) // The only element of the hover listener basecomponents is the item json
        };

        // Create the hover event
        HoverEvent event = new HoverEvent(HoverEvent.Action.SHOW_ITEM, hoverEventComponents);

        /* And now we create the text component (this is the actual text that the player sees)
         * and set it's hover event to the item event */
        TextComponent component = new TextComponent(message);
        component.setHoverEvent(event);

        // Finally, send the message to the player
        player.spigot().sendMessage(component);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void event(PlayerInteractEvent e) {
        if (e.getItem() == null) return;
        if (e.getPlayer().isSneaking()) {


            //sendItemTooltipMessage(e.getPlayer(), "Item no chat", e.getPlayer().getItemInHand());
        }
    }
}
