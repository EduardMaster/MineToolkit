package net.eduard.api.command.bungee

import net.eduard.api.EduardAPIBungee
import net.md_5.bungee.api.CommandSender
import net.md_5.bungee.api.chat.TextComponent
import net.md_5.bungee.api.plugin.Command

class BungeeReloadCommand : Command("bungeereload") {


    override fun execute(sender: CommandSender, args: Array<String>) {

        sender.sendMessage(TextComponent("§aToda configuracao foi recarregada!"))
        EduardAPIBungee.instance.reload()
    }

}