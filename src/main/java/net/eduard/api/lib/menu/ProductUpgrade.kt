package net.eduard.api.lib.menu

import org.bukkit.entity.Player

class ProductUpgrade(var level: Int = 1, product: Product? = null) {

    @Transient
    lateinit var productParent: Product

    init {
        if (product != null) {
            productParent = product!!
            productParent.upgrades.add(this)
        }
    }

    var name = "Produto nivel $level"
    val displayName get() = productParent.name + " Nível $level"

    var price = 10000.0

    val permission get() = productParent.permission + "." + level
    fun hasBought(player: Player) = player.hasPermission(permission)


}