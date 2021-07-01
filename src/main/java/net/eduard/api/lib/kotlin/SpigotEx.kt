package net.eduard.api.lib.kotlin

import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent

fun confirmationChat(acceptCommand: String, declineCommand: String,
                     confirmationText: String = "§a§lACEITAR",

negationText : String = "§c§lNEGAR"): TextComponent {
    val texto = TextComponent("            ")
    val aceitar = TextComponent(confirmationText)
    aceitar.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, acceptCommand)

    val negar = TextComponent(negationText)
    negar.clickEvent = ClickEvent(ClickEvent.Action.RUN_COMMAND, declineCommand)
    texto.addExtra(aceitar)
    texto.addExtra("   ")
    texto.addExtra(negar)
    return texto
}
inline val String.chat get() = TextComponent(this)
