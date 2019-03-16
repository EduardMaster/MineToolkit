package net.eduard.api.lib.old;

import org.bukkit.entity.Player;

import net.eduard.api.lib.click.PlayerEffect;
/**
 * Representa o efeito feito no jogador
 * @version 1.0
 * @since 0.9
 * @author Eduard
 * @deprecated Versão atual {@link PlayerEffect}
 *
 */
public abstract interface PlayerManager
{
  public abstract void playerEvent(Player player);
}
