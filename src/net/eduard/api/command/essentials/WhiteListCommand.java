
package net.eduard.api.command.essentials;

import net.eduard.api.setup.manager.CommandManager;

public class WhiteListCommand extends CommandManager {
	public WhiteListCommand() {
		super("whitelist");
		register(new WhiteListHelpCommand());
		register(new WhiteListAddCommand());
		register(new WhiteListRemoveCommand());
		register(new WhiteListOnCommand());
		register(new WhiteListOffCommand());
		register(new WhiteListListCommand());

	}

}
