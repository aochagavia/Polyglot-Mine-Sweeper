package console.print;

import board.Board;
import board.Cell;

import java.io.PrintStream;

public class BoardPrinter {
    Board board;

    public BoardPrinter(Board board) {
        this.board = board;
    }

    public void print(PrintStream stream) {
        int width = this.board.getWidth();
        int height = this.board.getHeight();

        // Column numbers
        stream.print("     ");
        for (int x = 0; x < width; x++) {
            // Spaces before each column number
            String spaces = x < 9 ? "  " : " ";
            stream.print(spaces);
            stream.print(x + 1);
        }

        // A horizontal line to separate the column numbers from the board
        int length = 3 * width;
        StringBuilder hLine = new StringBuilder();
        for (int i = 0; i < length; i++)
            hLine.append('_');

        stream.println();
        stream.print("     ");
        stream.println(hLine);

        // The vertical coordinate numbers are mixed with the printing of the board itself
        for (int y = 0; y < height; y++) {
            // Vertical coordinates
            String spaces = y < 9 ? "  " : " ";
            stream.print(spaces);
            stream.print(y + 1);

            // Horizontal line
            stream.print(" |");

            // Board row
            for (int x = 0; x < width; x++) {
                Cell cell = this.board.cellAt(x, y).get();

                // Mark
                if (cell.isMarked())
                    stream.print("  #");

                // Hidden
                else if (!cell.isShown())
                    stream.print("  *");

                // Shown mine
                else if (cell.isMine())
                    stream.print("  X");

                // Shown empty cell
                else if (cell.getNumber() == 0)
                    stream.print("   ");

                // Shown number
                else
                    stream.print("  " + cell.getNumber());
            }

            stream.print("\n\n");
        }
    }
}
