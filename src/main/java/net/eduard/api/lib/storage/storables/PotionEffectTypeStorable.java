package net.eduard.api.lib.storage.storables;

import net.eduard.api.lib.storage.annotations.StorageAttributes;
import org.bukkit.potion.PotionEffectType;

import net.eduard.api.lib.storage.Storable;

@StorageAttributes(inline = true)
public class PotionEffectTypeStorable implements Storable<PotionEffectType> {

    public PotionEffectType restore(String string) {
        String[] split = string.split(";");
        return PotionEffectType.getByName(split[0]);
    }

    public String store(PotionEffectType potionEffectType) {
        return potionEffectType.getName() + ";" + potionEffectType.getId();
    }

}
