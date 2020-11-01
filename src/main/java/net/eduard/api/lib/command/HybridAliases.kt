package net.eduard.api.lib.command

import net.eduard.api.lib.hybrid.*

typealias Sender = ISender
typealias PlayerBungee = BungeePlayer
typealias PlayerBukkit = BukkitPlayer
typealias PlayerOffline = PlayerUser
typealias PlayerOnline<T> = IPlayer<T>
val IPlayer<*>.offline get() = PlayerUser(name, uuid)