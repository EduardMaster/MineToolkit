
package net.eduard.api.command.essentials;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.eduard.api.config.Config;
import net.eduard.api.setup.manager.CommandManager;

public class SlimeCommand extends CommandManager {
	public List<String> messages = new ArrayList<>();
	public SlimeCommand() {
		messages.add(" - Requisitos para ser Youtuber - ");

	}

	public Config config = new Config("spawn.yml");

	@Override
	public boolean onCommand(CommandSender sender, Command command,
			String label, String[] args) {

		if (sender instanceof Player) {

			Player p = (Player) sender;
			/* 668 */ if (args.length == 0)
			/*      */ {
				/*      */ {
					/*      */
					/* 674 */ p.sendMessage(" ");
					/* 675 */ p.sendMessage(
							"\u00A7cVoc\u00EA desligou o localizador de Slime Chunk, Voc\u00EA n\u00E3o receber\u00E1");
					/* 676 */ p.sendMessage(
							"\u00A7cmas estes barulhos, caso deseje ativar use \u00A7n/slime\u00A7c");
					/* 677 */ p.sendMessage(" ");
					/* 678 */ p.playSound(p.getLocation(), Sound.SLIME_WALK,
							5.0F, 5.0F);
					/*      */
					/*      */ }
				/*      */
				/* 687 */ p.sendMessage(
						"\u00A7aVoc\u00EA ativou o localizador de \u00A7nSlime Chunks\u00A7a. Sempre que voc\u00EA");
				/* 688 */ p.sendMessage(
						"\u00A7apassar em uma Slime Chunk voc\u00EA escutar\u00E1 um \u00A7nbarulho de slime\u00A7a");
				/* 689 */ p.sendMessage(
						"\u00A7afique atento e ative os sons do seu Jogo");
				/* 690 */ p.sendMessage(" ");
				/* 691 */ p.playSound(p.getLocation(), Sound.SLIME_WALK, 5.0F,
						5.0F);
				/*      */
				/* 693 */ return true;
				/*      */ }
			/*      */
			/*      */ }
		/*      */
		/* 699 */

		return true;
	}

}
