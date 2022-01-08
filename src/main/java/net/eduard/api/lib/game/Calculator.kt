package net.eduard.api.lib.game

import net.eduard.api.lib.storage.annotations.StorageAttributes


@StorageAttributes(inline = true)
class Calculator(
    var base: Double,
    var perLevel: Double,
    var limit: Double,
    var variationBetweenLevels: Double,
    var variationAditional: Double
) {
    companion object {
        fun linearByBasePercent(base: Double, basePercent: Double): Calculator {
            return Calculator(base, base * basePercent)
        }
    }

    constructor() : this(10.0, 5.0, 0.0)

    constructor(
        base: Number = 10,
        perLevel: Number = 5,
        limit: Number = 0,
        variationBetweenLevels: Number = 0,
        variationAditional: Number = 0
    ) : this(
        base.toDouble(),
        perLevel.toDouble(),
        limit.toDouble(),
        variationBetweenLevels.toDouble(),
        variationAditional.toDouble()
    )

    constructor(
        base: Number = 10,
        perLevel: Number = 5,
        limit: Number = 0,
    ) : this(base.toDouble(), perLevel.toDouble(), limit.toDouble(), 0, 0)

    constructor(
        base: Number = 10,
        perLevel: Number = 5
    ) : this(base.toDouble(), perLevel.toDouble(), 0, 0, 0)

    fun getValue(level: Double): Double {
        val result = perLevel * level + base
        var variationPower = 0
        if (variationBetweenLevels > 0 && level > 0)
            variationPower = (level / variationBetweenLevels).toInt()
        val variationTotal = variationPower * variationAditional
        var resultFinal = result + variationTotal
        if (limit > 0 && resultFinal > limit) {
            return limit
        }
        if (resultFinal <= 0) {
            resultFinal = 0.0
        }
        return resultFinal
    }

}