package net.eduard.api.lib.menu

enum class TradeType(var displayName: String,var description: String) {
    SELABLE("Venda", "Vendivel"), BUYABLE("Compra", "Compravel"), BOTH("Troca", "Trocavel");

    fun getName(): String {
        return displayName
    }
}