
package net.eduard.eduardapi.command;

import net.eduard.api.manager.CMD;

public class ApiCommand extends CMD {

	public ApiCommand() {
		super("api");
		register(new ApiReloadCommand());
	}
	

}
