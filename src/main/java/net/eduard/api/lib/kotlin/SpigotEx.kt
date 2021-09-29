package net.eduard.api.lib.kotlin

import net.md_5.bungee.api.ChatColor
import net.md_5.bungee.api.chat.BaseComponent
import net.md_5.bungee.api.chat.ClickEvent
import net.md_5.bungee.api.chat.TextComponent

fun confirmationChat(
    acceptCommand: String, declineCommand: String,
    confirmationText: String = "§a§lACEITAR",

    negationText: String = "§c§lNEGAR"
): TextComponent {
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

inline val String.chat get() = TextComponent(this).fixColors()

fun String.lastChatColor(): ChatColor {
    val array = toLowerCase().toCharArray()
    var lastColor = ChatColor.WHITE
    var lastChar = 0.toChar()
    for (caracter in array) {
        if (lastChar == ChatColor.COLOR_CHAR) {
            val byCharColor = ChatColor.getByChar(caracter)
            if (byCharColor!= null) {
                lastColor = byCharColor
            }
        }
        lastChar = caracter
    }
    return lastColor
}
fun BaseComponent.fixColors(): BaseComponent {
    if (this is TextComponent) {
        color=this.text.lastChatColor()
    }
    if (extra != null)
        for (extraLoop in extra) {
            extraLoop.fixColors();
        }
    return this
}
