package com.mc.lld.xyz;

public abstract class Board {
    protected String[][] board;

    // Constructor to initialize the board size
    public Board(int size) {
        this.board = new String[size][size];
    }

    // Abstract method to be implemented by subclasses for specific initialization
    protected abstract void initializeBoard();

    public void printBoard() {
        for (String[] row : board) {
            for (String cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }

    public void setPosition(int x, int y, String symbol) {
        board[x][y] = symbol;
    }

    public String getPosition(int x, int y) {
        return board[x][y];
    }

    public String[][] getBoard() {
        return board;
    }
}

