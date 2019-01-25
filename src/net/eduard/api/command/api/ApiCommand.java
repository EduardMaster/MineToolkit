
package net.eduard.api.command.api;

import net.eduard.api.lib.manager.CommandManager;

public class ApiCommand extends CommandManager {

	public ApiCommand() {
		super("api");
		register(new ApiHelpCommand());
		register(new ApiReloadCommand());
		register(new ApiUnloadWorldCommand());
		register(new ApiLoadWorldCommand());
		register(new ApiWorldsCommand());
		register(new ApiDeleteWorldCommand());
		register(new ApiListCommand());
		register(new ApiDisableCommand());
		register(new ApiEnableCommand());
		register(new ApiListCommand());
		register(new ApiReloadConfigCommand());
		register(new ApiSaveConfigCommand());
		register(new ApiSaveCommand());
		register(new ApiReloadCommand());
		register(new ApiUnloadCommand());

	}

}
