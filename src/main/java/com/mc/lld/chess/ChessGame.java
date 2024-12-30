package com.mc.lld.chess;

import java.util.ArrayList;
import java.util.List;

public class ChessGame {
    private final Board board;
    private final Player[] players;
    private int currentPlayer;
    List<Move> mockMoves = new ArrayList<>();

    public ChessGame() {
        board = new Board();
        players = new Player[]{new Player(Color.WHITE), new Player(Color.BLACK)};
        currentPlayer = 0;

        // Move 1: White pawn e2 to e4
        Piece whitePawn = board.getPiece(1, 4); // Pawn at e2
        mockMoves.add(new Move(whitePawn, 3, 4)); // Move to e4

// Move 2: Black pawn f7 to f5
        Piece blackPawn = board.getPiece(6, 5); // Pawn at f7
        mockMoves.add(new Move(blackPawn, 4, 5)); // Move to f5

// Move 3: White queen d1 to h5
        Piece whiteQueen = board.getPiece(0, 3); // Queen at d1
        mockMoves.add(new Move(whiteQueen, 4, 7)); // Move to h5 (Checkmate)
    }

    public void start() {
        // Game loop
        int mockMoveIndex = 0;
        while (!isGameOver()) {
            Player player = players[currentPlayer];
            System.out.println(player.getColor() + "'s turn.");

            // Get move from the player
            Move move = mockMoves.get(mockMoveIndex);

            // Make the move on the board
            try {
                player.makeMove(board, move);
            } catch (InvalidMoveException e) {
                System.out.println(e.getMessage());
                System.out.println("Try again!");
                continue;
            }

            // Switch to the next player
            currentPlayer = (currentPlayer + 1) % 2;
            mockMoveIndex++;
        }

        // Display game result
        displayResult();
    }

    private boolean isGameOver() {
        return board.isCheckmate(players[0].getColor()) || board.isCheckmate(players[1].getColor()) ||
                board.isStalemate(players[0].getColor()) || board.isStalemate(players[1].getColor());
    }

//    private Move getPlayerMove(Player player) {
//        // TODO: Implement logic to get a valid move from the player
        // For simplicity, let's assume the player enters the move via console input
//        Scanner scanner = new Scanner(System.in);
//        System.out.print("Enter source row: ");
//        int sourceRow = scanner.nextInt();
//        System.out.print("Enter source column: ");
//        int sourceCol = scanner.nextInt();
//        System.out.print("Enter destination row: ");
//        int destRow = scanner.nextInt();
//        System.out.print("Enter destination column: ");
//        int destCol = scanner.nextInt();
//
//        Piece piece = board.getPiece(sourceRow, sourceCol);
//        if (piece == null || piece.getColor() != player.getColor()) {
//            throw new IllegalArgumentException("Invalid piece selection!");
//        }
//        if (player.getColor().equals(Color.WHITE)) {
//
//        }
//
//        return new Move(piece, destRow, destCol);
//    }

    private void displayResult() {
        if (board.isCheckmate(Color.WHITE)) {
            System.out.println("Black wins by checkmate!");
        } else if (board.isCheckmate(Color.BLACK)) {
            System.out.println("White wins by checkmate!");
        } else if (board.isStalemate(Color.WHITE) || board.isStalemate(Color.BLACK)) {
            System.out.println("The game ends in a stalemate!");
        }
    }
}
