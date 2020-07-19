package net.eduard.api.lib.abstraction;

import net.eduard.api.lib.modules.MineReflect;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MinecraftReflect extends Minecraft {
    @Override
    public void sendPacket(Object packet, Player player) {
        try {
            MineReflect.sendPacket(packet, player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void sendActionBar(Player player, String message) {

        MineReflect.sendActionBar(player,message);
    }

    @Override
    public void sendParticle(Player player, String name, Location location, int amount, float xOffset, float yOffset, float zOffset, float speed) {

    }

    @Override
    public void setHeadSkin(ItemStack head, String texture, String signature) {

    }

    @Override
    public void performRespawn(Player player) {
        MineReflect.makeRespawn(player);
    }

    @Override
    public void setPlayerSkin(Player player, String newSkin) {


    }

    @Override
    public void setPlayerName(Player player, String newName) {

    }

    @Override
    public void respawnPlayer(Player playerToRespawn) {

    }

    @Override
    public void removeFromTab(Player playerRemoved) {

    }

    @Override
    public void addToTab(Player playerToAdd) {

    }

    @Override
    public Object getItemNBT(ItemStack item) {
        return MineReflect.getData(item).getNBT();
    }

    @Override
    public ItemStack setItemNBT(ItemStack item, Object nbt) {
        MineReflect.ItemExtraData data = new MineReflect.ItemExtraData();
        data.setNBT(nbt);
        return MineReflect.setData(item, data);

    }

    @Override
    public void disableAI(Entity entity) {
        MineReflect.disableAI(entity);
    }
}
