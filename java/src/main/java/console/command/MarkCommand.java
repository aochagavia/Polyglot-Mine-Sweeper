package console.command;

import board.Board;

public class MarkCommand implements Command {
    int x, y;

    public MarkCommand(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void execute(Board board) {
        board.cellAt(this.x, this.y).get().toggleMark();
    }
}
