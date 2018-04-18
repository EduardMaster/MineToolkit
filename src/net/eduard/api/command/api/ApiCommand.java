
package net.eduard.api.command.api;

import net.eduard.api.setup.manager.CommandManager;

public class ApiCommand extends CommandManager {

	public ApiCommand() {
		super("api");
		register(new ApiReloadCommand());
	}
	

}
