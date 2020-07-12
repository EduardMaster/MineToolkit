package net.eduard.api.lib.modules;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Classe que constitui métodos com Reflection para funções que não possui na Bukkit API
 * <br>
 *  Nome antigo: RexAPI
 * @version 1.0
 * @since 02/06/2020
 */
public class MineReflect {
    /**
     * INICIO Métodos ABAIXO
     *
     */

    public static  String MSG_ITEM_STACK = "§aQuantidade: §f$stack";
    public static ItemStack toStack(ItemStack original, double amount) {

        List<String> lore = Mine.getLore(original);
        lore.add(MineReflect.MSG_ITEM_STACK.replace("$stack",Extra.formatMoney(amount)));
        Mine.setLore(original, lore);
        ItemExtraData data = getData(original);
        data.setCustomStack(amount);
        original = setData(original, data);
        return original;
    }


    public static class ItemExtraData {

        private ItemExtraData() {

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
                Method getString = Extra.getMethod(MineReflect.classMineNBTTagCompound, "hasKey", String.class);
                return (boolean) getString.invoke(nbtcompound, key);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        public String getString(String key) {
            if (has(key)) {
                try {
                    Method getString = Extra.getMethod(MineReflect.classMineNBTTagCompound, "getString", String.class);
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
                    Method getString = Extra.getMethod(MineReflect.classMineNBTTagCompound, "getDouble", String.class);
                    return (double) getString.invoke(nbtcompound, key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return 0;

        }

        public String setDouble(String key, double value) {
            try {
                Method getString = Extra.getMethod(MineReflect.classMineNBTTagCompound, "setDouble", String.class, double.class);
                return (String) getString.invoke(nbtcompound, key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        public String setString(String key, String value) {
            try {
                Method getString = Extra.getMethod(MineReflect.classMineNBTTagCompound, "setString", String.class, String.class);
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
            nbt = Extra.getNew(MineReflect.classMineNBTTagCompound);
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
            Method asNMSCopy = Extra.getMethod(MineReflect.classCraftItemStack, "asNMSCopy", ItemStack.class);
            Object itemCopia = asNMSCopy.invoke(0, item);
            if (itemCopia == null) {
                return null;
            }
            Method getTag = Extra.getMethod(MineReflect.classMineItemStack, "getTag");
            Object tag = getTag.invoke(itemCopia);
            return tag;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ItemStack setData(ItemStack item, ItemExtraData data) {
        try {
            Method asNMSCopy = Extra.getMethod(MineReflect.classCraftItemStack, "asNMSCopy", ItemStack.class);
            Object itemCopia = asNMSCopy.invoke(0, item);
            Method setTag = Extra.getMethod(MineReflect.classMineItemStack, "setTag", MineReflect.classMineNBTTagCompound);
            setTag.invoke(itemCopia, data.getNBT());
            Method asCraftMirror = Extra.getMethod(MineReflect.classCraftItemStack, "asCraftMirror", MineReflect.classMineItemStack);
            Object itemModified = asCraftMirror.invoke(0, itemCopia);

            Method asBukkitCopy = Extra.getMethod(MineReflect.classCraftItemStack, "asBukkitCopy", MineReflect.classMineItemStack);
            itemModified = asBukkitCopy.invoke(0, itemCopia);
            return (ItemStack) itemModified;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     *
     *
     *
     *
     *
     */





    /**
     * @return Versão do Servidor
     */
    public static String getVersion() {

        try {
            String v = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            return v;
        } catch (Exception er) {
            return "";
        }

    }

    /**
     * (Não funciona)
     *
     * @return Versão do Servidor
     */
    @Deprecated
    public static String getVersion2() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\\\")[3];
    }

    static {
        Extra.newReplacer("#v", MineReflect.getVersion());
    }
    public static String classMineEntityPlayer = "#mEntityPlayer";
    public static String classCraftCraftPlayer = "#cCraftPlayer";
    public static String classSpigotPacketTitle = "#sProtocolInjector$PacketTitle";
    public static String classSpigotAction = "#sProtocolInjector$PacketTitle$Action";
    public static String classSpigotPacketTabHeader = "#sProtocolInjector$PacketTabHeader";
    public static String classPacketPlayOutChat = "#pPlayOutChat";
    public static String classPacketPlayOutTitle = "#pPlayOutTitle";
    public static String classPacketPlayOutWorldParticles = "#pPlayOutWorldParticles";
    public static String classPacketPlayOutPlayerListHeaderFooter = "#pPlayOutPlayerListHeaderFooter";
    public static String classPacketPlayOutNamedEntitySpawn = "#pPlayOutNamedEntitySpawn";
    public static String classPacketPlayInClientCommand = "#pPlayInClientCommand";
    public static String classCraftEnumTitleAction = "#cEnumTitleAction";
    public static String classPacketEnumTitleAction2 = "#pPlayOutTitle$EnumTitleAction";
    public static String classMineEnumClientCommand = "#mEnumClientCommand";
    public static String classMineEnumClientCommand2 = "#pPlayInClientCommand$EnumClientCommand";
    public static String classMineChatSerializer = "#mChatSerializer";
    public static String classMineIChatBaseComponent = "#mIChatBaseComponent";
    public static String classMineEntityHuman = "#mEntityHuman";
    public static String classMineNBTTagCompound = "#mNBTTagCompound";
    public static String classMineNBTBase = "#mNBTBase";
    public static String classMineNBTTagList = "#mNBTTagList";
    public static String classPacketPacket = "#p";
    public static String classCraftItemStack = "#cinventory.CraftItemStack";
    public static String classMineItemStack = "#mItemStack";
    public static String classBukkitItemStack = "#bItemStack";
    public static String classBukkitBukkit = "#bBukkit";
    public static String classMineChatComponentText = "#mChatComponentText";
    public static String classMineMinecraftServer = "#mMinecraftServer";

    public static Villager newNPCVillager(Location location, String name) {
        Villager npc = location.getWorld().spawn(location, Villager.class);
        npc.setCustomName(name);
        npc.setCustomNameVisible(true);
        MineReflect.disableAI(npc);
        return npc;
    }
    /**
     * Pega o TPS do servidor uma expecie de calculador de LAG
     *
     * @return TPS em forma de DOUBLE
     */
    public static Double getTPS() {
        try {
            return Double.valueOf(Math.min(20.0D, Math.round(MineReflect.getCurrentTick() * 10) / 10.0D));
        } catch (Exception e) {
        }

        return 0D;
    }

    public static int getCurrentTick() throws Exception {
        return (int) Extra.getValue(MineReflect.classMineMinecraftServer, "currentTick");
    }

    /**
     * Altera a stack máxima do Item
     * @param itemOriginal
     * @param amount
     * @return
     */
    public static ItemStack setMaxStackSize(ItemStack itemOriginal, int amount) {
        try {

            Class<?> claz = Extra.getClassFrom(MineReflect.classCraftItemStack);
            Method method_asNMSCopy = Extra.getMethod(claz, "asNMSCopy", ItemStack.class);
            Method method_asBukkitCopy = Extra.getMethod(claz, "asBukkitCopy", MineReflect.classMineItemStack);
            Object nmsItemStack = method_asNMSCopy.invoke(0, itemOriginal);

            Method method_getItem = nmsItemStack.getClass().getDeclaredMethod("getItem");
            Object nmsItem = method_getItem.invoke(nmsItemStack);
            Method method_c = nmsItem.getClass().getDeclaredMethod("c", int.class);
            method_c.invoke(nmsItem, amount);

            Object newItem = method_asBukkitCopy.invoke(0, nmsItemStack);

            return (ItemStack) newItem;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * Desabilita a Inteligência da Entidade<br>
     * <p>
     * NMS código executado <br>
     *
     * <code>
     * net.minecraft.server.v1_8_R3.Entity NMS = ((CraftEntity) entidade).getHandle();<br>
     * NBTTagCompound compound = new NBTTagCompound();<br>
     * NMS.c(compound);<br>
     * compound.setByte("NoAI", (byte) 1);<br>
     * NMS.f(compound);
     *
     * </code>
     *
     * @param entity Entidade
     */
    public static void disableAI(Entity entity) {
        try {
            Object compound = Extra.getNew(MineReflect.classMineNBTTagCompound);
            Object getHandle = Extra.getResult(entity, "getHandle");
            Extra.getResult(getHandle, "c", compound);
            Extra.getResult(compound, "setByte", "NoAI", (byte) 1);
            Extra.getResult(getHandle, "f", compound);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * Modifica o nome do Jogador para um Novo Nome e<br>
     * Envia para Todos os outros Jogadores a alteração (Packet)
     *
     * @param player      Jogador
     * @param displayName Novo Nome
     */
    public static void changeName(Player player, String displayName) {

        try {
            Object entityplayer = getHandle(player);
            // PacketPlayOutNamedEntitySpawn a;
            // EntityPlayer c;
            // PacketPlayOutEntity d;
            // PacketPlayOutSpawnEntityLiving e;

            // EntityHuman b;
            Field profileField = Extra.getField(MineReflect.classMineEntityHuman, "bH");
            Object gameprofile = profileField.get(entityplayer);
            // Object before = Extra.getValue(gameprofile, "name");
            Extra.setValue(gameprofile, "name", displayName);
            // EntityPlayer a;
            // Object packet = Extra.getNew(MineReflect.classPacketPlayOutNamedEntitySpawn,
            // Extra.getParameters(MineReflect.classMineEntityHuman),
            // entityplayer);
            // // Extra.setValue(Extra.getValue(packet, "b"), "name", displayName);
            // sendPackets(packet, player);
            for (Player p : getPlayers()) {
                if (p.equals(player))
                    continue;
                p.hidePlayer(player);
            }
            for (Player p : getPlayers()) {
                if (p.equals(player))
                    continue;
                p.showPlayer(player);
            }
            // Extra.setValue(gameprofile, "name", before);
            // System.out.println(Bukkit.getPlayer(displayName));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }


    /**
     * Definir uma tag customizada
     *
     * @param player Jogador
     * @param prefix Prefixo
     * @param suffix Suffixo
     * @param order  Ordem
     */
    @SuppressWarnings("unused")
    public static void sendNameTag(Player player, String prefix, String suffix, String order) {
        String teamName = UUID.randomUUID().toString().substring(0, 15);

        try {
            Object packet = Extra.getNew("#pPlayOutScoreboardTeam");
            Class<?> clas = packet.getClass();
            Field team_name = Extra.getField(clas, "a");
            Field display_name = Extra.getField(clas, "b");
            Field prefix2 = Extra.getField(clas, "c");
            Field suffix2 = Extra.getField(clas, "d");
            Field members = Extra.getField(clas, "g");
            Field param_int = Extra.getField(clas, "h");
            Field pack_option = Extra.getField(clas, "i");
            Extra.setValue(packet, "a", order + teamName);
            Extra.setValue(packet, "b", player.getName());
            Extra.setValue(packet, "c", prefix);
            Extra.setValue(packet, "d", suffix);
            Extra.setValue(packet, "g", Arrays.asList(new String[]{player.getName()}));
            Extra.setValue(packet, "h", Integer.valueOf(0));
            Extra.setValue(packet, "i", Integer.valueOf(1));
            MineReflect.sendPackets(packet);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Envia um Title para os Jogadores
     *
     * @param title    Titulo
     * @param subTitle SubTitulo
     * @param fadeIn   Tempo de Aparececimento (Ticks)
     * @param stay     Tempo de Passagem (Ticks)
     * @param fadeOut  Tempo de Desaparecimento (Ticks)
     */
    public static void sendTitle(String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        for (Player player : MineReflect.getPlayers()) {
            sendTitle(player, title, subTitle, fadeIn, stay, fadeOut);
        }
    }

    /**
     * Envia um Title para o Jogador
     *
     * @param player   Jogador
     * @param title    Titulo
     * @param subTitle SubTitulo
     * @param fadeIn   Tempo de Aparececimento (Ticks)
     * @param stay     Tempo de Passagem (Ticks)
     * @param fadeOut  Tempo de Desaparecimento (Ticks)
     */
    public static void sendTitle(Player player, String title, String subTitle, int fadeIn, int stay, int fadeOut) {
        try {
            if (isAbove1_8(player)) {

                // sendPacket(player, getNew(PacketTitle, getParameters(Action,
                // int.class, int.class, int.class),
                // getValue(Action, "TIMES"), fadeIn, stay, fadeOut));
                sendPacket(player, Extra.getNew(MineReflect.classSpigotPacketTitle,
                        Extra.getValue(MineReflect.classSpigotAction, "TIMES"), fadeIn, stay, fadeOut));
                sendPacket(player,
                        Extra.getNew(MineReflect.classSpigotPacketTitle,
                                Extra.getParameters(MineReflect.classSpigotAction, MineReflect.classMineIChatBaseComponent),
                                Extra.getValue(MineReflect.classSpigotAction, "TITLE"), getIChatText(title)));
                sendPacket(player,
                        Extra.getNew(MineReflect.classSpigotPacketTitle,
                                Extra.getParameters(MineReflect.classSpigotAction, MineReflect.classMineIChatBaseComponent),
                                Extra.getValue(MineReflect.classSpigotAction, "SUBTITLE"), getIChatText(subTitle)));

                return;
            }

        } catch (Exception e) {
        }
        try {
            sendPacket(player, Extra.getNew(MineReflect.classPacketPlayOutTitle, fadeIn, stay, fadeOut));
            sendPacket(player,
                    Extra.getNew(MineReflect.classPacketPlayOutTitle,
                            Extra.getParameters(MineReflect.classCraftEnumTitleAction, MineReflect.classMineIChatBaseComponent),
                            Extra.getValue(MineReflect.classCraftEnumTitleAction, "TITLE"), getIChatText(title)));
            sendPacket(player,
                    Extra.getNew(MineReflect.classPacketPlayOutTitle,
                            Extra.getParameters(MineReflect.classCraftEnumTitleAction, MineReflect.classMineIChatBaseComponent),
                            Extra.getValue(MineReflect.classCraftEnumTitleAction, "SUBTITLE"), getIChatText(subTitle)));
            return;
        } catch (Exception e) {
        }
        try {
            sendPacket(player, Extra.getNew(MineReflect.classPacketPlayOutTitle, fadeIn, stay, fadeOut));
            sendPacket(player,
                    Extra.getNew(MineReflect.classPacketPlayOutTitle,
                            Extra.getParameters(MineReflect.classPacketEnumTitleAction2, MineReflect.classMineIChatBaseComponent),
                            Extra.getValue(MineReflect.classPacketEnumTitleAction2, "TITLE"), getIChatText2(title)));
            sendPacket(player,
                    Extra.getNew(MineReflect.classPacketPlayOutTitle,
                            Extra.getParameters(MineReflect.classPacketEnumTitleAction2, MineReflect.classMineIChatBaseComponent),
                            Extra.getValue(MineReflect.classPacketEnumTitleAction2, "SUBTITLE"), getIChatText2(subTitle)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * Modifica a TabList do Jogador
     *
     * @param player Jogador
     * @param header Cabeçalho
     * @param footer Rodapé
     */
    public static void setTabList(Player player, String header, String footer) {
        try {
            if (isAbove1_8(player)) {
                Object packet = Extra.getNew(MineReflect.classSpigotPacketTabHeader,
                        Extra.getParameters(MineReflect.classMineIChatBaseComponent, MineReflect.classMineIChatBaseComponent),
                        getIChatText(header), getIChatText(footer));
                sendPacket(packet, player);
                return;
            }

        } catch (Exception e) {
        }
        try {
            Object packet = Extra.getNew(MineReflect.classPacketPlayOutPlayerListHeaderFooter,
                    Extra.getParameters(MineReflect.classMineIChatBaseComponent), getIChatText(header));

            Extra.setValue(packet, "b", getIChatText(footer));
            sendPacket(packet, player);
        } catch (Exception e) {
        }
        try {
            Object packet = Extra.getNew(MineReflect.classPacketPlayOutPlayerListHeaderFooter,
                    Extra.getParameters(MineReflect.classMineIChatBaseComponent), getIChatText2(header));
            Extra.setValue(packet, "b", getIChatText2(footer));
            sendPacket(packet, player);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    /**
     * @param player Jogador
     * @return Se o Jogador esta na versão 1.8 ou pra cima
     */
    public static boolean isAbove1_8(Player player) {
        try {
            return (int) Extra.getResult(Extra.getValue(getConnection(player), "networkManager"), "getVersion") == 47;

        } catch (Exception ex) {
        }
        return false;
    }
    /**
     * @param player Jogador
     * @return Ping do jogador
     */
    public static String getPing(Player player) {
        try {
            return Extra.getValue(getHandle(player), "ping").toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "";
    }

    /**
     * Modifica a Action Bar do Jogador
     *
     * @param player Jogador
     * @param text   Texto
     */
    public static void sendActionBar(Player player, String text) {
        try {
            Object component = getIChatText(text);
            Object packet = Extra.getNew(MineReflect.classPacketPlayOutChat,
                    Extra.getParameters(MineReflect.classMineIChatBaseComponent, byte.class), component, (byte) 2);
            sendPacket(player, packet);
            return;
        } catch (Exception ex) {
        }
        try {
            Object component = getIChatText2(text);
            Object packet = Extra.getNew(MineReflect.classPacketPlayOutChat,
                    Extra.getParameters(MineReflect.classMineIChatBaseComponent, byte.class), component, (byte) 2);
            sendPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
			Bukkit.getConsoleSender().sendMessage(
				"§bRexMine §aNao foi possivel usar o 'setActionBar' pois o servidor esta na versao anterior a 1.8");

        }

    }

    /**
     * Inicia um IChatBaseComponent pelo metodo a(String) da classe ChatSerializer
     *
     * @param component Componente (Texto)
     * @return IChatBaseComponent iniciado
     */
    public static Object getIChatBaseComponent(String component) throws Exception {
        return Extra.getResult(MineReflect.classMineChatSerializer, "a", component);
    }

    /**
     * Inicia um IChatBaseComponent pelo metodo a(String) da classe ChatSerializer
     * adicionando componente texto
     *
     * @param text Texto
     * @return IChatBaseComponent iniciado
     */
    public static Object getIChatText(String text) throws Exception {
        return getIChatBaseComponent(getIComponentText(text));
    }

    /**
     * Inicia um ChatComponentText"IChatBaseComponent" pelo cons(String) da classe
     * ChatComponentText
     *
     * @param text Texto
     * @return ChatComponentText iniciado
     */
    public static Object getIChatText2(String text) throws Exception {
        return Extra.getNew(MineReflect.classMineChatComponentText, text);

    }

    /**
     * @param text Texto
     * @return "{\"text\":\"" + text + "\"}"
     */
    public static String getIComponentText(String text) {
        return ("{\"text\":\"" + text + "\"}");

    }
    public static void sendActionBar(String message) {
        for (Player player : MineReflect.getPlayers()) {
            MineReflect.sendActionBar(player, message);
        }
    }
    /**
     * @return Lista de jogadores do servidor
     */
    public static List<Player> getPlayers() {
        List<Player> list = new ArrayList<>();
        try {

            Object object = Extra.getResult(MineReflect.classBukkitBukkit, "getOnlinePlayers");
            if (object instanceof Collection) {
                Collection<?> players = (Collection<?>) object;
                for (Object obj : players) {
                    if (obj instanceof Player) {
                        Player p = (Player) obj;
                        list.add(p);
                    }
                }
            } else if (object instanceof Player[]) {
                Player[] players = (Player[]) object;
                for (Player p : players) {
                    list.add(p);
                }
            }
        } catch (Exception e) {
        }

        return list;
    }

    /**
     * Envia o pacote para o jogador
     *
     * @param player Jogador
     * @param packet Pacote
     * @throws Exception
     */
    public static void sendPacket(Object packet, Player player) throws Exception {

        Extra.getResult(getConnection(player), "sendPacket", Extra.getParameters(MineReflect.classPacketPacket), packet);
    }

    /**
     * Envia o pacote para o jogador
     *
     * @param player Jogador
     * @param packet Pacote
     * @throws Exception
     */
    public static void sendPacket(Player player, Object packet) throws Exception {
        sendPacket(packet, player);
    }

    /**
     * Envia o pacote para todos jogadores
     *
     * @param packet Pacote
     */
    public static void sendPackets(Object packet) {
        for (Player p : getPlayers()) {
            try {
                sendPacket(packet, p);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Envia o pacote para todos menos para o jogador
     *
     * @param packet Pacote
     * @param target Jogador
     */
    public static void sendPackets(Object packet, Player target) {
        for (Player player : getPlayers()) {
            if (player.equals(target))
                continue;
            try {
                sendPacket(packet, player);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * @param player Jogador (CraftPlayer)
     * @return EntityPlayer pelo metodo getHandle da classe CraftPlayer(Player)
     * @throws Exception
     */
    public static Object getHandle(Player player) throws Exception {
        return Extra.getResult(player, "getHandle");
    }
    /**
     * Retorna Um PlayerConnection pela variavel playerConnection da classe
     * EntityPlayer <Br>
     * Pega o EntityPlayer pelo metodo getHandle(player)
     *
     * @param player Jogador (CraftPlayer)
     * @return Conexão do jogador
     */
    public static Object getConnection(Player player) throws Exception {
        return Extra.getValue(getHandle(player), "playerConnection");
    }
    /**
     * Força o Respawn do Jogador (Respawn Automatico)
     *
     * @param player Jogador
     */
    public static void makeRespawn(Player player) {
        try {
            Object packet = Extra.getNew(MineReflect.classPacketPlayInClientCommand,
                    Extra.getValue(MineReflect.classMineEnumClientCommand, "PERFORM_RESPAWN"));
            Extra.getResult(getConnection(player), "a", packet);

        } catch (Exception ex) {
            try {
                Object packet = Extra.getNew(MineReflect.classPacketPlayInClientCommand,
                        Extra.getValue(MineReflect.classMineEnumClientCommand2, "PERFORM_RESPAWN"));
                Extra.getResult(getConnection(player), "a", packet);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
