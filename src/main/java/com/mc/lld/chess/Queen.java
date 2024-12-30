package com.mc.lld.chess;

public class Queen extends Piece {
    public Queen(Color color, int row, int col) {
        super(color, row, col);
    }

    /**
     * (rowDiff == colDiff) checks if the move is diagonal (the row and column differences are equal).
     * (row == destRow || col == destCol) checks if the move is in a straight line (either rows or columns are the same).
     * The Queen combines the movement capabilities of both a Bishop (diagonal) and a Rook (straight lines).
     * @param board
     * @param destRow
     * @param destCol
     * @return
     */
    @Override
    public boolean canMove(Board board, int destRow, int destCol) {
        int rowDiff = Math.abs(destRow - row);
        int colDiff = Math.abs(destCol - col);
        return (rowDiff == colDiff) || (row == destRow || col == destCol);
    }
}
