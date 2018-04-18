package net.eduard.api.command.lag;

import net.eduard.api.setup.manager.CommandManager;

public class LagCommand extends CommandManager{

	public LagCommand() {
		super("lag");
		register(new LagMobsCommand());
	}
}
