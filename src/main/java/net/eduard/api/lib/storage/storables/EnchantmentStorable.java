package net.eduard.api.lib.storage.storables;

import net.eduard.api.lib.storage.annotations.StorageAttributes;
import org.bukkit.enchantments.Enchantment;

import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.Storable;

@StorageAttributes(inline = true)
public class EnchantmentStorable implements Storable<Enchantment> {


    public String store(Enchantment enchantment) {

        return "" + enchantment.getId();


    }


    public Enchantment restore(String string) {

        return Enchantment.getById(Extra.toInt(string));

    }


}
