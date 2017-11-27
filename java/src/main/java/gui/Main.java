package gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application
{
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("gui.fxml"));

        stage.setTitle("Minesweeper");
        stage.setScene(new Scene(root));
        stage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }
}
