package tool;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;

import tool.common.*;
import tool.common.CommonField.Direction;
import tool.game.*;

public class Main extends Application {
    CommonMazeObject pacman;
    Direction direction = null;
    CommonMaze maze;

    public VBox buildMaze(CommonMaze maze) throws FileNotFoundException {
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        Image imageOfGhost = new Image(new FileInputStream("/Users/marina/IJA-project/src/main/java/tool/images/ghost.png"));
        Image imageOfWall = new Image(new FileInputStream("/Users/marina/IJA-project/src/main/java/tool/images/bedrock.png"));
        Image imageOfTarget = new Image(new FileInputStream("/Users/marina/IJA-project/src/main/java/tool/images/target.png"));
        Image imageOfKey = new Image(new FileInputStream("/Users/marina/IJA-project/src/main/java/tool/images/key.png"));
        Image imageOfPacmanUp = new Image(new FileInputStream("/Users/marina/IJA-project/src/main/java/tool/images/pacmanUp.png"));
        Image imageOfPacmanRight = new Image(new FileInputStream("/Users/marina/IJA-project/src/main/java/tool/images/pacmanRight.png"));
        Image imageOfPacmanLeft = new Image(new FileInputStream("/Users/marina/IJA-project/src/main/java/tool/images/pacmanLeft.png"));
        Image imageOfPacmanDown = new Image(new FileInputStream("/Users/marina/IJA-project/src/main/java/tool/images/pacmanDown.png"));
        Image imageOfPath = new Image(new FileInputStream("/Users/marina/IJA-project/src/main/java/tool/images/dirt.png"));

        for (int row = 0; row < maze.numRows(); row++) {

            HBox hbox = new HBox();
            hbox.setAlignment(Pos.CENTER);

            for (int col = 0; col < maze.numCols(); col++) {
                CommonField field = maze.getField(row, col);

                Label fieldLabel;
                ImageView imageView;
                

                if (field.canMove()) {
                    if (field.isEmpty()) {
                        if (field.isTarget()) {
                            imageView = new ImageView(imageOfTarget);
                        }
                        else if (field.isKey()) {
                            imageView = new ImageView(imageOfKey);
                        }
                        else {
                            imageView = new ImageView(imageOfPath);
                        }
                    }
                    else {
                        CommonMazeObject obj = field.get();
                        if (obj.isPacman()) {
                            pacman = obj;
                            if (direction == null){
                                
                                imageView = new ImageView(imageOfPacmanRight);
                            } else {
                                switch (direction) {
                                    case D:
                                        System.out.println("dir is D");
                                        imageView = new ImageView(imageOfPacmanDown);
                                        break;
                                    case L:
                                        System.out.println("dir is L");
                                        imageView = new ImageView(imageOfPacmanLeft);
                                        break;
                                    case U:
                                        System.out.println("dir is U");
                                        imageView = new ImageView(imageOfPacmanUp);
                                        break;
                                    default:
                                        System.out.println("dir is R");
                                        imageView = new ImageView(imageOfPacmanRight);  
                                        break;
                                }
                            }
                        } else {
                            imageView = new ImageView(imageOfGhost);
                        }
                    }
                }
                else {
                    imageView = new ImageView(imageOfWall);
                }
                hbox.getChildren().add(imageView);
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
                try {
                    tick(stage);
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }));
        eventLoop.setCycleCount(Timeline.INDEFINITE);
        eventLoop.play();
    }

    public void tick(Stage stage) throws FileNotFoundException {
        if (direction != null) {
            pacman.move(direction);
        }

        if (pacman.getWin()) {
            win(stage);
            return;
        }

        if (pacman.getLives() == 0) {
            died(stage);
            return;
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

    public void died(Stage stage) {
        Label endLabel = new Label("You died. Game over.");
        endLabel.setAlignment(Pos.CENTER);
        Scene newScene = new Scene(endLabel);
        stage.setScene(newScene);
    }

    public void win(Stage stage) {
        Label endLabel = new Label("You won!");
        endLabel.setAlignment(Pos.CENTER);
        Scene newScene = new Scene(endLabel);
        stage.setScene(newScene);
    }

    public static void main(String[] args) {
        launch();
    }
}