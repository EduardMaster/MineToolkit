package net.eduard.api.lib.storage.storables;

import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.annotations.StorageAttributes;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import net.eduard.api.lib.storage.Storable;

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
            if (string.contains(";")) {
                String[] split = string.split(";");
                return new MaterialData(Material.getMaterial(Extra.toInt(split[0])), Extra.toByte(split[1]));
            }else{
                return  new MaterialData(Material.getMaterial(Extra.toInt(string)));
            }
        } catch (Exception e) {
        }
        return newInstance();
    }

}
