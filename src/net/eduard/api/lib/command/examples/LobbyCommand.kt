package net.eduard.api.lib.command.examples

import net.eduard.api.lib.command.Command
import net.eduard.api.lib.command.PlayerOnline

class LobbyCommand : Command("lobby", "hub") {
    override fun onCommand(sender: PlayerOnline<*>, args: List<String>) {
        sender.connect(args[0])


    }
}