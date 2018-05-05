package net.eduard.api.command.api.lag;

import net.eduard.api.setup.manager.CommandManager;

public class LagCommand extends CommandManager{

	public LagCommand() {
		super("lag");
		register(new LagMobsCommand());
	}
}
