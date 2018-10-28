package net.eduard.api.lib.storage.bukkit_storables;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.StorageAttributes;

@StorageAttributes(inline=true)
public class MaterialDataStorable implements Storable {

	@Override
	public Object newInstance() {

		return new MaterialData(Material.STONE);
	}
	@SuppressWarnings("deprecation")
	@Override
	public Object store(Object object) {
		if (object instanceof MaterialData) {
			MaterialData materialData = (MaterialData) object;
			return materialData.getItemTypeId() + ":" + materialData.getData();

		}
		return null;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Object restore(Object object) {
		if (object instanceof String) {
			String string = (String) object;
			try {
				String[] split = string.split(":");
				return new MaterialData(Material.getMaterial(Mine.toInt(split[0])), Mine.toByte(split[1]));
			} catch (Exception e) {
				new MaterialData(Material.STONE);
			}

		}
		return null;
	}

}
