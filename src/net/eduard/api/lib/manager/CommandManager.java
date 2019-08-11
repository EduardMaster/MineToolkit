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

import net.eduard.api.lib.Mine;
import net.eduard.api.lib.storage.StorageAttributes;
@StorageAttributes(indentificate=true)
public class CommandManager extends EventsManager implements TabCompleter, CommandExecutor {

	private static Map<String, CommandManager> commandsRegistred = new HashMap<>();

	public static CommandManager getCommand(String name) {
		return commandsRegistred.get(name.toLowerCase());
	}
 
	public static Map<String, CommandManager> getCommandsRegistred() {
		return commandsRegistred;
	}

	public static void setCommandsRegistred(Map<String, CommandManager> commandsRegistred) {
		CommandManager.commandsRegistred = commandsRegistred;
	}

	public String autoUsage() {
		if (parent != null) {
			return parent.autoUsage() + " " + name;
		} else {

			return "/" + name + "";
		}
	}

	public String autoPermission() {

		if (parent != null) {
			return parent.autoPermission() + "." + name;
		} else {
			return "command." + name;
		}

	}
	@StorageAttributes(reference=true)
	private CommandManager parent;
	protected String name;
	private String permission;

	

	

	private String usage;

	private String description = "§bExemplo";
	private List<String> aliases = new ArrayList<>();

	private String permissionMessage = Mine.MSG_NO_PERMISSION;

	private Map<String, CommandManager> commands = new HashMap<>();

	public CommandManager() {
		this("");
	}

	public CommandManager(String name, String... aliases) {
		this.name = name;
		if (name.equals("")) {
			this.name = getCommandName();

		}
		if (aliases != null)
			this.aliases = Arrays.asList(aliases);
	}

	public void broadcast(String message) {
		Mine.broadcast(message, permission);

	}

	public List<String> getAliases() {
		return aliases;
	}

	public PluginCommand getCommand() {
		return Bukkit.getPluginCommand(name);
	}

	public String getCommandName() {

		return getClass().getSimpleName().toLowerCase().replace("sub", "").replace("subcommand", "")
				.replace("comando", "").replace("command", "").replace("cmd", "").replace("eduard", "");
	}

	public Map<String, CommandManager> getCommands() {
		return commands;
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public CommandManager getParent() {
		return parent;
	}

	public String getPermission() {
		return permission;
	}

	public String getPermissionMessage() {
		return permissionMessage;
	}

	public String getUsage() {
		return usage;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		{
			CommandManager cmd = this;
			for (int i = 0; i < args.length; i++) {
				String arg = args[i].toLowerCase();
				CommandManager sub = null;
				for (CommandManager subcmd : cmd.getCommands().values()) {
					if (subcmd.getName().equalsIgnoreCase(arg)) {
						sub = subcmd;
					}
					for (String alias : subcmd.getAliases()) {
						if (alias.equalsIgnoreCase(arg)) {
							sub = subcmd;
						}
					}
				}
				if (sub == null) {
					break;

				}
				cmd = sub;
			}
//			sender.sendMessage("permiscao " + cmd.getPermission());
			if (cmd == this) {
				if (args.length == 0) {
					sender.sendMessage("/" + name + " help");
				} else {
					sendUsage(sender);
				}
			} else {

				if (sender.hasPermission(cmd.getPermission())) {
					cmd.onCommand(sender, command, label, args);
				} else
					sender.sendMessage(cmd.getPermissionMessage());
			}

		}
		return true;

	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
		ArrayList<String> vars = new ArrayList<>();
		{
			CommandManager cmd = this;
			for (int i = 0; i < args.length; i++) {
				String arg = args[i].toLowerCase();
				vars.clear();
				CommandManager sub = null;
				for (CommandManager subcmd : cmd.getCommands().values()) {
					if (sender.hasPermission(subcmd.getPermission())) {
						if (Mine.startWith(subcmd.getName(), arg)) {
							vars.add(subcmd.getName());
						}
						if (subcmd.getName().equalsIgnoreCase(arg)) {
							sub = subcmd;
						}
						for (String alias : subcmd.getAliases()) {
							if (Mine.startWith(alias, arg)) {
								vars.add(alias);
							}
							if (alias.equalsIgnoreCase(arg)) {
								sub = subcmd;
							}
						}

					}

				}
				if (sub == null) {
					break;
				}
				cmd = sub;
			}
			if (vars.isEmpty()) {
				return null;
			}
//			if (cmd == this) {
//				if (vars.isEmpty()) {
//					return null;
//				}
//				return vars;
//			} else {

//				if (sender.hasPermission(cmd.getPermission())) {
//					return cmd.onTabComplete(sender, command, label, args);
//				}
			return vars;
//			}

		}

	}

	public boolean register() {

		PluginCommand command = Bukkit.getPluginCommand(name);
		if (command == null) {
			Mine.console("§bCommandAPI §fO comando §a" + name
					+ " §fnao foi registrado na plugin.yml de nenhum Plugin do Servidor");
			return false;
		}
		setPlugin(command.getPlugin());
		if (command.getUsage() != null) {
			if (!command.getUsage().isEmpty()) {
				usage = command.getUsage().replace("<command>", name).replace('&', '§');
			}
		}
		if (usage == null) {
			usage = autoUsage();
		}
		if (command.getPermission() != null) {
			permission = command.getPermission();
		} else if (permission == null) {
			permission = autoPermission();
		}
		if (command.getPermissionMessage() != null) {
			permissionMessage = command.getPermissionMessage().replace('&', '§');
		}
		if (command.getDescription()!=null) {
			description = command.getDescription().replace('&', '§');
		}
		// alias não funciona para comandos apenas na plugin.yml ou subcomandos
		if (command.getAliases() != null) {
			aliases = command.getAliases();
		}

		command.setUsage(usage);
		command.setLabel(name);
//		command.setName(name);
		command.setAliases(aliases);
		command.setDescription(description);
		command.setPermission(permission);
		command.setExecutor(this);

		Mine.console("§bCommandAPI §fO comando §a" + name + " §ffoi registrado para o Plugin §b"
				+ command.getPlugin().getName());
		commandsRegistred.put(name.toLowerCase(), this);
		updateSubs();
		register(getPluginInstance());
		return true;

	}

	public boolean register(CommandManager sub) {
		commands.put(sub.name, sub);
		sub.setParent(this);
		return true;
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
		//command.setName(name);
		command.setUsage(usage);
		command.setPermissionMessage(permissionMessage);
		command.setPermission(permission);
		Mine.createCommand(plugin, command);
	}

	@Override
	public Object restore(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return null;
	}

	public void sendDescription(CommandSender sender) {
		sender.sendMessage(getDescription());
	}

	public void sendPermissionMessage(CommandSender sender) {
		sender.sendMessage(permissionMessage);
	}

	public void sendUsage(CommandSender sender) {
		sender.sendMessage(getUsage());
	}

	public void setAliases(List<String> aliases) {
		this.aliases = aliases;
	}

	protected void setCommands(Map<String, CommandManager> commands) {
		this.commands = commands;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParent(CommandManager parent) {
		this.parent = parent;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	public void setPermissionMessage(String permissionMessage) {
		this.permissionMessage = permissionMessage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	@Override
	public void store(Map<String, Object> map, Object object) {
		// TODO Auto-generated method stub

	}

	public boolean unregisterCommand() {
		Mine.removeCommand(name);
		return false;
	}

	public void updateSubs() {
		for (CommandManager sub : commands.values()) {
			if (sub.permission == null)
				sub.permission = sub.autoPermission();

			if (sub.usage == null) {
				sub.usage = sub.autoUsage();
			}
			Mine.console("§bCommandAPI §fO subcomando §e" + sub.name + " §ffoi registrado no comando §a" + name);
			if (!sub.commands.isEmpty())
				sub.updateSubs();
		}
	}

}
