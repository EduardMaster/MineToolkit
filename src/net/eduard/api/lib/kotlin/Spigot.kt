package net.eduard.api.lib.kotlin

import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent

inline fun confirmationChat(acceptCommand: String, declineCommand: String): TextComponent {
    val texto = TextComponent("            ")
    val aceitar = TextComponent("§a§lACEITAR")
    aceitar.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, acceptCommand)

    val negar = TextComponent("§c§lNEGAR")
    negar.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, declineCommand)
    texto.addExtra(aceitar)
    texto.addExtra("   ")
    texto.addExtra(negar)
    return texto
}
inline val String.chat get() = TextComponent(this)
