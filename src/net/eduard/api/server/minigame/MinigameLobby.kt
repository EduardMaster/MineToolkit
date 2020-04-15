package net.eduard.api.server.minigame

import java.util.ArrayList

import net.eduard.api.lib.storage.Storable

class MinigameLobby : Storable {

    var id = 1

    var slot: Int = 0


    @Transient
    var players = ArrayList<MinigamePlayer>()

    fun join(player: MinigamePlayer) {
        for (p in players) {
            p.show(player)
            player.show(p)
        }
        player.lobby = this
        if (!players.contains(player))
            players.add(player)

    }

    fun leave(player: MinigamePlayer) {
        for (p in players) {
            p.hide(player)
            player.hide(p)
        }
        player.lobby = null
        players.remove(player)
    }

}
