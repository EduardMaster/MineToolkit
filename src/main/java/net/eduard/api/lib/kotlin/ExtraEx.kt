package net.eduard.api.lib.kotlin

import net.eduard.api.lib.modules.Extra

fun Number.format() = Extra.formatMoney(this.toDouble())

fun <T : Any> Array<T>.shuffle(): Array<T> {
    for (index in 0 until size) {
        val temp = this[index]
        val sorted = Extra.getRandomInt(1, size) - 1
        this[index] = this[sorted]
        this[sorted] = temp

    }
    return this
}

fun String.toLore(maxCharsPerLine: Int = 70, loreColor: String = "§7"): MutableList<String> {
    val list = mutableListOf<String>()
    if (contains(" ")) {
        var charCount = 0

        val words = split(" ")
        val text = StringBuilder()
        text.append(loreColor)
        for (word in words) {
            charCount += word.count()
            if (charCount >= maxCharsPerLine) {
                charCount = 0
                text.append(word)
                list.add(text.toString())
                text.clear()
                text.append(loreColor)
            } else {
                text.append("$word ")
            }
        }
    } else {

        list.add("$loreColor" + this)
    }

    return list
}

fun <T : Any> List<T>.randomByPercent(getDouble: (T.() -> Double)): T {
    val percentMax = sumByDouble(getDouble)
    var dataMinChance = 0.0
    val percentSorted = percentMax * Math.random()
    for (data in this) {
        val dataMaxChance = dataMinChance + getDouble.invoke(data)
        if (percentSorted > dataMinChance && percentSorted < dataMaxChance) {
            return data
        }
        dataMinChance = dataMaxChance
    }

    return this[0]
}

fun Int.centralized(): Int {
    var valor = this
    while (Extra.isColumn(valor, 1) || Extra.isColumn(valor, 9)) {
        valor++
    }
    return valor
}

fun String.formatColors(): String {
    return Extra.formatColors(this)
}


fun Int.chance(): Boolean {
    return (this.toDouble() / 100).chance()
}

fun Double.chance(): Boolean {
    return Extra.getChance(this)
}

fun String.cut(maxSize: Int): String {
    return Extra.cutText(this, maxSize)
}

fun String.lowerContains(msg: String) = Extra.contains(this, msg)

inline operator fun <T : Any> T.invoke(block: T.() -> Unit): T {
    block(this)
    return this
}







