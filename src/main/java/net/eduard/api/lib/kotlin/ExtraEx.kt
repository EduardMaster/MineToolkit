package net.eduard.api.lib.kotlin

import net.eduard.api.lib.modules.Copyable
import net.eduard.api.lib.modules.Extra
import net.eduard.api.lib.storage.StorageAPI

/**
 * Alias para Copyable.copyObject, copia este Objeto
 *
 */
inline fun <T : Any> T.copy(): T {
    return Copyable.copyObject(this) as T
}
inline fun <reified  T : Any> store(){
    StorageAPI.autoRegisterClass(T::class.java)
}
inline fun <reified  T : Any> store(alias : String){
    StorageAPI.autoRegisterClass(T::class.java , alias)
}
inline fun Int.toRadians() = Math.toRadians(this.toDouble())
inline fun Double.toRadians() = Math.toRadians(this)


inline fun <reified T : Any> new(setup: T.() -> Unit): T {
    val element = T::class.java.newInstance() as T
    element.setup()
    return element
}


inline fun <reified T : Any> MutableList<T>.add(setup: T.() -> Unit): T {
    val element = T::class.java.newInstance() as T
    element.setup()
    add(element)
    return element
}

/**
 * Formata o Tempo do Jeito de Data ou por Duração
 */
fun Long.formatTime(durationFormat: Boolean = false): String {
    return if (durationFormat)
        Extra.formatTime(this) ?: "0"
    else
        Extra.FORMAT_DATETIME.format(this)
}


inline fun Long.formatDate() = Extra.FORMAT_DATE.format(this)

inline fun Long.formatHour() = Extra.FORMAT_TIME.format(this)

/**
 * Formata o numero no formato OP
 */
fun Number.format(formatOP: Boolean = true): String{
    return if (formatOP) Extra.formatMoney(this.toDouble())
    else Extra.MONEY.format(this)
}
inline val Number.text get() = Extra.MONEY.format(this)

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

        list.add(loreColor + this)
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

/**
 * Centraliza o slot atual para o centro, ignorando a coluna 1 e coluna 9
 * e linha 6
 */
fun Int.centralized(): Int {
    var valor = this
    while (Extra.isColumn(valor, 1) ||
        Extra.isColumn(valor, 9)) {
        valor++
    }
    return valor
}

/**
 * Alias para formatColors
 */
inline fun String.formatColors(): String {
    return Extra.formatColors(this)
}

/**
 * Alias para Extra.getChance() só que multiplica o Int por 100 antes
 */
inline fun Int.chance(): Boolean {
    return (this.toDouble() / 100).chance()
}

inline fun Double.percent() : String{
    return Extra.MONEY.format(this * 100.0)
}

/**
 * Alias para Extra.getChance()
 */
inline fun Double.chance(): Boolean {
    return Extra.getChance(this)
}

/**
 * Alias para Extra.cutText()
 */
inline fun String.cut(maxSize: Int): String {
    return Extra.cutText(this, maxSize)
}



inline fun String.lowerContains(msg: String) = Extra.contains(this, msg)







