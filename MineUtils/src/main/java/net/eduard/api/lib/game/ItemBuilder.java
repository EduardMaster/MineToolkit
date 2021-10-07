package net.eduard.api.lib.game;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.eduard.api.lib.modules.Mine;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.potion.PotionEffect;

import java.lang.reflect.Field;
import java.util.*;

/**
 * API de criação de Items<br>
 * Método build() gerá um instancia nova de ItemBuilder
 *
 * @author Eduard
 * @version 1.5
 */
@SuppressWarnings("unused")
public class ItemBuilder extends ItemStack {
    /**
     * Dia /04/08/2021
     * <br>
     * Variavle necessaria para salvar o Texto do tipo q veio da config
     * <br>
     * Na versão 1.16.5 os Items deram problemas tive q fazer para resolver
     */

    public ItemBuilder() {
        this(Material.STONE, 1);
    }

    public ItemBuilder(ItemStack clone) {
        super(clone);
    }

    public ItemBuilder(Material type) {
        this(type, 1);
    }

    public ItemBuilder(Material type, int amount) {
        super(type, amount);
    }

    public ItemBuilder spawnerType(EntityType type) {
        type(Material.MOB_SPAWNER);
        BlockStateMeta meta = (BlockStateMeta) getItemMeta();
        BlockState state = meta.getBlockState();
        if (state != null) {
            CreatureSpawner spawner = (CreatureSpawner) state;
            spawner.setSpawnedType(type);
        }


        return this;
    }

    public ItemBuilder potion(PotionEffect effect) {
        if (getType() != Material.POTION)
            setType(Material.POTION);
        PotionMeta meta = (PotionMeta) getItemMeta();
        meta.setMainEffect(effect.getType());
        meta.addCustomEffect(effect, true);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder banner(DyeColor baseColor, DyeColor patternColor, PatternType patternType) {
        type(Material.BANNER);
        BannerMeta bannerMeta = (BannerMeta) getItemMeta();
        bannerMeta.setBaseColor(baseColor);
        bannerMeta.addPattern(new Pattern(patternColor, patternType));
        setItemMeta(bannerMeta);
        return this;
    }

    public ItemBuilder addBanner(DyeColor patternColor, PatternType patternType) {
        type(Material.BANNER);
        BannerMeta bannerMeta = (BannerMeta) getItemMeta();
        bannerMeta.addPattern(new Pattern(patternColor, patternType));
        setItemMeta(bannerMeta);
        return this;
    }


    public ItemBuilder(String name) {
        this(Material.DIAMOND_SWORD, 1);
        name(name);
    }

    public ItemBuilder skull(String skullName) {
        type(Material.SKULL_ITEM);
        data(SkullType.PLAYER.ordinal());
        SkullMeta meta = (SkullMeta) getItemMeta();
        meta.setOwner(skullName);
        setItemMeta(meta);
        return this;

    }

    public ItemBuilder texture(String textureBase64) {
        type(Material.SKULL_ITEM);
        data(SkullType.PLAYER.ordinal());
        SkullMeta itemMeta = (SkullMeta) getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), "Notch");
        profile.getProperties().put("textures",
                new Property("textures", textureBase64));

        try {
            Field profileField = itemMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(itemMeta, profile);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        setItemMeta(itemMeta);
        return this;
    }

    public ItemBuilder skin(String skinUrl) {
        byte[] encodedData = Base64.getEncoder()
                .encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", skinUrl).getBytes());

        return texture(new String(encodedData));
    }

    public ItemBuilder skinId(String skinID) {
        return skin("http://textures.minecraft.net/texture/" + skinID);
    }

    public ItemBuilder amount(int amount) {
        setAmount(amount);
        return this;
    }

    public ItemBuilder data(int data) {
        setDurability((short) data);
        return this;
    }

    public ItemBuilder type(Material type) {
        setType(type);
        return this;
    }

    public ItemBuilder addFlags(ItemFlag... flags) {
        ItemMeta meta = getItemMeta();
        meta.addItemFlags(flags);
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder removeFlags() {
        ItemMeta meta = getItemMeta();
        meta.removeItemFlags(ItemFlag.values());
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder clearEnchants() {

        for (Enchantment enchant : getEnchantments().keySet()) {
            removeEnchantment(enchant);
        }

        return this;

    }

    public ItemBuilder lore(String... lore) {
        return lore(Arrays.asList(lore));
    }

    public ItemBuilder lore(Collection<String> lore) {
        ItemMeta meta = getItemMeta();
        meta.setLore(new ArrayList<>(lore));
        setItemMeta(meta);
        return this;
    }

    public List<String> getLore() {
        ItemMeta meta = getItemMeta();
        if (meta != null && meta.getLore() != null) {

            return meta.getLore();
        }
        return new ArrayList<>();
    }

    public ItemBuilder addLore(String line) {
        List<String> lore = getLore();
        lore.add(line);
        return lore(lore);
    }

    public ItemBuilder removeLore() {
        return lore();
    }

    public ItemBuilder clearLore() {
        ItemMeta meta = getItemMeta();
        meta.setLore(new ArrayList<>());
        setItemMeta(meta);
        return this;
    }

    public ItemBuilder glowed() {
        addEnchant(EnchantGlow.getGlow(), 1);
        return this;
    }

    public ItemBuilder addEnchant(Enchantment enchant, int level) {
        addUnsafeEnchantment(enchant, level);
        return this;
    }

    public ItemBuilder name(String name) {
        ItemMeta meta = this.getItemMeta();
        meta.setDisplayName(name);
        setItemMeta(meta);
        return this;

    }


    public ItemBuilder clone() {
        return (ItemBuilder) super.clone();
    }

    public ItemBuilder builder() {
        return clone();
    }


    public ItemBuilder banner(String alphabet, DyeColor baseColor, DyeColor dyeColor) {
        alphabet = ChatColor.stripColor(alphabet.toUpperCase()).substring(0, 1);

        BannerMeta bannerMeta = (BannerMeta) getItemMeta();
        bannerMeta.setBaseColor(baseColor);
        switch (alphabet) {
            case "A":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "B":
            case "8":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "C":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "D":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.CURLY_BORDER));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "E":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "F":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "G":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "H":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "I":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_CENTER));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "J":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "K":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_VERTICAL_MIRROR));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.CROSS));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "L":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "M":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.TRIANGLE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.TRIANGLES_TOP));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "N":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.DIAGONAL_RIGHT_MIRROR));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_DOWNRIGHT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "O":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "P":
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "Q":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.SQUARE_BOTTOM_RIGHT));
                break;
            case "R":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL_MIRROR));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_DOWNRIGHT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "S":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.RHOMBUS_MIDDLE));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_DOWNRIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.CURLY_BORDER));
                break;
            case "T":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_CENTER));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "U":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "V":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.TRIANGLES_BOTTOM));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_DOWNLEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "W":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.TRIANGLE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.TRIANGLES_BOTTOM));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "X":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.STRIPE_CENTER));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.CROSS));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "Y":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.CROSS));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_VERTICAL_MIRROR));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_DOWNLEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "Z":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_DOWNLEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "1":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.SQUARE_TOP_LEFT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_CENTER));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "2":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.RHOMBUS_MIDDLE));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_DOWNLEFT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "3":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "4":
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "5":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_DOWNRIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.CURLY_BORDER));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.SQUARE_BOTTOM_LEFT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "6":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "7":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.DIAGONAL_RIGHT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_DOWNLEFT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.SQUARE_BOTTOM_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "9":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.HALF_HORIZONTAL_MIRROR));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_MIDDLE));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
            case "0":
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_TOP));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_RIGHT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_BOTTOM));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_LEFT));
                bannerMeta.addPattern(new Pattern(dyeColor, PatternType.STRIPE_DOWNLEFT));
                bannerMeta.addPattern(new Pattern(baseColor, PatternType.BORDER));
                break;
        }
        setItemMeta(bannerMeta);
        return this;
    }
}
