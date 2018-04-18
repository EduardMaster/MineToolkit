
package net.eduard.api.tutorial.nivel_1;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.PlayerLeashEntityEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerAchievementAwardedEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRegisterChannelEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.player.PlayerStatisticIncrementEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerToggleFlightEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.event.player.PlayerUnleashEntityEvent;
import org.bukkit.event.player.PlayerUnregisterChannelEvent;
import org.bukkit.event.player.PlayerVelocityEvent;

public class EventosJogador implements  Listener  {

	@EventHandler
	public void JogadorAnimacao(PlayerAnimationEvent e) {

	}

	@EventHandler
	public void JogadorAtivarCorrida(PlayerToggleSprintEvent e) {

	}

	@EventHandler
	public void JogadorAtivarEsgueirar(PlayerToggleSneakEvent e) {

	}

	@EventHandler
	public void JogadorAtivarVoo(PlayerToggleFlightEvent e) {

	}

//	@EventHandler
//	public void JogadorBalde(PlayerBucketEvent e) {
//
//	}

	@EventHandler
	public void JogadorBaldeEncher(PlayerBucketFillEvent e) {

	}

	@EventHandler
	public void JogadorBaldeEsvasiar(PlayerBucketEmptyEvent e) {

	}

//	@EventHandler
//	public void JogadorCanal(PlayerChannelEvent e) {
//
//	}

	@EventHandler
	public void JogadorChatTabCompletar(PlayerChatTabCompleteEvent e) {

	}

	@EventHandler
	public void JogadorComandoProcesso(PlayerCommandPreprocessEvent e) {

	}

	@EventHandler
	public void JogadorCortarOvelha(PlayerShearEntityEvent e) {

	}

	@EventHandler
	public void JogadorDesprenderEntidade(PlayerUnleashEntityEvent e) {

	}

	@EventHandler
	public void JogadorEditarBook(PlayerEditBookEvent e) {

	}

	@EventHandler
	public void JogadorEntrar(PlayerJoinEvent e) {

	}

	@EventHandler
	public void JogadorEntrarCama(PlayerBedEnterEvent e) {

	}

	@EventHandler
	public void JogadorGamemodeMudar(PlayerGameModeChangeEvent e) {

	}

	@EventHandler
	public void JogadorInteragir(PlayerInteractEvent e) {

	}

	@EventHandler
	public void JogadorInteragirEntidade(PlayerInteractEntityEvent e) {

	}

	@EventHandler
	public void JogadorItemConsumir(PlayerItemConsumeEvent e) {

	}

	@EventHandler
	public void JogadorItemDropar(PlayerDropItemEvent e) {

	}

	@EventHandler
	public void JogadorItemQuebrar(PlayerItemBreakEvent e) {

	}

	@EventHandler
	public void JogadorItemSlotMudar(PlayerItemHeldEvent e) {

	}

	@EventHandler
	public void JogadorJogarOvos(PlayerEggThrowEvent e) {

	}

	@EventHandler
	public void JogadorKickar(PlayerKickEvent e) {

	}

	@EventHandler
	public void JogadorLevelMudar(PlayerLevelChangeEvent e) {

	}

	@EventHandler
	public void JogadorLogin(PlayerLoginEvent e) {

	}

	@EventHandler
	public void JogadorMorrer(PlayerDeathEvent e) {

	}

	@EventHandler
	public void JogadorMover(PlayerMoveEvent e) {

	}

	@EventHandler
	public void JogadorMudarXP(PlayerExpChangeEvent e) {

	}

//	@EventHandler
//	public void JogadorName(PlayerNamePrompt e) {
//
//	}

	@EventHandler
	public void JogadorPegarItem(PlayerPickupItemEvent e) {

	}

	@EventHandler
	public void JogadorPescar(PlayerFishEvent e) {

	}

	@EventHandler
	public void JogadorPortal(PlayerPortalEvent e) {

	}

	@EventHandler
	public void JogadorPreLogar(AsyncPlayerPreLoginEvent e) {

	}

	@EventHandler
	public void JogadorPremioGanhado(AsyncPlayerChatEvent e) {

	}

	@EventHandler
	public void JogadorPremioGanhado(PlayerAchievementAwardedEvent e) {

	}

	@EventHandler
	public void JogadorPrenderEntidade(PlayerLeashEntityEvent e) {

	}

	@EventHandler
	public void JogadorRegistrarCanal(PlayerRegisterChannelEvent e) {

	}

	@EventHandler
	public void JogadorRenascer(PlayerRespawnEvent e) {

	}

	@EventHandler
	public void JogadorSair(PlayerQuitEvent e) {

	}

	@EventHandler
	public void JogadorSairCama(PlayerBedLeaveEvent e) {

	}

	@EventHandler
	public void JogadorStatsMudar(PlayerStatisticIncrementEvent e) {

	}

	@EventHandler
	public void JogadorTeleportar(PlayerTeleportEvent e) {

	}

	@EventHandler
	public void JogadorTrocarMundo(PlayerChangedWorldEvent e) {

	}

	@EventHandler
	public void JogadorUnCanal(PlayerUnregisterChannelEvent e) {

	}

	@EventHandler
	public void JogadorVelocidade(PlayerVelocityEvent e) {

	}
}
