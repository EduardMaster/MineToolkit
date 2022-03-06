package net.eduard.api.lib.modules;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.spigotmc.AsyncCatcher;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Extensão da Bukkit API com métodos que utilizam Reflection para Interagir com NMS<br>
 * Independentemente da Versão do servidor vai funcionar os Métodos exceto
 * <br>
 * Versão 1.17 e Superior
 *
 * <br>
 * Nome antigo: RexAPI
 *
 * @version 1.1
 * @since 02/06/2020
 */
@SuppressWarnings("unused")
public class MineReflect {
    private static String version;

    /**
     * @return Versão do Servidor
     */
    public static String getVersion() {
        try {
            if (version == null) {
                version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            }
            return version;
        } catch (Exception ignored) {
        }
        return "";
    }

    private static Class<?> class_MinecraftServer;
    private static Class<?> class_NBTCompound;
    private static Class<?> class_NBTBase;
    private static Class<?> class_CraftItemStack;
    private static Class<?> class_NMSItemStack;
    private static Class<?> enumclass_ClientCommand;
    private static Class<?> class_NMSPlayerConnection;
    private static Class<?> class_Packet;
    private static Method method_asNMSCopy;
    private static Method method_asBukkitCopy;
    private static Method method_getItem;
    private static Method method_NMSItemStack_setMaxDurability;
    private static Method method_forcePlayerRespawn;
    private static Method method_NBT_hasKey;
    private static Method method_NBT_getString;
    private static Method method_NBT_getBoolean;
    private static Method method_NBT_getInt;
    private static Method method_NBT_getDouble;
    private static Method method_NBT_getLong;
    private static Method method_NBT_setLong;
    private static Method method_NBT_setDouble;
    private static Method method_NBT_setInt;
    private static Method method_NBT_setBoolean;
    private static Method method_NBT_setString;
    private static Method method_NBT_set;
    private static Method method_NBT_get;
    private static Method method_NMSItemStack_setTag;
    private static Method method_NMSItemStack_getTag;
    private static Method method_sendPacket;

    static {
        try {

            Extra.newReplacer("#v", MineReflect.getVersion());
            Extra.newReplacer("#ms", "net.minecraft.server.");
            Extra.newReplacer("#mn", "net.minecraft.nbt.");
            class_MinecraftServer = Extra.getClassFrom("#mMinecraftServer");
            if (class_MinecraftServer == null) {
                class_MinecraftServer = Extra.getClassFrom("#ms.MinecraftServer");
            }
            class_NBTCompound = Extra.getClassFrom("#mNBTTagCompound");
            if (class_NBTCompound == null) {
                class_NBTCompound = Extra.getClassFrom("#mnNBTTagCompound");
            }
            class_NBTBase = Extra.getClassFrom("#mNBTBase");
            if (class_NBTBase == null) {
                class_NBTBase = Extra.getClassFrom("#mnNBTBase");
            }
            class_CraftItemStack = Extra.getClassFrom("#cinventory.CraftItemStack");
            class_NMSItemStack = Extra.getClassFrom("#mItemStack");
            if (class_NMSItemStack == null) {
                class_NMSItemStack = Extra.getClassFrom("net.minecraft.world.item.ItemStack");
            }
            class_NMSPlayerConnection = Extra.getClassFrom("#mPlayerConnection");
            if (class_NMSPlayerConnection == null) {
                class_NMSPlayerConnection = Extra.getClassFrom("#ms.network.PlayerConnection");
            }
            class_Packet = Extra.getClassFrom("#p");
            if (class_Packet == null) {
                class_Packet = Extra.getClassFrom("#ms.network.Packet");
            }
            method_asNMSCopy = Extra.getMethod(class_CraftItemStack, "asNMSCopy", ItemStack.class);
            method_asBukkitCopy = Extra.getMethod(class_CraftItemStack, "asBukkitCopy", class_NMSItemStack);
            method_getItem = Extra.getMethod(class_NMSItemStack, "getItem");
            method_NMSItemStack_getTag = Extra.getMethod(class_NMSItemStack, "getTag");
            method_NBT_hasKey = Extra.getMethod(class_NBTCompound, "hasKey", String.class);
            method_NBT_getString = Extra.getMethod(class_NBTCompound, "getString", String.class);
            method_NBT_getBoolean = Extra.getMethod(class_NBTCompound, "getBoolean", String.class);
            method_NBT_getInt = Extra.getMethod(class_NBTCompound, "getInt", String.class);
            method_NBT_getDouble = Extra.getMethod(class_NBTCompound, "getDouble", String.class);
            method_NBT_getLong = Extra.getMethod(class_NBTCompound, "getLong", String.class);
            method_NMSItemStack_setTag = Extra.getMethod(class_NMSItemStack, "setTag", class_NBTCompound);
            method_NBT_setDouble = Extra.getMethod(class_NBTCompound, "setDouble", String.class, double.class);
            method_NBT_setInt = Extra.getMethod(class_NBTCompound, "setInt", String.class, int.class);
            method_NBT_setLong = Extra.getMethod(class_NBTCompound, "setLong", String.class, long.class);
            method_NBT_setString = Extra.getMethod(class_NBTCompound, "setString", String.class, String.class);
            method_NBT_setBoolean = Extra.getMethod(class_NBTCompound, "setBoolean", String.class, boolean.class);
            method_NBT_set = Extra.getMethod(class_NBTCompound, "set", String.class, class_NBTBase);
            method_NBT_get = Extra.getMethod(class_NBTCompound, "get", String.class);
            method_sendPacket = Extra.getMethod(class_NMSPlayerConnection, "sendPacket", class_Packet);
            method_NMSItemStack_setMaxDurability = Extra.getMethod(class_NMSItemStack, "c", int.class);

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    /**
     * Se terminar com 2, é porque a classe esta dentro de outra classe
     */
    public static String classBukkitBukkit = "#bBukkit";

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


    public static String classMineNBTTagList = "#mNBTTagList";

    static {
        try {
            Mine.console("§c" + Extra.getMethod("#centity.CraftPlayer", "getHandle"));
            Mine.console("§c" + Extra.getField("#mEntityPlayer", "playerConnection"));
            Mine.console("§c" + Extra.getMethod("#mPlayerConnection", "sendPacket", "#p"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Bloqueia uso de Async na execução do Método atual
     *
     * @param message Mensagem
     */
    public static void asyncCatch(String message) {
        AsyncCatcher.catchOp(message);
    }

    public static boolean isOnMainThread() {

        // Thread.currentThread() == MinecraftServer.getServer().primaryThread
        return Bukkit.isPrimaryThread();
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
            Extra.getMethodInvoke(getPlayerConnection(player), "a", packet);

        } catch (Exception ex) {
            try {

                Object packet = Extra.getNew(MineReflect.classPacketPlayInClientCommand,
                        Extra.getFieldValue(MineReflect.classMineEnumClientCommand2, "PERFORM_RESPAWN"));
                Extra.getMethodInvoke(getPlayerConnection(player), "a", packet);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
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
            Object nmsItemStack = method_asNMSCopy.invoke(0, itemOriginal);
            Object nmsItem = method_getItem.invoke(nmsItemStack);
            method_NMSItemStack_setMaxDurability.invoke(nmsItem, amount);
            Object newItem = method_asBukkitCopy.invoke(0, nmsItemStack);
            return (ItemStack) newItem;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    /**
     * INICIO Métodos ABAIXO
     */

    public static String LORE_ITEM_STACK = "§aQuantidade: §f%stack";

    public static ItemStack toStack(ItemStack original, double amount) {
        List<String> lore = null;
        ItemMeta meta = original.getItemMeta();
        if (meta != null) {
            lore = meta.getLore();
            if (lore == null) {
                lore = new ArrayList<>();
            }
        }
        lore.add(MineReflect.LORE_ITEM_STACK.replace("%stack", Extra.formatMoney(amount)));
        if (meta != null) {
            meta.setLore(lore);
            original.setItemMeta(meta);
        }
        ItemExtraData data = getData(original);
        data.setCustomStack(amount);
        return setData(original, data);
    }


    /**
     * API criada para gerenciar HashMap de NBT (NBTCompound)
     * via Reflection
     *
     * @version 1.0
     * @since 19/09/2021
     */
    public static class NBTReflection {
        private Object nbtMap;

        public NBTReflection(Object currentNBT) {
            this.nbtMap = currentNBT;
        }

        public Object getNbtMap() {
            return nbtMap;
        }

        public void setNbtMap(Object nbtMap) {
            this.nbtMap = nbtMap;
        }

        public boolean has(String key) {
            try {
                return (boolean) method_NBT_hasKey.invoke(nbtMap, key);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        public String getString(String key) {
            try {
                return (String) method_NBT_getString.invoke(nbtMap, key);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public boolean getBoolean(String key) {
            try {
                return (boolean) method_NBT_getBoolean.invoke(nbtMap, key);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        public int getInt(String key) {
            try {
                return (int) method_NBT_getInt.invoke(nbtMap, key);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }

        public long getLong(String key) {
            try {
                return (long) method_NBT_getLong.invoke(nbtMap, key);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0L;
        }

        public double getDouble(String key) {
            try {
                return (double) method_NBT_getDouble.invoke(nbtMap, key);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }


        public void setDouble(String key, double value) {
            try {
                method_NBT_setDouble.invoke(nbtMap, key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setNBT(String key, Object newNBT) {
            try {
                method_NBT_set.invoke(nbtMap, key, newNBT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public Object getNBT(String key) {
            try {
                return method_NBT_get.invoke(nbtMap, key);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public void setInt(String key, int value) {
            try {
                method_NBT_setInt.invoke(nbtMap, key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setLong(String key, long value) {
            try {
                method_NBT_setLong.invoke(nbtMap, key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setBoolean(String key, boolean value) {
            try {
                method_NBT_setBoolean.invoke(nbtMap, key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void setString(String key, String value) {
            try {
                method_NBT_setString.invoke(nbtMap, key, value);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static class ItemExtraData {
        private NBTReflection itemNBT;
        private NBTReflection customNBT;

        public ItemExtraData(Object itemStackNBT) {
            setItemNBT(new NBTReflection(itemStackNBT));
            if (itemNBT.has("EXTRA_DATA")) {
                setCustomNBT(new NBTReflection(itemNBT.getNBT("EXTRA_DATA")));
            } else {
                setCustomNBT(new NBTReflection(emptyNBT()));
                itemNBT.setNBT("EXTRA_DATA", customNBT.getNbtMap());
            }

        }

        public boolean has(String key) {
            return customNBT.has(key);
        }

        public void setDouble(String key, double value) {
            customNBT.setDouble(key, value);
        }

        public void setInt(String key, int value) {
            customNBT.setInt(key, value);
        }

        public void setLong(String key, long value) {
            customNBT.setLong(key, value);
        }

        public void setBoolean(String key, boolean flag) {
            customNBT.setBoolean(key, flag);
        }


        public double getDouble(String key) {
            return customNBT.getDouble(key);
        }

        public void setString(String key, String value) {
            customNBT.setString(key, value);
        }

        public String getString(String key) {
            return customNBT.getString(key);
        }

        public int getInt(String key) {
            return customNBT.getInt(key);
        }

        public long getLong(String key) {
            return customNBT.getLong(key);
        }

        public boolean getBoolean(String key) {
            return customNBT.getBoolean(key);
        }

        public String getUniqueId() {
            return customNBT.getString("UNIQUE_ID");
        }

        public double getCustomStack() {
            return customNBT.getDouble("CUSTOM_STACK");
        }

        public void setUniqueId(String uniqueId) {
            customNBT.setString("UNIQUE_ID", uniqueId);
        }

        public void setCustomStack(double stack) {
            customNBT.setDouble("CUSTOM_STACK", stack);
        }

        public void setCustomNBT(NBTReflection customNBT) {
            this.customNBT = customNBT;
        }

        public void setItemNBT(NBTReflection itemNBT) {
            this.itemNBT = itemNBT;
        }

        public NBTReflection getCustomNBT() {
            return customNBT;
        }

        public NBTReflection getItemNBT() {
            return itemNBT;
        }
    }

    private static Object emptyNBT() {
        try {
            return Extra.getNew(class_NBTCompound);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ItemExtraData getData(ItemStack item) {
        if (item != null) {
            Object nbt = getNBT(item);
            if (nbt != null)
                return new ItemExtraData(nbt);
        }
        return new ItemExtraData(emptyNBT());
    }


    private static Object getNBT(ItemStack item) {
        try {
            Object itemCopia = method_asNMSCopy.invoke(0, item);
            if (itemCopia == null) {
                return null;
            }
            return method_NMSItemStack_getTag.invoke(itemCopia);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ItemStack setData(ItemStack item, ItemExtraData data) {
        try {

            Object itemCopia = method_asNMSCopy.invoke(0, item);
            if (itemCopia != null) {
                method_NMSItemStack_setTag.invoke(itemCopia, data.getItemNBT().getNbtMap());
                //Method asCraftMirror = Extra.getMethod(class_CraftItemStack,"asCraftMirror", MineReflect.classMineItemStack);
                Object itemModified; //= asCraftMirror.invoke(0, itemCopia);
                itemModified = method_asBukkitCopy.invoke(0, itemCopia);
                if (itemModified != null)
                    return (ItemStack) itemModified;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return item;
    }

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
        return 1D;
    }

    public static int getCurrentTick() throws Exception {
        return (int) Extra.getFieldValue(class_MinecraftServer, "currentTick");
    }


    /**
     * Desabilita a Inteligência da Entidade<br>
     *
     * @param entity Entidade
     */
    public static void disableAI(Entity entity) {
        try {
            Object compound = Extra.getNew(class_NBTCompound);
            Object getHandle = Extra.getMethodInvoke(entity, "getHandle");
            Extra.getMethodInvoke(getHandle, "c", compound);
            Method method = Extra.getMethod(compound, "setByte", String.class, byte.class);
            method.invoke(compound, "NoAI", (byte) 1);
            //Extra.getMethodInvoke(compound, "setByte", "NoAI", (byte) 1);
            Extra.getMethodInvoke(getHandle, "f", compound);
        } catch (Exception exception) {
            exception.printStackTrace();
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
            Object playerHandle = getPlayerHandle(player);
            // PacketPlayOutNamedEntitySpawn a;
            // EntityPlayer c;
            // PacketPlayOutEntity d;
            // PacketPlayOutSpawnEntityLiving e;

            // EntityHuman b;
            Field profileField = Extra.getField(MineReflect.classMineEntityHuman, "bH");
            Object gameProfile = profileField.get(playerHandle);
            // Object before = Extra.getFieldValue(gameprofile, "name");
            Extra.setFieldValue(gameProfile, "name", displayName);
            // EntityPlayer a;
            // Object packet = Extra.getNew(MineReflect.classPacketPlayOutNamedEntitySpawn,
            // Extra.getParameters(MineReflect.classMineEntityHuman),
            // entityplayer);
            // // Extra.setFieldValue(Extra.getFieldValue(packet, "b"), "name", displayName);
            // sendPackets(packet, player);
            for (Player playerLoop : getPlayers()) {
                if (playerLoop.equals(player))
                    continue;
                playerLoop.hidePlayer(player);
            }
            for (Player playerLoop : getPlayers()) {
                if (playerLoop.equals(player))
                    continue;
                playerLoop.showPlayer(player);
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
                        new Object[]{MineReflect.classSpigotAction, int.class, int.class, int.class},
                        Extra.getFieldValue(MineReflect.classSpigotAction, "TIMES"), fadeIn, stay, fadeOut));
                sendPacket(player, Extra.getNew(MineReflect.classSpigotPacketTitle,
                        Extra.getParametersTypes(MineReflect.classSpigotAction, MineReflect.classMineIChatBaseComponent),
                        Extra.getFieldValue(MineReflect.classSpigotAction, "TITLE"), getChatComponentText(title)));
                sendPacket(player, Extra.getNew(MineReflect.classSpigotPacketTitle,
                        Extra.getParametersTypes(MineReflect.classSpigotAction, MineReflect.classMineIChatBaseComponent),
                        Extra.getFieldValue(MineReflect.classSpigotAction, "SUBTITLE"), getChatComponentText(subTitle)));

                return;
            }

        } catch (Exception ignored) {
        }
        try {
            sendPacket(player, Extra.getNew(MineReflect.classPacketPlayOutTitle, new Object[]{int.class, int.class, int.class}, fadeIn, stay, fadeOut));
            sendPacket(player,
                    Extra.getNew(MineReflect.classPacketPlayOutTitle,
                            Extra.getParametersTypes(MineReflect.classCraftEnumTitleAction, MineReflect.classMineIChatBaseComponent),
                            Extra.getFieldValue(MineReflect.classCraftEnumTitleAction, "TITLE"), getChatComponentText(title)));
            sendPacket(player,
                    Extra.getNew(MineReflect.classPacketPlayOutTitle,
                            Extra.getParametersTypes(MineReflect.classCraftEnumTitleAction, MineReflect.classMineIChatBaseComponent),
                            Extra.getFieldValue(MineReflect.classCraftEnumTitleAction, "SUBTITLE"), getChatComponentText(subTitle)));
            return;
        } catch (Exception ignored) {
        }
        try {

            sendPacket(player, Extra.getNew(MineReflect.classPacketPlayOutTitle,
                    new Object[]{int.class, int.class, int.class}, fadeIn, stay, fadeOut));

            sendPacket(player,
                    Extra.getNew(MineReflect.classPacketPlayOutTitle,
                            Extra.getParametersTypes(MineReflect.classPacketEnumTitleAction2, MineReflect.classMineIChatBaseComponent),
                            Extra.getFieldValue(MineReflect.classPacketEnumTitleAction2, "TITLE"), getChatComponentText(title)));
            sendPacket(player,
                    Extra.getNew(MineReflect.classPacketPlayOutTitle,
                            Extra.getParametersTypes(MineReflect.classPacketEnumTitleAction2, MineReflect.classMineIChatBaseComponent),
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
                        Extra.getParametersTypes(MineReflect.classMineIChatBaseComponent, MineReflect.classMineIChatBaseComponent),
                        getChatComponentText(header), getChatComponentText(footer));
                sendPacket(packet, player);
                return;
            }

        } catch (Exception ignored) {
        }
        try {
            Object packet = Extra.getNew(MineReflect.classPacketPlayOutPlayerListHeaderFooter,
                    Extra.getParametersTypes(MineReflect.classMineIChatBaseComponent), getChatComponentText(header));


            Extra.setFieldValue(packet, "b", getChatComponentText(footer));
            sendPacket(packet, player);
        } catch (Exception ignored) {
        }
        try {
            Object packet = Extra.getNew(MineReflect.classPacketPlayOutPlayerListHeaderFooter,
                    Extra.getParametersTypes(MineReflect.classMineIChatBaseComponent), getChatComponentText(header));
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
            return (int) Extra.getMethodInvoke(Extra.getFieldValue(getPlayerConnection(player), "networkManager"), "getVersion") == 47;

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
            return Extra.getFieldValue(getPlayerHandle(player), "ping").toString();
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
            Object packet = Extra.getNew(MineReflect.classPacketPlayOutChat, Extra.getParametersTypes(MineReflect.classMineIChatBaseComponent, byte.class), component, (byte) 2);
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
            return (String) Extra.getMethodInvoke(MineReflect.classMineChatSerializer, "a", new Object[]{classMineIChatBaseComponent}, component);
        } catch (Exception ex) {
            return (String) Extra.getMethodInvoke(MineReflect.classMineChatSerializer2, "a", new Object[]{classMineIChatBaseComponent}, component);
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
        method_sendPacket.invoke(getPlayerConnection(player), packet);
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
        for (Player player : getPlayers()) {
            try {
                sendPacket(packet, player);
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
    public static Object getPlayerHandle(Player player) throws Exception {
        return Extra.getMethodInvoke(player, "getHandle");
    }


    /**
     * Retorna PlayerConnection da variavel playerConnection da classe
     * EntityPlayer <Br>
     * Pega o EntityPlayer pelo metodo getHandle(player)
     *
     * @param player Jogador (CraftPlayer)
     * @return Conexão do jogador
     */
    public static Object getPlayerConnection(Player player) throws Exception {
        return Extra.getFieldValue(getPlayerHandle(player), "playerConnection");
    }


}
