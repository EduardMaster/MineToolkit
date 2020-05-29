package net.eduard.api.lib.database.test;

import net.eduard.api.lib.database.annotations.*;



@TableName("players")
public class PlayerData {

    @ColumnPrimary
    private int id;

    @ColumnUnique
    @ColumnName("name")
    @ColumnSize(16)
    private String playerName;

    @ColumnName("rank")
    private String rankName;

    @ColumnName("cash")
    private double cashAmount;


}
