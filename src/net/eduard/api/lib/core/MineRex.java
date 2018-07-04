package net.eduard.api.lib.core;

import net.eduard.api.lib.Extra;
/**
 * Gerenciamento das clases NMS e OBC e Spigot
 * @author Eduard
 *
 */
public final class MineRex {
	public static String claz_mEntityPlayer = "#mEntityPlayer";
	public static String claz_cCraftPlayer = "#cCraftPlayer";
	public static String claz_sPacketTitle = "#sProtocolInjector$PacketTitle";
	public static String claz_sAction = "#sProtocolInjector$PacketTitle$Action";
	public static String claz_sPacketTabHeader = "#sProtocolInjector$PacketTabHeader";
	public static String claz_pPlayOutChat = "#pPlayOutChat";
	public static String claz_pPlayOutTitle = "#pPlayOutTitle";
	public static String claz_pPlayOutWorldParticles = "#pPlayOutWorldParticles";
	public static String claz_pPlayOutPlayerListHeaderFooter = "#pPlayOutPlayerListHeaderFooter";
	public static String claz_pPlayOutNamedEntitySpawn = "#pPlayOutNamedEntitySpawn";
	public static String claz_pPlayInClientCommand = "#pPlayInClientCommand";
	public static String claz_cEnumTitleAction = "#cEnumTitleAction";
	public static String claz_pEnumTitleAction2 = "#pPlayOutTitle$EnumTitleAction";
	public static String claz_mEnumClientCommand = "#mEnumClientCommand";
	public static String claz_mEnumClientCommand2 = "#pPlayInClientCommand$EnumClientCommand";
	public static String claz_mChatSerializer = "#mChatSerializer";
	public static String claz_mIChatBaseComponent = "#mIChatBaseComponent";
	public static String claz_mEntityHuman = "#mEntityHuman";
	public static String claz_mNBTTagCompound = "#mNBTTagCompound";
	public static String claz_mNBTBase = "#mNBTBase";
	public static String claz_mNBTTagList = "#mNBTTagList";
	public static String claz_pPacket = "#p";
	public static String claz_cItemStack = "#cinventory.CraftItemStack";
	public static String claz_mItemStack = "#mItemStack";
	public static String claz_bItemStack = "#bItemStack";
	public static String claz_bBukkit = "#bBukkit";
	public static String claz_mChatComponentText = "#mChatComponentText";
	public static String claz_mMinecraftServer = "#mMinecraftServer";
	static {
		Extra.newReplacer("#v", Mine.getVersion());
	}
	
}
