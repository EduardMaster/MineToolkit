package net.eduard.api.lib.kotlin

import net.eduard.api.lib.modules.Extra

inline fun Number.format() = Extra.formatMoney(this.toDouble())

fun Int.centralized(): Int {
    while (Extra.isColumn(this, 1) || Extra.isColumn(this, 9)) {
        this.inc()
    }
    return this
}

inline fun String.formatColors(): String {
    return Extra.formatColors(this)
}


inline fun Integer.chance(): Boolean {
    return (this.toDouble() / 100).chance()
}

inline fun Double.chance(): Boolean {
    return Extra.getChance(this)
}

inline fun String.cut(maxSize: Int): String {
    return Extra.cutText(this, maxSize)
}

inline fun String.lowerContains(msg: String) = Extra.contains(this, msg)

inline operator fun <T> T.invoke(block : T.() -> Unit) : T {
    block(this)
    return this
}







