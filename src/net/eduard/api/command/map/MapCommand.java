
package net.eduard.api.command.map;

import net.eduard.api.setup.manager.CommandManager;

public class MapCommand extends CommandManager {

	public MapCommand() {
		super("map");
		register(new MapCopyCommand());
		register(new MapPasteCommand());
		register(new MapPos1Command());
		register(new MapPos2Command());
		register(new MapLoadCommand());
		register(new MapSaveCommand());
		register(new MapWorldCommand());
		register(new MapHelpCommand());
	}

}
