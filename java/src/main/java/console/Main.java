package console;

import board.Board;
import console.print.BoardPrinter;
import console.command.CommandParser;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("|--------------|");
        System.out.println("| Mine Sweeper |");
        System.out.println("|--------------|\n");
        System.out.println("- INSTRUCTIONS -");
        System.out.println("Show the content of a square: s x y (example: s 2 3).");
        System.out.println("Mark a square as a mine: m x y (example: m 2 3).\n");
        System.out.println("Press enter to continue...");

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();

        // The game loop
        Board board = new Board();
        BoardPrinter boardPrinter = new BoardPrinter(board);
        while (board.gameRunning()) {
            // Clear screen
            for (int i = 0; i < 50; i++)
                System.out.println();

            // Show board and status
            boardPrinter.print(System.out);
            System.out.println("Mines left: " + board.minesLeft());

            // Get the next command and ignore it if it is invalid
            CommandParser.parse(scanner.nextLine())
                         .ifPresent(command -> command.execute(board));
        }

        // Ending: print the board again and show the outcome
        boardPrinter.print(System.out);
        System.out.print("\n\n");
        if (board.defeat())
            System.out.println("You lost! Better luck next time!");
        else
            System.out.println("Congratulations! You won!");
    }
}
