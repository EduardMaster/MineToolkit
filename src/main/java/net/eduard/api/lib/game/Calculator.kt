package net.eduard.api.lib.game

import lib.modules.Extra
import net.eduard.api.lib.storage.Storable.StorageAttributes

@StorageAttributes(inline = true)
class Calculator(
    var base: Double = 10.0,
    var perLevel: Double = 5.0,
    var limit: Double = 0.0,
    var variationPerLevel: Double = 0.0,
    var variation: Double = 0.0
) {
    constructor(
        base: Number = 10,
        perLevel: Number = 5,
        limit: Number = 0,
        variationPerLevel: Number = 0,
        variation: Number = 0
    ) : this(base.toDouble(), perLevel.toDouble(), limit.toDouble(), variationPerLevel.toDouble(), variation.toDouble())


    fun getValue(level: Double): Double {
        val result = perLevel * level + base
        val variationNumber = variation + variationPerLevel * level
        val random = lib.modules.Extra.getRandomDouble(-variationNumber, variationNumber)
        var resultFinal = result + random // + extra;
        if (limit > 0 && resultFinal > limit) {
            return limit
        }
        if (resultFinal <= 0) {
            resultFinal = 0.0
        }
        return resultFinal
    }

}