package net.eduard.api.lib.command.examples

import net.eduard.api.lib.command.Command
import net.eduard.api.lib.command.PlayerSender

class LobbyCommand : Command("lobby", "hub") {
    override fun onCommand(sender: PlayerSender<*>, args: List<String>) {
        sender.connect(args[0])


    }
}