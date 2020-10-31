package net.eduard.api.lib.command

import net.eduard.api.lib.hybrid.IPlayer

typealias Sender = net.eduard.api.lib.hybrid.ISender
typealias PlayerBungee = net.eduard.api.lib.hybrid.BungeePlayer
typealias PlayerBukkit = net.eduard.api.lib.hybrid.BukkitPlayer

val IPlayer<*>.offline get() = PlayerOffline(name,uuid)