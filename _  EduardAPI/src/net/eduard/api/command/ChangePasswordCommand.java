package net.eduard.api.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.API;
import net.eduard.api.config.Config;
import net.eduard.api.manager.CMD;

public class ChangePasswordCommand extends CMD {
	public String message = "§aVoce trocou sua senha!";
	public String messageError = "§cVoce ja esta logado!";
	public String messagePassword = "§cSenha antiga incorreta";

	public Config config = new Config("auth.yml");
	public ChangePasswordCommand() {
		super("changepassword");

	}
	public String getPassword(Player p) {
		return config.getString(p.getUniqueId().toString() + ".password");
	}
	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {
		if (API.onlyPlayer(sender)) {
			Player p = (Player) sender;
			if (LoginCommand.PLAYERS_LOGGED.containsKey(p)) {
				if (args.length <= 1) {
					sendUsage(sender);
				} else {
					String pass = args[0];
					String newPass = args[1];
					if (getPassword(p).equals(pass)) {
						if (RegisterCommand.getRegister().canRegister(p,
								newPass)) {
							config.set(
									p.getUniqueId().toString()
											+ ".last-change-password",
									API.getNow());
							config.set(p.getUniqueId().toString() + ".password",
									newPass);
							API.chat(p, message);
						}

					} else {
						API.chat(p, messagePassword);
					}
				}
			} else {
				API.chat(p, messageError);
			}
		}
		return true;
	}

}
