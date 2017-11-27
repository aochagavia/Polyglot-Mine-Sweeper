package board;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.stream.Stream;

public class Board {
    private Cell[] cells;
    private int totalMines;
    private int width, height;



    public Board() {
        this.width = 9;
        this.height = 9;
        this.totalMines = 10;
        this.createCells();
    }

    public Board(int width, int height, int mines) {
        this.width = width;
        this.height = height;
        this.totalMines = mines;
        this.createCells();
    }

    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public boolean defeat() {
        return Arrays.stream(this.cells).anyMatch(cell -> cell.isMine() && cell.isShown());
    }

    public boolean gameRunning() {
        // The game ends when we lose or when we win
        // Losing is handled by the `defeat` function
        // Winning happens if the amount of hidden cells is equal to the amount of mines and we haven't lost yet
        boolean victory = Arrays.stream(this.cells).filter(cell -> !cell.isShown()).count() == this.totalMines;
        return !this.defeat() && !victory;
    }

    public boolean firstTurn() {
        return Arrays.stream(this.cells).noneMatch(Cell::isShown);
    }

    public int minesLeft() {
        return this.totalMines - this.markedMines();
    }

    public void showCell(int x, int y) {
        if (!this.validCoords(x, y) || this.cell(x, y).isMarked())
            return;

        // If the cell is a mine, we show it and stop here
        if (this.cell(x, y).isMine()) {
            this.cell(x, y).show();
            return;
        }

        // If on the first turn, we need to spawn the mines
        if (this.firstTurn())
            this.spawnMines(x, y);

        // Now we need to show the cell and any empty surrounding cells
        Deque<Coords> coordsToShow = new ArrayDeque<>();
        coordsToShow.add(new Coords(x, y));
        while (!coordsToShow.isEmpty()) {
            Coords coords = coordsToShow.poll();
            Cell cell = this.cell(coords);

            // Ignore cells that have already been shown
            if (cell.isShown())
                continue;

            // Show this cell (and unmark it just in case)
            cell.unmark();
            cell.show();

            // If the cell is empty, enqueue all non-mine neighbors
            if (cell.number == 0)
                this.surroundingCoords(coords)
                    .filter(sCoords -> !this.cell(sCoords).isMine())
                    .forEach(coordsToShow::add);
        }
    }

    public Optional<Cell> cellAt(int x, int y) {
        if (!this.validCoords(x, y))
            return Optional.empty();

        int index = this.coordsToIndex(x, y);
        return Optional.of(this.cells[index]);
    }

    public Stream<Cell> cellStream() {
        return Arrays.stream(this.cells);
    }

    private void createCells() {
        this.cells = new Cell[this.width * this.height];

        // Populate the cells with empty ones
        for (int i = 0; i < this.cells.length; i++)
            this.cells[i] = new Cell();
    }

    private void spawnMines(int forbiddenX, int forbiddenY) {
        int forbiddenIndex = this.coordsToIndex(forbiddenX, forbiddenY);

        // Randomly spawn the mines
        int spawnedMines = 0;
        Random rand = new Random();
        while (spawnedMines < this.totalMines) {
            int randIndex = rand.nextInt(this.cells.length);

            // Make sure not to put a mine in the passed coordinates
            if (randIndex == forbiddenIndex)
                continue;

            Cell randomCell = this.cells[randIndex];
            if (!randomCell.isMine()) {
                randomCell.setMine();
                spawnedMines++;
            }
        }

        // Calculate the numbers for each free cell
        for (int x = 0; x < this.width; x++) {
            for (int y = 0; y < this.height; y++) {
                Cell cell = this.cell(x, y);
                if (!cell.isMine()) {
                    long number = surroundingCells(x, y).filter(Cell::isMine).count();
                    cell.setNumber((int)number);
                }
            }
        }
    }

    private Cell cell(int x, int y) {
        return this.cellAt(x, y).get();
    }

    private Cell cell(Coords c) {
        return this.cell(c.x, c.y);
    }

    private int coordsToIndex(int x, int y) {
        return y * this.width + x;
    }

    private Stream<Coords> surroundingCoords(Coords c) {
        return this.surroundingCoords(c.x, c.y);
    }

    private Stream<Coords> surroundingCoords(int x, int y) {
        ArrayList<Coords> surrounding = new ArrayList<>();

        for (int dx = -1; dx < 2; dx++)
            for (int dy = -1; dy < 2; dy++)
                if (!(dx == 0 && dy == 0) && this.validCoords(x + dx, y + dy))
                    surrounding.add(new Coords(x + dx, y + dy));

        return surrounding.stream();
    }

    private Stream<Cell> surroundingCells(int x, int y) {
        return this.surroundingCoords(x, y).map(this::cell);
    }

    private boolean validCoords(int x, int y) {
        return 0 <= x && x < this.width
            && 0 <= y && y < this.height;
    }

    private int markedMines() {
        return (int)Arrays.stream(this.cells).filter(Cell::isMarked).count();
    }
}

class Coords {
    public int x, y;
    public Coords(int x, int y) { this.x = x; this.y = y; }
}
