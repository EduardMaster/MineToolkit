package net.eduard.api.lib.database.test;

import net.eduard.api.lib.database.annotations.*;



@TableName("players")
public class PlayerData {
    public PlayerData(){

    }
    public PlayerData(int id, String playerName, String rankName, double cashAmount) {
        this.id = id;
        this.playerName = playerName;
        this.rankName = rankName;
        this.cashAmount = cashAmount;
    }

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getRankName() {
        return rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public double getCashAmount() {
        return cashAmount;
    }

    public void setCashAmount(double cashAmount) {
        this.cashAmount = cashAmount;
    }

    @Override
    public String toString() {
        return "PlayerData{" +
                "id=" + id +
                ", playerName='" + playerName + '\'' +
                ", rankName='" + rankName + '\'' +
                ", cashAmount=" + cashAmount +
                '}';
    }
}
