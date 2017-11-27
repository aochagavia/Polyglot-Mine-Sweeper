package gui;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Cell extends StackPane {
    static final int TILE_SIZE = 40;
    private int x, y;
    private board.Cell boardCell;
    private Text text = new Text();
    private Rectangle body = new Rectangle(TILE_SIZE - 2, TILE_SIZE - 2);

    Cell(int x, int y, board.Cell boardCell, HandleCellClick showCell, HandleCellClick markCell) {
        this.x = x;
        this.y = y;
        this.boardCell = boardCell;
        this.body.setStroke(Color.DARKGRAY);
        this.text.setFont(Font.font(18));
        this.text.setVisible(true);
        this.hideCell();

        this.getChildren().addAll(this.body, this.text);
        this.setTranslateX(this.x * TILE_SIZE);
        this.setTranslateY(this.y * TILE_SIZE);
        this.setOnMouseClicked(e -> {
            switch (e.getButton()) {
                case PRIMARY:
                    showCell.call(this.x, this.y);
                    break;
                case SECONDARY:
                    markCell.call(this.x, this.y);
                    break;
            }
        });
    }

    void showCell() {
        this.body.setFill(Color.AQUAMARINE);

        // Mine
        if (this.boardCell.isMine()) {
            this.text.setText("X");
            this.body.setFill(Color.DARKRED);
        }

        // Number
        else if (this.boardCell.getNumber() != 0)
            this.text.setText(Integer.toString(this.boardCell.getNumber()));

        // Note: empty cells don't show any text
    }

    void hideCell() {
        this.body.setFill(Color.LIGHTGRAY);
        this.text.setText("");
    }

    void markCell() {
        if (this.boardCell.isShown())
            return;

        this.boardCell.toggleMark();

        if (this.boardCell.isMarked()) {
            this.body.setFill(Color.LIGHTSALMON);
            this.text.setText("?");
        } else if (this.boardCell.isShown()) {
            this.showCell();
        } else {
            this.hideCell();
        }
    }
}
