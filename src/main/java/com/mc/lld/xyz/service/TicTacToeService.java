package com.mc.lld.xyz.service;

import com.mc.lld.xyz.board.TicTacToeBoard;
import com.mc.lld.xyz.model.Player;

import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TicTacToeService implements GameService {

    private TicTacToeBoard board;
    private List<Player> players;
    // Use AtomicInteger for thread-safe counter
    private final AtomicInteger gameCount = new AtomicInteger(0);
    // Lock for critical sections
    private final Lock gameLock = new ReentrantLock();
    // Volatile to ensure visibility across threads
    private volatile boolean gameEnded = false;
    private Scanner scanner;

    public TicTacToeService(TicTacToeBoard board, List<Player> players, Scanner scanner) {
        this.board = board;
        this.players = players;
        this.scanner = scanner;
    }

    @Override
    public void startGame() {

        gameCount.set(0);
        gameEnded = false;
        board.printBoard();

        int i = 0;
        while (true) {
            if (i >= players.size()) {
                i = 0;
            }
            System.out.println(players.get(i).getPlayerName() + " Turn " + players.get(i).getSymbol() + "\n");
            System.out.println("Enter your move as row and column numbers separated by a space (e.g., 1 2):");
            int x = scanner.nextInt();
            int y = scanner.nextInt();
            if (board.validateCoordinates(x, y) && board.isBoardPositionEmpty(x, y)) {
                Player player = players.get(i);
                gameLock.lock();
                makeMove(x, y, player);
                gameLock.unlock();
                board.printBoard();
                if (checkWinCondition(x, y, player)) {
                    System.out.println(" Player " + player.getPlayerName() + " Wins");
                    break;
                }
                gameCount.incrementAndGet();
                if (isGameDraw()) {
                    System.out.println("Game Draw");
                    break;
                }
            } else {
                System.out.println("Invalid input");
                break;
            }
            i++;
        }
    }

    @Override
    public boolean isGameDraw() {
        return gameCount.get() == board.getBoard().length * board.getBoard()[0].length;
    }

    @Override
    public boolean checkWinCondition(int row, int column, Player player) {
        boolean winRow = true, winColm = true, winLeft = true, winRight = true;
        for (int i = 0; i < board.getBoard().length; i++) {
            if (!board.getPosition(row, i).equals(player.getSymbol())) { // check entire row
                winRow = false;
            }
            if (!board.getPosition(i, column).equals(player.getSymbol())) { // check entire column
                winColm = false;
            }
            if (!board.getPosition(i, i).equals(player.getSymbol())) { // diagonal -> left to right
                winLeft = false;
            }
            if (!board.getPosition(i, board.getBoard().length - 1 - i).equals(player.getSymbol())) { // diagonal -> right -> left
                winRight = false;
            }
        }
        return winRow || winColm || winLeft || winRight;
    }

    @Override
    public void makeMove(int row, int column, Player player) {
        board.setPosition(row, column, player.getSymbol());
    }

}
