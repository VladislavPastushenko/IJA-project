package tool;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.fxml.FXMLLoader;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.net.URL;

import tool.common.*;
import tool.common.CommonField.Direction;
import tool.game.*;

public class Main extends Application {
    CommonMazeObject pacman;
    Direction direction = null;
    CommonMaze maze;

    public VBox buildMaze(CommonMaze maze) {
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
                            pacman = obj;
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

        return vbox;
    }

    @Override
    public void start(Stage stage) throws Exception {
        MazeConfigure cfg = new MazeConfigure();
        cfg.readFromFile("/Users/marina/IJA-project/mapa01.txt");
        cfg.stopReading();


        stage.setTitle("IJA Pacman");
        stage.setWidth(600);
        stage.setHeight(600);

        maze = cfg.createMaze();

        VBox vbox = buildMaze(maze);
        Scene primaryScene = new Scene(vbox);
        stage.setScene(primaryScene);
        stage.show();

        primaryScene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
            case UP:
                direction = Direction.U;
               break;
            case DOWN:
                direction = Direction.D;
               break;
            case LEFT:
                direction = Direction.L;
               break;
            case RIGHT:
                direction = Direction.R;
               break;
            }
         });

         Timeline eventLoop = new Timeline(
                 new KeyFrame(Duration.seconds(0.5),
                 new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                tick(stage);
            }
        }));
        eventLoop.setCycleCount(Timeline.INDEFINITE);
        eventLoop.play();
    }

    public void tick(Stage stage) {
        if (direction != null) {
            pacman.move(direction);
        }

        maze.randomGhostsMovement();

        VBox vbox = buildMaze(maze);
        Scene newScene = new Scene(vbox);
        stage.setScene(newScene);

        newScene.setOnKeyPressed(e -> {
            switch (e.getCode()) {
            case UP:
                direction = Direction.U;
               break;
            case DOWN:
                direction = Direction.D;
               break;
            case LEFT:
                direction = Direction.L;
               break;
            case RIGHT:
                direction = Direction.R;
               break;
            }
         });
    }

    public static void delay(long millis, Runnable continuation) {
        Task<Void> sleeper = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                try { Thread.sleep(millis); }
                catch (InterruptedException e) { }
                return null;
            }
        };
        sleeper.setOnSucceeded(event -> continuation.run());
        new Thread(sleeper).start();
      }

    public static void main(String[] args) {
        launch();
    }
}