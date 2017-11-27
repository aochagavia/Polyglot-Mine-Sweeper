package console.command;

import board.Board;

public class ShowCommand implements Command {
    int x, y;

    public ShowCommand(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void execute(Board board) {
        board.showCell(this.x, this.y);
    }
}
