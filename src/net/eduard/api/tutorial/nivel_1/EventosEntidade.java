
package net.eduard.api.tutorial.nivel_1;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.EntityBlockFormEvent;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EntityBreakDoorEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityCombustByBlockEvent;
import org.bukkit.event.entity.EntityCombustByEntityEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityCreatePortalEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.entity.EntityPortalEnterEvent;
import org.bukkit.event.entity.EntityPortalEvent;
import org.bukkit.event.entity.EntityPortalExitEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.EntityTameEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.EntityUnleashEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.HorseJumpEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.entity.SheepDyeWoolEvent;
import org.bukkit.event.entity.SheepRegrowWoolEvent;
import org.bukkit.event.entity.SlimeSplitEvent;

public class EventosEntidade implements  Listener {

	@EventHandler
	public void EntidadeAtiraComFlecha(EntityShootBowEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeBlocoFormar(EntityBlockFormEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeCarneiroTrocaCor(SheepDyeWoolEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeCarneiroTrocaCor(SheepRegrowWoolEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeCavaloPulo(HorseJumpEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeCreaturaSpawn(CreatureSpawnEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeCreeperPower(CreeperPowerEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeCriarPortal(EntityCreatePortalEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeDano(EntityDamageEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeDanoPorBloco(EntityDamageByBlockEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeDanoPorEntidade(EntityDamageByEntityEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeDesprender(EntityUnleashEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeExplode(EntityExplodeEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeExplosionPrime(ExplosionPrimeEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeFoodChange(FoodLevelChangeEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeInteragir(EntityInteractEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeItemSome(ItemDespawnEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeItemSpawn(ItemSpawnEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeMira(EntityTargetEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeMiraUnidadeViva(EntityTargetLivingEntityEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeMorrer(EntityDeathEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeMudarBloco(EntityChangeBlockEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadePegandoFogoPorBloco(EntityCombustByBlockEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadePegandoFogoPorEntidade(EntityCombustByEntityEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadePegarFogo(EntityCombustEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadePortal(EntityPortalEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadePortalEntrar(EntityPortalEnterEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadePortalSair(EntityPortalExitEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeProjectileAcerta(ProjectileHitEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeProjectileLancar(ProjectileLaunchEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	// Entidade
	@EventHandler
	public void EntidadeQuebraPorta(EntityBreakDoorEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeRecuperaVida(EntityRegainHealthEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeSlimeMutiplica(SlimeSplitEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeTame(EntityTameEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}

	@EventHandler
	public void EntidadeTeleportar(EntityTeleportEvent e) {
		Bukkit.broadcastMessage(e.getEventName());
	}
}
