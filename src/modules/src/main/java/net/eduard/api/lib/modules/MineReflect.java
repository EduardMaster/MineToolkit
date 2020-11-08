package net.eduard.api.lib.modules;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Classe que constitui métodos com Reflection para funções que não possui na Bukkit API
 * <br>
 * Nome antigo: RexAPI
 *
 * @version 1.0
 * @since 02/06/2020
 */
@SuppressWarnings("unused")
public class MineReflect {
    /**
     * INICIO Métodos ABAIXO
     */

    public static String MSG_ITEM_STACK = "§aQuantidade: §f$stack";

    public static ItemStack toStack(ItemStack original, double amount) {

        List<String> lore = getLore(original);
        lore.add(MineReflect.MSG_ITEM_STACK.replace("$stack", Extra.formatMoney(amount)));
        setLore(original, lore);
        ItemExtraData data = getData(original);
        data.setCustomStack(amount);
        original = setData(original, data);
        return original;
    }

    /**
     * Pega o descrição do Item
     *
     * @param item Item
     * @return Descrição
     */
    private static List<String> getLore(ItemStack item) {
        if (item != null) {
            if (item.hasItemMeta() && item.getItemMeta().hasLore()) {
                return item.getItemMeta().getLore();
            }
        }
        return new ArrayList<>();
    }

    /**
     * Modifica a Descrição do Item
     *
     * @param item Item
     * @param lore Descrição
     */
    private static void setLore(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setLore(lore);
            item.setItemMeta(meta);
        }
    }

    public static class ItemExtraData {


        private Object nbt;

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
                return (boolean) getString.invoke(nbt, key);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        public String getString(String key) {
            if (has(key)) {
                try {
                    Method getString = Extra.getMethod(MineReflect.classMineNBTTagCompound, "getString", String.class);
                    return (String) getString.invoke(nbt, key);
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
                    return (double) getString.invoke(nbt, key);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return 0;

        }


        public void setDouble(String key, double value) {
            try {
                Method setDouble = Extra.getMethod(MineReflect.classMineNBTTagCompound, "setDouble", String.class, double.class);
                setDouble.invoke(nbt, key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }


        }


        public void setString(String key, String value) {
            try {
                Method setDouble = Extra.getMethod(MineReflect.classMineNBTTagCompound, "setString", String.class, String.class);
                setDouble.invoke(nbt, key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setNBT(Object nbtcompound) {
            this.nbt = nbtcompound;
        }

        public Object getNBT() {
            return nbt;
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
            return getTag.invoke(itemCopia);

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
            Object itemModified; //= asCraftMirror.invoke(0, itemCopia);

            Method asBukkitCopy = Extra.getMethod(MineReflect.classCraftItemStack, "asBukkitCopy", MineReflect.classMineItemStack);
            itemModified = asBukkitCopy.invoke(0, itemCopia);
            if (itemModified != null)
                return (ItemStack) itemModified;

        } catch (Exception e) {
            // e.printStackTrace();
        }
        return item;
    }

    /**
     * @return Versão do Servidor
     */
    public static String getVersion() {

        try {
            return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
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

    /**
     * Se terminar com 2, é porque a classe esta dentro de outra classe
     */
    public static String classBukkitBukkit = "#bBukkit";
    public static String classMineMinecraftServer = "#mMinecraftServer";
    public static String classMineEntityPlayer = "#mEntityPlayer";
    public static String classCraftCraftPlayer = "#cCraftPlayer";
    public static String classMineEntityHuman = "#mEntityHuman";
    public static String classSpigotPacketTitle = "#sProtocolInjector$PacketTitle";
    public static String classSpigotAction = "#sProtocolInjector$PacketTitle$Action";
    public static String classSpigotPacketTabHeader = "#sProtocolInjector$PacketTabHeader";

    public static String classPacketPlayOutChat = "#pPlayOutChat";
    public static String classPacketPlayOutTitle = "#pPlayOutTitle";
    public static String classPacketPlayOutWorldParticles = "#pPlayOutWorldParticles";
    public static String classPacketPlayOutPlayerListHeaderFooter = "#pPlayOutPlayerListHeaderFooter";
    public static String classPacketPlayOutNamedEntitySpawn = "#pPlayOutNamedEntitySpawn";
    public static String classCraftEnumTitleAction = "#cEnumTitleAction";
    public static String classPacketEnumTitleAction2 = "#pPlayOutTitle$EnumTitleAction";


    /**
     * Responsavel por Ações acontecerem no Cliente
     */
    public static String classPacketPlayInClientCommand = "#pPlayInClientCommand";
    public static String classMineEnumClientCommand = "#mEnumClientCommand";
    public static String classMineEnumClientCommand2 = "#pPlayInClientCommand$EnumClientCommand";


    /**
     * Classes para criar Textos, e componentes de Textos
     */
    public static String classMineIChatBaseComponent = "#mIChatBaseComponent";
    public static String classMineChatComponentText = "#mChatComponentText";
    public static String classMineChatSerializer = "#mChatSerializer";
    public static String classMineChatSerializer2 = "#mIChatBaseComponent$ChatSerializer";


    /**
     * Classes abaixo relacionado a items
     */
    public static String classMineNBTTagCompound = "#mNBTTagCompound";
    public static String classMineNBTBase = "#mNBTBase";
    public static String classMineNBTTagList = "#mNBTTagList";
    public static String classPacketPacket = "#p";
    public static String classCraftItemStack = "#cinventory.CraftItemStack";
    public static String classMineItemStack = "#mItemStack";
    public static String classBukkitItemStack = "#bItemStack";


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
            return Math.min(20.0D, Math.round(MineReflect.getCurrentTick() * 10) / 10.0D);
        } catch (Exception ignored) {
        }

        return 0D;
    }

    public static int getCurrentTick() throws Exception {
        return (int) Extra.getFieldValue(MineReflect.classMineMinecraftServer, "currentTick");
    }

    /**
     * Altera a stack máxima do Item
     *
     * @param itemOriginal Item Original
     * @param amount       Quantidade
     * @return ItemStack novo
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
            Object getHandle = Extra.getMethodInvoke(entity, "getHandle");
            Extra.getMethodInvoke(getHandle, "c", compound);
            Extra.getMethodInvoke(compound, "setByte", "NoAI", (byte) 1);
            Extra.getMethodInvoke(getHandle, "f", compound);

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
            // Object before = Extra.getFieldValue(gameprofile, "name");
            Extra.setFieldValue(gameprofile, "name", displayName);
            // EntityPlayer a;
            // Object packet = Extra.getNew(MineReflect.classPacketPlayOutNamedEntitySpawn,
            // Extra.getParameters(MineReflect.classMineEntityHuman),
            // entityplayer);
            // // Extra.setFieldValue(Extra.getFieldValue(packet, "b"), "name", displayName);
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
            // Extra.setFieldValue(gameprofile, "name", before);
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
            Extra.setFieldValue(packet, "a", order + teamName);
            Extra.setFieldValue(packet, "b", player.getName());
            Extra.setFieldValue(packet, "c", prefix);
            Extra.setFieldValue(packet, "d", suffix);
            Extra.setFieldValue(packet, "g", Collections.singletonList(player.getName()));
            Extra.setFieldValue(packet, "h", 0);
            Extra.setFieldValue(packet, "i", 1);
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
                sendPacket(player, Extra.getNew(MineReflect.classSpigotPacketTitle,
                        Extra.getFieldValue(MineReflect.classSpigotAction, "TIMES"), fadeIn, stay, fadeOut));
                sendPacket(player,
                        Extra.getNew(MineReflect.classSpigotPacketTitle,
                                Extra.getParameters(MineReflect.classSpigotAction, MineReflect.classMineIChatBaseComponent),
                                Extra.getFieldValue(MineReflect.classSpigotAction, "TITLE"), getChatComponentText(title)));
                sendPacket(player,
                        Extra.getNew(MineReflect.classSpigotPacketTitle,
                                Extra.getParameters(MineReflect.classSpigotAction, MineReflect.classMineIChatBaseComponent),
                                Extra.getFieldValue(MineReflect.classSpigotAction, "SUBTITLE"), getChatComponentText(subTitle)));

                return;
            }

        } catch (Exception ignored) {
        }
        try {
            sendPacket(player, Extra.getNew(MineReflect.classPacketPlayOutTitle, fadeIn, stay, fadeOut));
            sendPacket(player,
                    Extra.getNew(MineReflect.classPacketPlayOutTitle,
                            Extra.getParameters(MineReflect.classCraftEnumTitleAction, MineReflect.classMineIChatBaseComponent),
                            Extra.getFieldValue(MineReflect.classCraftEnumTitleAction, "TITLE"), getChatComponentText(title)));
            sendPacket(player,
                    Extra.getNew(MineReflect.classPacketPlayOutTitle,
                            Extra.getParameters(MineReflect.classCraftEnumTitleAction, MineReflect.classMineIChatBaseComponent),
                            Extra.getFieldValue(MineReflect.classCraftEnumTitleAction, "SUBTITLE"), getChatComponentText(subTitle)));
            return;
        } catch (Exception ignored) {
        }
        try {
            sendPacket(player, Extra.getNew(MineReflect.classPacketPlayOutTitle, fadeIn, stay, fadeOut));
            sendPacket(player,
                    Extra.getNew(MineReflect.classPacketPlayOutTitle,
                            Extra.getParameters(MineReflect.classPacketEnumTitleAction2, MineReflect.classMineIChatBaseComponent),
                            Extra.getFieldValue(MineReflect.classPacketEnumTitleAction2, "TITLE"), getChatComponentText(title)));
            sendPacket(player,
                    Extra.getNew(MineReflect.classPacketPlayOutTitle,
                            Extra.getParameters(MineReflect.classPacketEnumTitleAction2, MineReflect.classMineIChatBaseComponent),
                            Extra.getFieldValue(MineReflect.classPacketEnumTitleAction2, "SUBTITLE"), getChatComponentText(subTitle)));
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
                        getChatComponentText(header), getChatComponentText(footer));
                sendPacket(packet, player);
                return;
            }

        } catch (Exception ignored) {
        }
        try {
            Object packet = Extra.getNew(MineReflect.classPacketPlayOutPlayerListHeaderFooter,
                    Extra.getParameters(MineReflect.classMineIChatBaseComponent), getChatComponentText(header));


            Extra.setFieldValue(packet, "b", getChatComponentText(footer));
            sendPacket(packet, player);
        } catch (Exception ignored) {
        }
        try {
            Object packet = Extra.getNew(MineReflect.classPacketPlayOutPlayerListHeaderFooter,
                    Extra.getParameters(MineReflect.classMineIChatBaseComponent), getChatComponentText(header));
            Extra.setFieldValue(packet, "b", getChatComponentText(footer));
            sendPacket(packet, player);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * @param player Jogador
     * @return Se o Jogador esta na cima versão 1.8
     */
    public static boolean isAbove1_8(Player player) {
        try {
            return (int) Extra.getMethodInvoke(Extra.getFieldValue(getConnection(player), "networkManager"), "getVersion") == 47;

        } catch (Exception ignored) {
        }
        return false;
    }

    /**
     * @param player Jogador
     * @return Ping do jogador
     */
    public static String getPing(Player player) {
        try {
            return Extra.getFieldValue(getHandle(player), "ping").toString();
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
            Object component = getChatComponentText(text);
            Object packet = Extra.getNew(MineReflect.classPacketPlayOutChat,
                    Extra.getParameters(MineReflect.classMineIChatBaseComponent, byte.class), component, (byte) 2);
            sendPacket(player, packet);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Inicia um IChatBaseComponent pelo metodo a(String) da classe ChatSerializer
     *
     * @param component Componente (Texto)
     * @return IChatBaseComponent iniciado
     */
    public static String getChatComponentJSON(Object component) throws Exception {

        try {
            return (String) Extra.getMethodInvoke(MineReflect.classMineChatSerializer, "a", new Object[]{
                    classMineIChatBaseComponent
            }, component);
        } catch (Exception ex) {
            return (String) Extra.getMethodInvoke(MineReflect.classMineChatSerializer2, "a", new Object[]{
                    classMineIChatBaseComponent
            }, component);
        }
    }

    /**
     * Inicia um IChatBaseComponent pelo metodo a(String) da classe ChatSerializer
     * adicionando componente texto
     *
     * @param component Texto
     * @return IChatBaseComponent iniciado
     */
    public static Object getChatComponent(String component) throws Exception {
        try {
            return Extra.getMethodInvoke(MineReflect.classMineChatSerializer, "a", component);
        } catch (Exception ex) {
            return Extra.getMethodInvoke(MineReflect.classMineChatSerializer2, "a", component);
        }
    }

    /**
     * Inicia um ChatComponentText"IChatBaseComponent" pelo init(String) da classe
     * ChatComponentText
     *
     * @param text Texto
     * @return ChatComponentText iniciado
     */
    public static Object getChatComponentText(String text) throws Exception {

        return Extra.getNew(MineReflect.classMineChatComponentText, text);

    }

    /**
     * @param rawText Texto
     * @return "{\"text\":\"" + text + "\"}"
     */
    private static String getChatJsonByRaw(String rawText) {
        return ("{\"text\":\"" + rawText + "\"}");
    }

    public static void sendActionBar(String message) {
        for (Player player : MineReflect.getPlayers()) {
            MineReflect.sendActionBar(player, message);
        }
    }

    /**
     * @return Lista de jogadores do servidor
     */
    private static List<Player> getPlayers() {
        List<Player> list = new ArrayList<>();
        try {

            Object object = Extra.getMethodInvoke(MineReflect.classBukkitBukkit, "getOnlinePlayers");
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
                Collections.addAll(list, players);
            }
        } catch (Exception ignored) {
        }

        return list;
    }

    /**
     * Envia o pacote para o jogador
     *
     * @param player Jogador
     * @param packet Pacote
     * @throws Exception Erro
     */
    public static void sendPacket(Object packet, Player player) throws Exception {

        Extra.getMethodInvoke(getConnection(player), "sendPacket", Extra.getParameters(MineReflect.classPacketPacket), packet);
    }

    /**
     * Envia o pacote para o jogador
     *
     * @param player Jogador
     * @param packet Pacote
     * @throws Exception Erro
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
     * @throws Exception Erro
     */
    public static Object getHandle(Player player) throws Exception {
        return Extra.getMethodInvoke(player, "getHandle");
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
        return Extra.getFieldValue(getHandle(player), "playerConnection");
    }

    /**
     * Força o Respawn do Jogador (Respawn Automatico)
     *
     * @param player Jogador
     */
    public static void makeRespawn(Player player) {
        try {
            Object packet = Extra.getNew(MineReflect.classPacketPlayInClientCommand,
                    Extra.getFieldValue(MineReflect.classMineEnumClientCommand, "PERFORM_RESPAWN"));
            Extra.getMethodInvoke(getConnection(player), "a", packet);

        } catch (Exception ex) {
            try {

                Object packet = Extra.getNew(MineReflect.classPacketPlayInClientCommand,
                        Extra.getFieldValue(MineReflect.classMineEnumClientCommand2, "PERFORM_RESPAWN"));
                Extra.getMethodInvoke(getConnection(player), "a", packet);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
