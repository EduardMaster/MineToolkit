package net.eduard.api.lib.storage.bukkit_storables;

import org.bukkit.potion.PotionEffectType;

import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.Storable.*;
@StorageAttributes(inline=true)
public class PotionEffectTypeStorable implements Storable<PotionEffectType> {



	public PotionEffectType restore(String object) {
		if (object instanceof String) {

			String string = (String) object;
			String[] split = string.split(";");
			return PotionEffectType.getByName(split[1]);
		}
		return null;
	}


	public String store(PotionEffectType object) {
		if (object instanceof PotionEffectType) {
			PotionEffectType potionEffectType = (PotionEffectType) object;
			return potionEffectType.getName() + ";" + potionEffectType.getId();

		}
		return null;
	}

}
