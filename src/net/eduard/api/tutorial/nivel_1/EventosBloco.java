
package net.eduard.api.tutorial.nivel_1;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockCanBuildEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockExpEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockGrowEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.BrewEvent;
import org.bukkit.event.inventory.FurnaceBurnEvent;
import org.bukkit.event.inventory.FurnaceSmeltEvent;

public class EventosBloco implements Listener {

	@EventHandler
	public void BlocoAparecerAosPoucos(BlockFormEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void BlocoCairFolhas(LeavesDecayEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void BlocoColocar(BlockPlaceEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void BlocoDano(BlockDamageEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void BlocoDispensar(BlockDispenseEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void BlocoEstende(BlockPistonExtendEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	// PARA 1.8
	// @EventHandler
	// public void BlocoColocarVarios(BlockMultiPlaceEvent e) {
	// }
	@EventHandler
	public void BlocoFermentar(BrewEvent e) {

	}

	@EventHandler
	public void BlocoFogaoCozinhar(FurnaceBurnEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void BlocoFogaoQueimar(FurnaceSmeltEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void BlocoMover(BlockFromToEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void BlocoNascer(BlockGrowEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void BlocoNotar(NotePlayEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void BlocoPegarFogo(BlockBurnEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void BlocoPegarFogo(BlockIgniteEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void BlocoPodeModificalo(BlockCanBuildEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

//	@EventHandler
//	public void BlocoPysical(BlockPhysicsEvent e) {
//		ChatBukkit.broadcastMessage(e.getEventName())
//	}

	// Blocos
	@EventHandler
	public void BlocoQuebrar(BlockBreakEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void BlocoRedstone(BlockRedstoneEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void BlocoRetrai(BlockPistonRetractEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void BlocoSpread(BlockSpreadEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void BlocoSumirAosPoucos(BlockFadeEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void BlocoTrocarSign(SignChangeEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void BlocoXP(BlockExpEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}
}
