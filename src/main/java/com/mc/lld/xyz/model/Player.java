package com.mc.lld.xyz.model;

public class Player {
    private String playerName;
    private int id;
    private String symbol; // in case of chess it is color.


    public Player(String playerName, int id, String symbol) {
        this.playerName = playerName;
        this.id = id;
        this.symbol = symbol;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }
}
