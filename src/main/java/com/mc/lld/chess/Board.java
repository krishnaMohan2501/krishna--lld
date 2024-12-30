package com.mc.lld.chess;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private final Piece[][] board;

    public Board() {
        board = new Piece[8][8];
        initializeBoard();
    }

    private void initializeBoard() {
        // Initialize white pieces
        board[0][0] = new Rook(Color.WHITE, 0, 0);
        board[0][1] = new Knight(Color.WHITE, 0, 1);
        board[0][2] = new Bishop(Color.WHITE, 0, 2);
        board[0][3] = new Queen(Color.WHITE, 0, 3);
        board[0][4] = new King(Color.WHITE, 0, 4);
        board[0][5] = new Bishop(Color.WHITE, 0, 5);
        board[0][6] = new Knight(Color.WHITE, 0, 6);
        board[0][7] = new Rook(Color.WHITE, 0, 7);
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(Color.WHITE, 1, i);
        }

        // Initialize black pieces
        board[7][0] = new Rook(Color.BLACK, 7, 0);
        board[7][1] = new Knight(Color.BLACK, 7, 1);
        board[7][2] = new Bishop(Color.BLACK, 7, 2);
        board[7][3] = new Queen(Color.BLACK, 7, 3);
        board[7][4] = new King(Color.BLACK, 7, 4);
        board[7][5] = new Bishop(Color.BLACK, 7, 5);
        board[7][6] = new Knight(Color.BLACK, 7, 6);
        board[7][7] = new Rook(Color.BLACK, 7, 7);
        for (int i = 0; i < 8; i++) {
            board[6][i] = new Pawn(Color.BLACK, 6, i);
        }
    }

    public Piece getPiece(int row, int col) {
        return board[row][col];
    }

    public void setPiece(int row, int col, Piece piece) {
        board[row][col] = piece;
    }

    public boolean isValidMove(Piece piece, int destRow, int destCol) {
        if (piece == null || destRow < 0 || destRow > 7 || destCol < 0 || destCol > 7) {
            return false;
        }
        Piece destPiece = board[destRow][destCol];
        return (destPiece == null || destPiece.getColor() != piece.getColor()) &&
                piece.canMove(this, destRow, destCol);
    }

    // Checks if the player of the given color is in checkmate
    public boolean isCheckmate(Color color) {
        if (!isKingInCheck(color)) {
            return false; // If the king is not in check, it cannot be checkmate
        }

        // Get all possible legal moves for the current player
        List<Move> legalMoves = getAllLegalMoves(color);

        // If the player has no legal moves, it's a checkmate
        return legalMoves.isEmpty();
    }

    // Checks if the player of the given color is in stalemate
    public boolean isStalemate(Color color) {
        if (isKingInCheck(color)) {
            return false; // If the king is in check, it cannot be stalemate
        }

        // Get all possible legal moves for the current player
        List<Move> legalMoves = getAllLegalMoves(color);

        // If the player has no legal moves, it's a stalemate
        return legalMoves.isEmpty();
    }

    // Helper method to check if the king of the given color is in check
    private boolean isKingInCheck(Color color) {
        // Find the position of the king of the given color
        int[] kingPosition = findKingPosition(color);

        // Check if any opponent piece can attack the king
        return isUnderAttack(kingPosition, getOpponentColor(color));
    }

    // Finds the position of the king for the given color
    private int[] findKingPosition(Color color) {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = getPiece(row, col);
                if (piece != null && piece.getColor().equals(color) && piece instanceof King) {
                    return new int[] {row, col};
                }
            }
        }
        throw new IllegalStateException("King not found on the board for color: " + color);
    }

    // Checks if a position is under attack by the opponent
    private boolean isUnderAttack(int[] kingPosition, Color opponentColor) {
        // Get all opponent moves
        List<Move> opponentMoves = getAllLegalMoves(opponentColor);

        // Check if any opponent move targets the given position
//        for (Move move : opponentMoves) {
//            if (move.getTarget().equals(position)) {
//                return true;
//            }
//        }
        return false;
    }

    // Retrieves all legal moves for the player of the given color
    private List<Move> getAllLegalMoves(Color opponentColor) {
        List<Move> legalMoves = new ArrayList<>();
//        for (int row = 0; row < 8; row++) {
//            for (int col = 0; col < 8; col++) {
//                Piece piece = getPiece(row, col);
//                if (piece != null && piece.getColor().equals(opponentColor)) {
//                    legalMoves.addAll(piece.getLegalMoves(this, new Position(row, col)));
//                }
//            }
//        }
        return legalMoves;
    }

    // Placeholder for getting the opponent's color
    private Color getOpponentColor(Color color) {
        return color.equals(Color.WHITE) ? Color.WHITE : Color.BLACK;
    }
}
