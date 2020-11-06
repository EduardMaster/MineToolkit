package net.eduard.api.lib.kotlin

import lib.modules.Extra

fun Number.format() = lib.modules.Extra.formatMoney(this.toDouble())

fun Int.centralized(): Int {
    var valor = this
    while (lib.modules.Extra.isColumn(valor, 1) || lib.modules.Extra.isColumn(valor, 9)) {
        valor++
    }
    return valor
}

fun String.formatColors(): String {
    return lib.modules.Extra.formatColors(this)
}


fun Int.chance(): Boolean {
    return (this.toDouble() / 100).chance()
}

fun Double.chance(): Boolean {
    return lib.modules.Extra.getChance(this)
}

fun String.cut(maxSize: Int): String {
    return lib.modules.Extra.cutText(this, maxSize)
}

fun String.lowerContains(msg: String) = lib.modules.Extra.contains(this, msg)

inline operator fun <T : Any> T.invoke(block : T.() -> Unit) : T {
    block(this)
    return this
}







