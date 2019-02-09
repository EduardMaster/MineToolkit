package net.eduard.api.manager;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.gmail.nossr50.datatypes.player.McMMOPlayer;
import com.gmail.nossr50.datatypes.skills.SkillType;
import com.gmail.nossr50.events.experience.McMMOPlayerExperienceEvent;
import com.gmail.nossr50.events.experience.McMMOPlayerLevelUpEvent;
import com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent;
import com.gmail.nossr50.events.party.McMMOPartyXpGainEvent;
import com.gmail.nossr50.util.player.UserManager;

import net.eduard.api.EduardAPI;
import net.eduard.api.lib.Mine;
import net.eduard.api.lib.Mine.Replacer;
import net.eduard.api.lib.manager.EventsManager;

public class McMMOReplacers extends EventsManager {

	private Map<Player, SkillType> ultimoUp = new HashMap<>();
	private Map<Player, BukkitTask> ultimasTasks = new HashMap<>();

	public McMMOReplacers() {
		if (Mine.hasPlugin("mcMMO")) {
			register(EduardAPI.getInstance());
			Mine.addReplacer("$mcmmo_level", new Replacer() {

				@Override
				public Object getText(Player p) {
					McMMOPlayer usuario = UserManager.getPlayer(p);
					int nivel = usuario.getPowerLevel();
					return nivel;
				}
			});
			Mine.addReplacer("$mcmmo_status", new Replacer() {

				@Override
				public Object getText(Player p) {
					McMMOPlayer usuario = UserManager.getPlayer(p);
					if (ultimoUp.containsKey(p)) {
						SkillType ultimo = ultimoUp.get(p);
						return "§f" + ultimo.getName() + ": §a" + usuario.getSkillLevel(ultimo);
					}
					return "§fNível: §a" + usuario.getPowerLevel();
				}
			});
		}
	}

	@EventHandler
	public void event(McMMOPlayerExperienceEvent e) {

	}

	@EventHandler
	public void event(McMMOPartyXpGainEvent e) {

	}

	@EventHandler
	public void event(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		ultimoUp.remove(p);
	}

	@EventHandler
	public void event(McMMOPlayerLevelUpEvent e) {
		Player p = e.getPlayer();
//		SkillType habilidade = e.getSkill();
		ultimoUp.remove(p);

	}

	@EventHandler
	public void event(McMMOPlayerXpGainEvent e) {
		Player p = e.getPlayer();
		SkillType habilidade = e.getSkill();
		ultimoUp.put(p, habilidade);
		if (ultimasTasks.containsKey(p)) {
			BukkitTask task = ultimasTasks.get(p);

			task.cancel();
		}
		BukkitTask task = new BukkitRunnable() {
			
			@Override
			public void run() {
				ultimoUp.remove(p);
				ultimasTasks.remove(p);
			}
		}.runTaskLaterAsynchronously(getPlugin(), 20*5);
		
		ultimasTasks.put(p, task);
	}

}
