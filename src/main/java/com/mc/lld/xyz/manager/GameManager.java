package com.mc.lld.xyz.manager;

import com.mc.lld.xyz.model.Player;
import com.mc.lld.xyz.Board;
import com.mc.lld.xyz.service.GameService;
import com.mc.lld.xyz.board.TicTacToeBoard;
import com.mc.lld.xyz.service.TicTacToeService;

import java.util.List;
import java.util.Scanner;

public class GameManager {
    private GameService gameService;

    public GameManager(String gameType, Board board, List<Player> players, Scanner scanner) {
        // Decide which game service to delegate to based on the gameType
        switch (gameType.toLowerCase()) {
            case "tictactoe":
                this.gameService = new TicTacToeService((TicTacToeBoard) board, players, scanner);
                break;
            default:
                throw new IllegalArgumentException("Unsupported game type: " + gameType);
        }
    }

    public void startGame() {
        // Delegate to the correct game service
        gameService.startGame();
    }


}
