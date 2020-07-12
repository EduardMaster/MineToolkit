package net.eduard.api.lib.command.examples

import net.eduard.api.lib.command.Command
import net.eduard.api.lib.command.PlayerOnline

class FlyCommand : Command("fly", "voar") {
    override fun onCommand(sender: PlayerOnline<*>, args: List<String>) {
        sender.fly = !sender.fly

    }
}