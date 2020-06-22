package net.eduard.api.lib.menu

enum class ShopSortType(var displayName: String) {
    SELL_PRICE_ASC("Preço de venda crescente."),
    SELL_PRICE_DESC("Preço de venda decrescente."),
    BUY_PRICE_ASC("Preço de compra crescente."),
    BUY_PRICE_DESC("Preço de compra decrescente.")
}