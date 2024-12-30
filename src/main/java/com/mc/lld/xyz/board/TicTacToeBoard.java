package com.mc.lld.xyz.board;

import com.mc.lld.xyz.Board;

import java.util.Arrays;

public class TicTacToeBoard extends Board {

    public TicTacToeBoard() {
        super(3); // Tic-Tac-Toe is always a 3x3 board
        initializeBoard();
    }

    @Override
    protected void initializeBoard() {
        for (String[] strings : board) {
            // Default symbol for empty space
            Arrays.fill(strings, "_");
        }
    }

    public boolean validateCoordinates(int x, int y) {
        return x < this.getBoard().length && y <this.getBoard()[0].length && x>=0 && y>=0;
    }

    public  boolean isBoardPositionEmpty(int x, int y) {
        return this.getPosition(x,y).equals("_");
    }
}
