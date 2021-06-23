package net.eduard.api.lib.storage.storables;

import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.annotations.StorageAttributes;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.eduard.api.lib.storage.Storable;

import java.util.Map;

@StorageAttributes(inline = true)
public class PotionEffectStorable implements Storable<PotionEffect> {
	public PotionEffect newInstance() {
		return new PotionEffect(PotionEffectType.ABSORPTION, 20, 0);
	}

	@Override
	public String store(PotionEffect effect) {
		return effect.getType().getName()+";"+effect.getDuration()+";"+effect.getAmplifier();
	}

	@Override
	public PotionEffect restore(String text) {
		try{
			String[] args = text.split(";");
			PotionEffectType type = PotionEffectType.getByName(args[0]);
			int duration = Extra.toInt(args[1]);
			int level = Extra.toInt(args[2]);
			return new PotionEffect(type,duration,level);

		}catch (Exception ex){
			ex.printStackTrace();
			return new PotionEffect(PotionEffectType.SPEED,20*60,0);
		}
	}

}
