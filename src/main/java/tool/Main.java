package tool;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.fxml.FXMLLoader;


import java.net.URL;

import tool.common.*;
import tool.game.*;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        MazeConfigure cfg = new MazeConfigure();
        cfg.readFromFile("/Users/marina/IJA-project/maze01.txt");
        cfg.stopReading();

        CommonMaze maze = cfg.createMaze();

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);

        for (int row = 0; row < maze.numRows(); row++) {

            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER);

            for (int col = 0; col < maze.numCols(); col++) {
                CommonField field = maze.getField(row, col);

                Label fieldLabel;

                if (field.canMove()) {
                    if (field.isEmpty()) {
                        fieldLabel = new Label("_");
                    }
                    else {
                        CommonMazeObject obj = field.get();
                        if (obj.isPacman()) {
                            fieldLabel = new Label("P");
                        } else {
                            fieldLabel = new Label("G");
                        }
                    }
                }
                else {
                    fieldLabel = new Label("X");
                }
                hbox.getChildren().add(fieldLabel);
            }
            vbox.getChildren().add(hbox);
        }

        stage.setTitle("IJA Pacman");
        stage.setWidth(600);
        stage.setHeight(600);

        Scene primaryScene = new Scene(vbox);
        stage.setScene(primaryScene);

        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}