package net.eduard.api.advanced;

import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;
import net.minecraft.server.v1_8_R3.NBTTagString;

/**
 * 
 * {@linkplain <a href=
 * 'https://www.spigotmc.org/resources/letterheads-%E2%99%A6-500-custom-skulls-%E2%99%A6-player-heads-%E2%99%A6-gui-%E2%99%A6-1-8-1-12.14685/'
 * >Dono do Sistema</a>}
 *
 */
public class LetterHead {
	org.bukkit.inventory.ItemStack HeadItem = new org.bukkit.inventory.ItemStack(Material.SKULL_ITEM, 1, (short) 3);

	public LetterHead(String playername) {
		org.bukkit.inventory.ItemStack itemStack = new org.bukkit.inventory.ItemStack(Material.SKULL_ITEM, 1,
				(short) 3);
		SkullMeta skullMeta = (SkullMeta) itemStack.getItemMeta();

		skullMeta.setOwner(playername);
		skullMeta.setDisplayName("Head of " + playername);
		itemStack.setItemMeta(skullMeta);

		this.HeadItem = itemStack;
	}

	public static LetterHead newHead(String displayName, String comando) {
		String[] split = comando.split(" ");
		String text = split[5];
		String[] idInicio = text.split("Id:\"");
		String[] idFim = idInicio[1].split("\"");
		String id = idFim[0];
		String[] texturaInicio = text.split("Value:\"");
		String[] texturaFim = texturaInicio[1].split("\"");
		String textura = texturaFim[0];
		return new LetterHead(displayName, id, textura);

	}

	public LetterHead(String skullname, String id, String textureValue) {
		org.bukkit.inventory.ItemStack itemStack = new org.bukkit.inventory.ItemStack(Material.SKULL_ITEM, 1,
				(short) 3);

		ItemMeta PlusMeta = itemStack.getItemMeta();
		PlusMeta.setDisplayName(skullname);
		itemStack.setItemMeta(PlusMeta);

		net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);

		NBTTagCompound compound = nmsStack.getTag();
		if (compound == null) {
			compound = new NBTTagCompound();
			nmsStack.setTag(compound);
			compound = nmsStack.getTag();
		}

		NBTTagCompound skullOwner = new NBTTagCompound();
		skullOwner.set("Id", new NBTTagString(id));
		NBTTagCompound properties = new NBTTagCompound();
		NBTTagList textures = new NBTTagList();
		NBTTagCompound value = new NBTTagCompound();
		value.set("Value", new NBTTagString(textureValue));
		textures.add(value);
		properties.set("textures", textures);
		skullOwner.set("Properties", properties);

		compound.set("SkullOwner", skullOwner);
		nmsStack.setTag(compound);

		this.HeadItem = CraftItemStack.asBukkitCopy(nmsStack);
	}

	public void giveTo(Player player) {
		player.getInventory().addItem(new org.bukkit.inventory.ItemStack[] { this.HeadItem });
	}

	public org.bukkit.inventory.ItemStack getItemStack() {
		return this.HeadItem;
	}
}