package net.eduard.api.lib.abstraction;

import net.eduard.api.lib.modules.MineReflect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import net.eduard.api.lib.modules.Mine;

/**
 * Representa uma Api como a do Bukkit porem com muito menos métodos
 *
 * @author Eduard
 */
abstract public class Minecraft {

    private static Minecraft instance;

    /**
     * Envia um Pacote de dados para o jogador
     *
     * @param packet Pacote
     * @param player Jogador
     */

    public void sendPacket(Object packet, Player player) {
        try {
            MineReflect.sendPacket(packet, player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Envia um Pacote de dados para o jogador
     *
     * @param player Jogador
     * @param packet Pacote
     */
    public void sendPacket(Player player, Object packet) {
        sendPacket(packet, player);
    }

    /**
     * Inicia automaticamente uma istancia da versão atual do servidor
     *
     * @return Instancia feita com Reflection
     */
    public static Minecraft getInstance() {

        try {
            if (instance == null) {
                instance = (Minecraft) Class.forName("net.eduard.api.lib.abstraction.Minecraft_" + MineReflect.getVersion())
                        .newInstance();
            }
            return instance;

        } catch (Exception error) {
            if (instance == null) {
                instance = new MinecraftReflect();
            }
            return instance;

        }

    }

    public void sendActionBar(Player player, String message) {
        MineReflect.sendActionBar(player, message);
    }

    public void sendParticle(Player player, String name, Location location, int amount, float xOffset, float yOffset, float zOffset, float speed) {

    }

    public final void sendParticle(Player player, String name, Location location, int amount) {
        sendParticle(player, name, location, amount, 0, 0, 0, 1);
    }


    public final void sendPacketsToAll(Object... packets) {
        for (Player player : Mine.getPlayers()) {
            for (Object packet : packets) {
                sendPacket(packet, player);
            }
        }
    }

    public final void sendPacketsToOthers(Player player, Object... packets) {
        for (Player playerLoop : Mine.getPlayers()) {
            if (playerLoop.equals(player)) continue;
            if (playerLoop.canSee(player)) {
                for (Object packet : packets) {
                    sendPacket(packet, player);
                }
            }
        }
    }

    public void setHeadSkin(ItemStack head, String texture, String signature) {

    }

    public void performRespawn(Player player) {
        MineReflect.makeRespawn(player);
    }

     public void setPlayerSkin(Player player, String newSkin){

    }

    public void setPlayerName(Player player, String newName) {

    }

    public void respawnPlayer(Player playerToRespawn) {

    }

    public void reloadPlayer(Player player) {

    }

    public void setTabList(Player player, String header, String footer) {
        MineReflect.setTabList(player, header, footer);
    }

    public void removeFromTab(Player playerRemoved) {

    }

    public void addToTab(Player playerToAdd) {

    }

    public void updateDisplayName(Player playerToAdd) {

    }


    public Object getItemNBT(ItemStack item) {
        return MineReflect.getData(item).getNBT();
    }


    public ItemStack setItemNBT(ItemStack item, Object nbt) {
        MineReflect.ItemExtraData data = new MineReflect.ItemExtraData();
        data.setNBT(nbt);
        return MineReflect.setData(item, data);

    }
    public void disableAI(Entity entity) {
        MineReflect.disableAI(entity);
    }


}
