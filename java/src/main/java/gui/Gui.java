package gui;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Gui {
    int seconds;

    public Label stopwatch;
    public Label minesLeft;
    public Board board;

    private Timeline timeline;

    @FXML
    void initialize() {
        this.stopwatch.setText("0");
        this.updateMinesLeft();

        timeline = new Timeline(new KeyFrame(Duration.millis(1000), ae -> {
            this.seconds++;
            updateSeconds();
        }));
        this.timeline.setCycleCount(Animation.INDEFINITE);

        this.board.onCellMarked = () -> this.updateMinesLeft();

        this.board.onFirstShowCell = () -> {
            // Start timer
            // Taken from http://tomasmikula.github.io/blog/2014/06/04/timers-in-javafx-and-reactfx.html
            timeline.playFromStart();
        };

        this.board.onGameEnd = () -> {
            this.timeline.stop();
            this.showEndDialog();
        };
    }

    @FXML
    void reset() {
        this.seconds = 0;
        this.board.reset();
        this.updateMinesLeft();
        this.timeline.stop();
        this.seconds = 0;
        this.updateSeconds();
    }

    private void showEndDialog() {
        Dialog dialog = new Dialog();
        if (this.board.board.defeat()) {
            dialog.setTitle("You lost!");
            dialog.setContentText("Better luck next time...");
        }
        else {
            dialog.setTitle("You won!");
            dialog.setContentText(String.format("Can you do better than %d seconds?", this.seconds));
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
        dialog.show();
    }

    private void updateSeconds() { this.stopwatch.setText(Integer.toString(this.seconds)); }
    private void updateMinesLeft() {
        this.minesLeft.setText(Integer.toString(this.board.board.minesLeft()));
    }
}
