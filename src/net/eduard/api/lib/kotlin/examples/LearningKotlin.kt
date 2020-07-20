package net.eduard.api.lib.kotlin.examples

import kotlin.math.pow

infix fun Number.adiciona(n: Number): Double {
    return this.toDouble() + n.toDouble()
}

infix fun Number.elevado(n: Number): Double {
    return this.toDouble().pow(n.toDouble())
}
infix fun Number.vezes(n: Number): Double {
    return this.toDouble().times(n.toDouble())
}
infix fun Number.divido(n: Number): Double {
    return this.toDouble().div(n.toDouble())
}
infix fun Number.tira(n: Number): Double {
    return this.toDouble() - n.toDouble()
}

operator fun String.times(n: Number): String {
    return repeat(n.toInt())
}


fun main() {
    TestingClass().test()
}
class User(val map: Map<String, Any?>) {
    val name: String by map                // 1
    val age: Int     by map                // 1
}
class TestingClass {

    fun test() {

        val n = Math.random()

        se(n < 0.5) {
            println("50 %")
        }


        val soma = 100f adiciona 10
        val subtração = 200L tira 0b1111_1111
        val elevarA3 = 0xF elevado 3

        println( (5 adiciona 10) divido (3 elevado 1) )




        val textoReplicado = " " * 0xF


    }

}


inline fun se(flag: Boolean, body: () -> Unit) {

    if (flag) {
        body.invoke()
    }
}

