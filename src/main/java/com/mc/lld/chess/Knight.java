package com.mc.lld.chess;

public class Knight extends Piece {
    public Knight(Color color, int row, int col) {
        super(color, row, col);
    }

    /**
     * It can move 2 steps in one direction (row or column) and 1 step in the perpendicular direction.
     * Alternatively, it can move 1 step in one direction and 2 steps in the perpendicular direction.
     * @param board
     * @param destRow
     * @param destCol
     * @return
     */
    @Override
    public boolean canMove(Board board, int destRow, int destCol) {
        int rowDiff = Math.abs(destRow - row);
        int colDiff = Math.abs(destCol - col);
        return (rowDiff == 2 && colDiff == 1) || (rowDiff == 1 && colDiff == 2);
    }
}
