package net.eduard.api.lib.storage.storables;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.*;
import java.util.Map.Entry;

import com.google.gson.*;
import net.eduard.api.lib.game.ItemBuilder;
import net.eduard.api.lib.storage.StorageAPI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.eduard.api.lib.modules.Mine;
import net.eduard.api.lib.game.EnchantGlow;
import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.Storable;


public class ItemStackStorable implements Storable<ItemStack>, JsonSerializer<ItemStack>, JsonDeserializer<ItemStack> {

    public ItemStack deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return (ItemStack) StorageAPI.restore(ItemStack.class, jsonDeserializationContext.deserialize(jsonElement, Map.class));
    }

    public JsonElement serialize(ItemStack itemStack, Type type, JsonSerializationContext jsonSerializationContext) {
        return jsonSerializationContext.serialize(StorageAPI.store(ItemStack.class, itemStack));
    }


    @Override
    public ItemStack newInstance() {
        return new ItemStack(Material.STONE);
    }

    @Override
    public ItemStack restore(Map<String, Object> map) {
        int id = Extra.toInt(map.get("id"));
        int amount = Extra.toInt(map.get("amount"));
        int data = (map.containsKey("data")) ? Extra.toInt(map.get("data")) : 0;
        @SuppressWarnings("deprecation")
        ItemBuilder item = new ItemBuilder(Material.getMaterial(id), amount);
        item.data(data);
        if (map.containsKey("name")) {
            String name = Extra.toChatMessage((String) map.get("name"));
            if (!name.isEmpty()) {
                Mine.setName(item, name);
            }
        }
        if (map.containsKey("type")){
            try {
                Material type = Material.getMaterial(map.get("type").toString());
                item.type(type);
            }catch (Exception ignored){
            }

        }
        if (map.containsKey("lore")) {
            Object dado = map.get("lore");
            if (dado instanceof List) {
                @SuppressWarnings("unchecked")
                List<String> lore = Extra.toMessages((List<Object>) dado);
                if (!lore.isEmpty()) {
                    Mine.setLore(item, lore);
                }
            }
        }
        if (map.containsKey("enchants")) {
            if (map.get("enchants") instanceof String) {

            } else {
                List<String> enchants = (List<String>) map.get("enchants");

                for (String enchantLine : enchants) {
                    String[] sub = enchantLine.split(";");
                    @SuppressWarnings("deprecation")
                    Enchantment ench = Enchantment.getById(Extra.toInt(sub[0]));
                    Integer level = Extra.toInt(sub[1]);
                    item.addUnsafeEnchantment(ench, level);
                }
            }
        }
        if (map.containsKey("head-name")) {
            ItemMeta meta = item.getItemMeta();
            if (meta instanceof SkullMeta) {
                SkullMeta skullmeta = (SkullMeta) meta;

                skullmeta.setOwner("" + map.get("head-name"));

                item.setItemMeta(skullmeta);
            }

        } else if (map.containsKey("texture-value")) {
            ItemMeta meta = item.getItemMeta();
            if (meta instanceof SkullMeta) {
                GameProfile profile = new GameProfile(UUID.randomUUID(), null);
                profile.getProperties().put("textures", new Property("textures", (String) map.get("texture-value")));
                Field profileField = null;
                try {
                    profileField = meta.getClass().getDeclaredField("profile");
                    profileField.setAccessible(true);
                    profileField.set(meta, profile);

                } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
                    e.printStackTrace();
                }
                item.setItemMeta(meta);
            }
        } else if (map.containsKey("texture")) {
            Mine.setSkin(item, (String) map.get("texture"));
        }
        if (map.containsKey("glow")) {
            boolean glowed = Extra.toBoolean(map.get("glow"));
            if (glowed) {
                EnchantGlow.addGlow(item);
            }
        }
        return item;
    }


    @Override
    public void store(Map<String, Object> map, ItemStack item) {
        map.remove("durability");
        map.remove("meta");
        map.remove("type");
        map.remove("data");
        map.remove("handle");
        map.remove("skull-owner");
        map.put("id", item.getTypeId());
        map.put("type",item.getType());
        map.put("data", item.getDurability());
        map.put("amount", item.getAmount());
        map.put("name", Mine.getName(item));
        map.put("lore", Extra.toConfigMessages(Mine.getLore(item)));
        if (item.containsEnchantment(EnchantGlow.getGlow())) {
            map.put("glow", true);
        }
        List<String> enchants = new ArrayList<>();
        if (item.getItemMeta().hasEnchants()) {
            StringBuilder str = new StringBuilder();
            try {
                for (Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet()) {
                    Enchantment enchantment = entry.getKey();
                    enchants.add(enchantment.getId() + ";" + entry.getValue());
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        map.put("enchants", enchants);
        if (item.getItemMeta() instanceof SkullMeta) {
            SkullMeta meta = (SkullMeta) item.getItemMeta();
            if (meta.getOwner() != null) {
                map.put("head-name", meta.getOwner());
            }
            try {
                Field profileField = meta.getClass().getDeclaredField("profile");
                profileField.setAccessible(true);
                GameProfile profile = (GameProfile) profileField.get(meta);
                if (profile == null)
                    return;
                if (profile.getProperties() == null)
                    return;
                Collection<Property> textures = profile.getProperties().get("textures");
                if (textures == null)
                    return;
                if (textures.size() >= 1) {
                    for (Property texture : textures) {

                        map.put("texture-value", texture.getValue());
                        map.put("texture-signature", texture.getSignature());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }
//


    @Override
    public ItemStack restore(String text) {
        try {
            String[] split = text.split(";");
            String[] splitData = split[0].split("-");
            Integer qnt = Extra.toInt(splitData[1]);
            String[] splitInfo = splitData[0].split(":");
            Integer id = Extra.toInt(splitInfo[0]);
            short data = Extra.toShort(splitInfo[1]);
            ItemStack item = new ItemStack(1);
            item.setTypeId(id);
            item.setDurability(data);
            item.setAmount(qnt);
            if (split.length > 0) {
                if (split[1].contains(",")) {
                    String[] enchs = split[1].split(",");
                    for (String enchant : enchs) {
                        String[] ench = enchant.split("-");
                        Integer ench_id = Extra.toInt(ench[0]);
                        Integer ench_level = Extra.toInt(ench[1]);
                        item.addUnsafeEnchantment(Enchantment.getById(ench_id), ench_level);
                    }
                } else {
                    if (!split[1].equals(" ")) {
                        String[] ench = split[1].split("-");
                        Integer ench_id = Extra.toInt(ench[0]);
                        Integer ench_level = Extra.toInt(ench[1]);
                        item.addUnsafeEnchantment(Enchantment.getById(ench_id), ench_level);
                    }

                }
            }
            String nome = split[2];
            if (!nome.equals(" ")) {
                Mine.setName(item, Extra.toChatMessage(nome));
            }
            List<String> lista = new ArrayList<>();
            String descricao = split[3];
            if (descricao.contains(",")) {
                String[] lore = descricao.split(",");
                for (String line : lore) {
                    lista.add(Extra.toChatMessage(line));
                }
            } else {
                if (!descricao.equals(" ")) {
                    lista.add(descricao);
                }
            }
            Mine.setLore(item, lista);
            return item;

        } catch (Exception e) {
            e.printStackTrace();
            return new ItemStack(1);
        }


    }

    @SuppressWarnings("deprecation")
    @Override
    public String store(ItemStack item) {
        StringBuilder textao = new StringBuilder();
        textao.append(item.getTypeId() + ":" + item.getDurability() + "-" + item.getAmount() + ";");
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (meta.hasEnchants()) {
                boolean first = true;
                for (Entry<Enchantment, Integer> enchant : item.getItemMeta().getEnchants().entrySet()) {
                    if (!first) {
                        textao.append(",");
                    } else
                        first = false;
                    textao.append(enchant.getKey().getId());
                    textao.append("-");
                    textao.append(enchant.getValue());
                }
            } else {
                textao.append(" ");
            }
            textao.append(";");
            if (item.getItemMeta().hasDisplayName()) {
                textao.append(item.getItemMeta().getDisplayName());
            } else {
                textao.append(" ");
            }
            textao.append(";");
            if (meta.hasLore()) {
                boolean first = true;
                for (String line : meta.getLore()) {
                    if (!first) {
                        textao.append(",");
                    } else
                        first = false;
                    textao.append(line);
                }
            } else {
                textao.append(" ");
            }
            textao.append(";");
        }
        return textao.toString();

    }


}
