package com.mc.lld.chess;

public class Pawn extends Piece {
    public Pawn(Color color, int row, int col) {
        super(color, row, col);
    }

    @Override
    public boolean canMove(Board board, int destRow, int destCol) {
        int rowDiff = destRow - row;
        int colDiff = Math.abs(destCol - col);
        // white pawn move up ward row1->row2.....
        if (color == Color.WHITE) {
            // A white Pawn can move one step forward if:
            //rowDiff == 1 (destination is one row ahead).
            //colDiff == 0 (it stays in the same column).
            return (rowDiff == 1 && colDiff == 0) ||
                    //On its first move, the white Pawn can move two steps forward if:
                    //It is on its initial row (row == 1).
                    //rowDiff == 2 (destination is two rows ahead).
                    //colDiff == 0 (it stays in the same column).
                    (row == 1 && rowDiff == 2 && colDiff == 0) ||
                    // A white Pawn can move diagonally to capture an opponent's piece if:
                    //rowDiff == 1 (destination is one row ahead).
                    //colDiff == 1 (destination is one column to the side).
                    //board.getPiece(destRow, destCol) != null (there is a piece to capture at the destination).
                    (rowDiff == 1 && colDiff == 1 && board.getPiece(destRow, destCol) != null);
        } else {
            // black pawn move to downward from row8->row7->row6
            return (rowDiff == -1 && colDiff == 0) ||
                    (row == 6 && rowDiff == -2 && colDiff == 0) ||
                    (rowDiff == -1 && colDiff == 1 && board.getPiece(destRow, destCol) != null);
        }
    }
}
