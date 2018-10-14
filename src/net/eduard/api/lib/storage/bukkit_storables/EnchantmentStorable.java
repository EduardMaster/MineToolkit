package net.eduard.api.lib.storage.bukkit_storables;

import org.bukkit.enchantments.Enchantment;

import net.eduard.api.lib.modules.Extra;
import net.eduard.api.lib.storage.Storable;

public class EnchantmentStorable implements Storable{

		@Override
		public boolean saveInline() {
			return true;
		}

		@SuppressWarnings("deprecation")
		@Override
		public Object store(Object object) {
			if (object instanceof Enchantment) {
				Enchantment enchantment = (Enchantment) object;
				return enchantment.getId();

			}
			return null;
		}

		@SuppressWarnings("deprecation")
		@Override
		public Object restore(Object object) {
			if (object instanceof String) {
				String string = (String) object;
				return Enchantment.getById(Extra.toInt(string));

			}
			return null;
		}

}
