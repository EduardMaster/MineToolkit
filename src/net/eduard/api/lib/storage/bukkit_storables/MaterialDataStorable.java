package net.eduard.api.lib.storage.bukkit_storables;

import net.eduard.api.lib.modules.Extra;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import net.eduard.api.lib.storage.Storable;
import net.eduard.api.lib.storage.Storable.*;

@StorageAttributes(inline = true)
public class MaterialDataStorable implements Storable<MaterialData> {

    @Override
    public MaterialData newInstance() {

        return new MaterialData(1);
    }


    @Override
    public String store(MaterialData materialData) {

        return materialData.getItemTypeId() + ";" + materialData.getData();


    }


    public MaterialData restore(String string) {

        try {
            String[] split = string.split(";");
            return new MaterialData(Material.getMaterial(Extra.toInt(split[0])), Extra.toByte(split[1]));
        } catch (Exception e) {
        }
        return null;
    }

}
