package net.eduard.api.setup.game;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;

import net.eduard.api.setup.Mine;
import net.eduard.api.setup.manager.EventsManager;
import net.eduard.api.setup.manager.FallManager;

public class LaunchPad extends EventsManager {

	public static final Map<World, Boolean> WORLDS = new HashMap<>();
	public static final FallManager NO_FALL = new FallManager();

	private int blockHigh;
	private int blockId = 20;
	private int blockData = -1;
	private Jump jump;
	public LaunchPad() {
	}
	
	public LaunchPad(int blockId, Jump jump) {
		this.blockId = blockId;
		this.jump = jump;
	}

	public LaunchPad(int blockHigh, int blockId, int blockData, Jump jump) {
		super();
		this.blockHigh = blockHigh;
		this.blockId = blockId;
		this.blockData = blockData;
		this.jump = jump;
	}
	public LaunchPad(int blockHigh, int blockId, Jump jump) {
		this.blockHigh = blockHigh;
		this.blockId = blockId;
		this.jump = jump;
	}

	@SuppressWarnings("deprecation")
	@EventHandler
	public void event(PlayerMoveEvent e) {
		if (!Mine.equals2(e.getFrom(), e.getTo())) {
			Player p = e.getPlayer();
			Block block = e.getTo().getBlock().getRelative(0, blockHigh, 0);
			if (blockData != -1 && blockData != block.getData())
				return;
			if (block.getTypeId() != blockId)
				return;
			if (WORLDS.get(p.getWorld())==null){
				WORLDS.put(p.getWorld(), true);
			}
			if (!WORLDS.get(p.getWorld()))return;
			jump.create(p);
			if (!NO_FALL.getPlayers().contains(p))
				NO_FALL.getPlayers().add(p);

		}
	}
	public int getBlockHigh() {
		return blockHigh;
	}
	public void setBlockHigh(int blockHigh) {
		this.blockHigh = blockHigh;
	}
	public int getBlockId() {
		return blockId;
	}
	public void setBlockId(int blockId) {
		this.blockId = blockId;
	}
	public int getBlockData() {
		return blockData;
	}
	public void setBlockData(int blockData) {
		this.blockData = blockData;
	}
	public Jump getJump() {
		return jump;
	}
	public void setJump(Jump jump) {
		this.jump = jump;
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
