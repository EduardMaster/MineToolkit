
package net.eduard.eduardapi.command.map;

import net.eduard.api.manager.CMD;

public class MapCommand extends CMD {

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
