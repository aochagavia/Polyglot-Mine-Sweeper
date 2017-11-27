package gui;

import javafx.scene.layout.Pane;

import java.util.HashMap;

public class Board extends Pane {
    private HashMap<board.Cell, Cell> cells;

    board.Board board;
    Runnable onFirstShowCell;
    Runnable onCellMarked;
    Runnable onGameEnd;

    public Board() { initialize(); }

    private void initialize() {
        // These variables could be adjustable...
        // Maybe this can help: http://stackoverflow.com/questions/39827416/javafx-create-custom-data-attributes-for-nodes
        final int width = 9;
        final int height = 9;
        final int mines = 10;

        // Create the board
        this.board = new board.Board(width, height, mines);
        this.setPrefSize(width * Cell.TILE_SIZE, height * Cell.TILE_SIZE);

        // Add the cells
        this.cells = new HashMap<>();
        for (int x = 0; x < width; x++)
            for (int y = 0; y < height; y++) {
                board.Cell boardCell = board.cellAt(x, y).get();
                Cell newCell = new Cell(x, y, boardCell, this::handleShow, this::handleMark);
                this.getChildren().add(newCell);
                this.cells.put(boardCell, newCell);
            }
    }

    void reset() {
        initialize();
    }

    private void handleShow(int x, int y) {
        // If the game has ended, ignore events
        if (!this.board.gameRunning())
            return;

        // If this is the first time the player shows a cell, start the stopwatch
        if (this.board.firstTurn())
            this.onFirstShowCell.run();

        // Show the cell in the board
        this.board.showCell(x, y);

        // Show all the cells uncovered at this point
        board.cellStream()
                .filter(boardCell -> boardCell.isShown())
                .forEach(cell -> this.cells.get(cell).showCell());

        // If the game has just ended, trigger the event
        if (!this.board.gameRunning())
            this.onGameEnd.run();
    }

    private void handleMark(int x, int y) {
        // If the game has ended, ignore events
        if (!this.board.gameRunning())
            return;

        board.Cell boardCell = this.board.cellAt(x, y).get();
        this.cells.get(boardCell).markCell();

        this.onCellMarked.run();
    }
}

@FunctionalInterface
interface HandleCellClick {
    void call(int x, int y);
}
