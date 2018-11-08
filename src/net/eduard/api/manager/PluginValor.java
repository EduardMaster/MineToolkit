package net.eduard.api.manager;

import org.bukkit.plugin.messaging.PluginMessageListener;

import net.eduard.api.lib.game.DisplayBoard;
import net.eduard.api.lib.manager.CommandManager;
import net.eduard.api.lib.manager.DBManager;
import net.eduard.api.lib.manager.EventsManager;
import net.eduard.api.lib.menu.Menu;
import net.eduard.api.lib.menu.Shop;
import net.eduard.api.lib.modules.Extra;

public class PluginValor {

	public static void register() {
		Extra.setPrice(Menu.class, 4);
		Extra.setPrice(Shop.class, 1);
		Extra.setPrice(CommandManager.class, 0.25);
		Extra.setPrice(EventsManager.class, 1);
		Extra.setPrice(DBManager.class, 15);
		Extra.setPrice(PluginMessageListener.class, 10);
		Extra.setPrice(DisplayBoard.class, 5);
		
	}

}
