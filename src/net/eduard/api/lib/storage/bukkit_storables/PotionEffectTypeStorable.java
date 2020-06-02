package net.eduard.api.lib.storage.bukkit_storables;

import org.bukkit.potion.PotionEffectType;

import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.Storable.*;

@StorageAttributes(inline = true)
public class PotionEffectTypeStorable implements Storable<PotionEffectType> {


    public PotionEffectType restore(String string) {


        String[] split = string.split(";");
        return PotionEffectType.getByName(split[1]);

    }


    public String store(PotionEffectType potionEffectType) {

        return potionEffectType.getName() + ";" + potionEffectType.getId();

    }

}
