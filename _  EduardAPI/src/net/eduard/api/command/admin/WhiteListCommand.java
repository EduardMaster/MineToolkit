
package net.eduard.api.command.admin;

import net.eduard.api.manager.CMD;

public class WhiteListCommand extends CMD {
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
