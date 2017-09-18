package net.eduard.api.game;

import java.util.Map;

import org.bukkit.entity.Player;

import net.eduard.api.API;
import net.eduard.api.setup.StorageAPI.Storable;

public class Delay implements Storable {

	private int seconds = 3;

	private String bypass = "teleport.bypass";

	private String message = "Por favor espera $seconds segundos!";


	public void effect(Player p, PlayerEffect effect) {
		if (p.hasPermission(bypass)) {
			
			effect.effect(p);
		} else {
			API.chat(p, message.replace("$seconds", ""+seconds));
			API.TIME.delay(20 * seconds, new Runnable() {

				@Override
				public void run() {
					effect.effect(p);
				}
			});
		}
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

}
