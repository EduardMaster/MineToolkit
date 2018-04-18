package net.eduard.api.tutorial.nivel_2;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class ComandoControlarChat implements CommandExecutor{
	
	private static boolean podeConversar = false;
	
	

	public static boolean isPodeConversar() {
		return podeConversar;
	}

	public static void setPodeConversar(boolean podeConversar) {
		ComandoControlarChat.podeConversar = podeConversar;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (isPodeConversar()) {
			setPodeConversar(false);
			sender.sendMessage("§aChate foi desativado");
		}else {
			setPodeConversar(true);
			sender.sendMessage("§aChate foi ativado");
		}
		return true;
	}

}
