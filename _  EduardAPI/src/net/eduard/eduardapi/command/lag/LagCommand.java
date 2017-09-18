package net.eduard.eduardapi.command.lag;

import net.eduard.api.manager.CMD;

public class LagCommand extends CMD{

	public LagCommand() {
		super("lag");
		register(new LagMobsCommand());
	}
}
