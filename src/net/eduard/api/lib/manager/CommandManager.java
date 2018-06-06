package net.eduard.api.lib.manager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import net.eduard.api.lib.core.Mine;

public class CommandManager extends EventsManager implements TabCompleter, CommandExecutor {

	private static Map<String, CommandManager> commandsRegistred = new HashMap<>();

	private Map<String, CommandManager> commands = new HashMap<>();

	private String permission;

	protected String name;

	private CommandManager parent;

	private String usage;

	private List<String> aliases = new ArrayList<>();

	private String permissionMessage="§cVocê não possui permissão para executar este comando.";

	private String description;

	public void sendPermissionMessage(CommandSender sender) {
		sender.sendMessage(permissionMessage);
	}

	public String getCommandName() {

		return getClass().getSimpleName().toLowerCase().replace("sub", "").replace("subcommand", "")
				.replace("comando", "").replace("command", "").replace("cmd", "").replace("eduard", "");
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		return null;
	}

	public void broadcast(String message) {
		Mine.broadcast(message, permission);

	}

	public static CommandManager getCommand(String name) {
		return commandsRegistred.get(name.toLowerCase());
	}

	public CommandManager() {
		this("");
	}

	public CommandManager(String name) {
		this.name = name;
		if (name.equals("")) {
			this.name = getCommandName();
		}
	}

	public CommandManager(String name, String... aliases) {
		this(name);
		this.aliases = Arrays.asList(aliases);
	}

	public void sendUsage(CommandSender sender) {
		sender.sendMessage(getUsage());
	}

	public void sendDescription(CommandSender sender) {
		sender.sendMessage(getDescription());
	}

	public boolean register(CommandManager sub) {
		commands.put(sub.name, sub);
		return true;
	}

	public void updateSubs() {
		for (CommandManager sub : commands.values()) {
			if (sub.permission == null)
				sub.permission = permission + "." + sub.name;
			if (sub.description == null) {
				sub.description = "§fExemplo";
			}

			if (permissionMessage == null) {
				permissionMessage = Mine.NO_PERMISSION;
			}

			if (sub.usage == null) {
				sub.usage = Mine.USAGE + "/" + name + " " + sub.name;

			}
			if (sub.usage == null)
				sub.usage = Mine.USAGE + "/" + name + " " + sub.name;
			Mine.console("§bCommandAPI §fO subcomando §e" + sub.name + " §ffoi registrado no comando §a" + name);
			if (!sub.commands.isEmpty())
				sub.updateSubs();
		}
	}

	public void registerCommand(Plugin plugin) {
		setPlugin(plugin);
		Command command = new Command(name) {

			@Override
			public boolean execute(CommandSender sender, String label, String[] args) {
				if (!plugin.isEnabled()) {
					return false;
				}
				if (sender.hasPermission(permission)) {

					return onCommand(sender, this, label, args);
				} else {
					sender.sendMessage(permissionMessage);
					return false;
				}
			}
		};
		command.setAliases(aliases);
		command.setDescription(description);
		command.setLabel(name);
		command.setUsage(usage);
		command.setPermissionMessage(permissionMessage);
		command.setPermission(permission);
		Mine.createCommand(plugin, command);
	}

	public boolean register() {
		PluginCommand command = Bukkit.getPluginCommand(name);
		if (command == null) {
			Mine.console("§bCommandAPI §fO comando §a" + name
					+ " §fnao foi registrado na plugin.yml de nenhum Plugin do Servidor");
			return false;
		}
		setPlugin(command.getPlugin());
		if (permission == null) {
			if (command.getPermission() == null) {
				permission = getPlugin().getName() + "." + name;
			} else if (command.getPermission().isEmpty()) {
				permission = getPlugin().getName() + "." + name;
			} else
				permission = command.getPermission();
		}
		if (description == null) {
			if (command.getDescription() == null) {
				description = "§fExemplo";
			} else {
				description = "§a" + command.getDescription();
			}
		}
		if (permissionMessage == null) {
			if (command.getPermissionMessage() == null) {
				permissionMessage = Mine.NO_PERMISSION;
			} else {
				permissionMessage = command.getPermissionMessage();
			}
		}
		if (usage == null) {
			if (!command.getUsage().isEmpty()) {
				usage = Mine.USAGE + command.getUsage().replace("<command>", name);
			} else {
				usage = Mine.USAGE + "/" + name + " help";
			}
		}

		aliases = command.getAliases();
		command.setUsage(usage);
		command.setDescription(description);
		command.setPermission(permission);
		command.setExecutor(this);
		Mine.console("§bCommandAPI §fO comando §a" + name + " §ffoi registrado para o Plugin §b"
				+ command.getPlugin().getName());
		commandsRegistred.put(name.toLowerCase(), this);
		updateSubs();
		register(getPlugin());
		return true;

	}

	public boolean unregisterCommand() {
		Mine.removeCommand(name);
		return false;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			return false;
		}
		CommandManager cmd = this;
		int id = 0;
		while (true) {
			if (args.length == id) {
				if (sender.hasPermission(cmd.getPermission())) {
					return cmd.onCommand(sender, command, label, args);
				}
				sendPermissionMessage(sender);
				return true;
			}
			String arg = args[id];
			CommandManager newCmd = null;
			for (CommandManager sub : cmd.getCommands().values()) {
				if (sub.getName().equalsIgnoreCase(arg)) {
					newCmd = sub;
					break;
				}
				if (sub.getAliases().contains(arg.toLowerCase())) {
					newCmd = sub;
					break;
				}
			}
			if (newCmd == null) {
				if (cmd == this) {
					return false;
				}
				if (sender.hasPermission(cmd.getPermission())) {
					return cmd.onCommand(sender, command, label, args);
				}
				sendPermissionMessage(sender);
				return true;
			} else {
				cmd = newCmd;
			}
			id++;

		}

	}

	public PluginCommand getCommand() {
		return Bukkit.getPluginCommand(name);
	}

	public Map<String, CommandManager> getCommands() {
		return commands;
	}

	protected void setCommands(Map<String, CommandManager> commands) {
		this.commands = commands;
	}

	public String getPermission() {
		return permission;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public List<String> getAliases() {
		return aliases;
	}

	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getPermissionMessage() {
		return permissionMessage;
	}

	public void setPermissionMessage(String permissionMessage) {
		this.permissionMessage = permissionMessage;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public static Map<String, CommandManager> getCommandsRegistred() {
		return commandsRegistred;
	}

	public static void setCommandsRegistred(Map<String, CommandManager> commandsRegistred) {
		CommandManager.commandsRegistred = commandsRegistred;
	}

	@Override
	public Object restore(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void store(Map<String, Object> map, Object object) {
		// TODO Auto-generated method stub

	}

	public CommandManager getParent() {
		return parent;
	}

	public void setParent(CommandManager parent) {
		this.parent = parent;
	}

}
