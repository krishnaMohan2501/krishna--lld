package com.mc.lld.xyz.service;

import com.mc.lld.xyz.model.Player;

public interface GameService {
    void startGame();
    boolean isGameDraw();
    boolean checkWinCondition(int row, int column, Player player);
    void makeMove(int row, int column, Player player);
}
