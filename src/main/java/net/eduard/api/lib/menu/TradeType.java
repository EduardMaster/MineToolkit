package net.eduard.api.lib.menu;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum TradeType {
    SELABLE("Venda", "Vendivel"),
    BUYABLE("Compra", "Compravel"),
    BOTH("Troca", "Trocavel");

    private String displayName;
    private String description;



}
