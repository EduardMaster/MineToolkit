package net.eduard.api.lib.command.examples

import net.eduard.api.lib.command.Command
import net.eduard.api.lib.command.PlayerSender

class FlyCommand : Command("fly", "voar") {
    override fun onCommand(sender: PlayerSender<*>, args: List<String>) {
        sender.fly = !sender.fly

    }
}