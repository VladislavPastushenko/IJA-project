package tool;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Button;
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
    Timeline eventLoop;
    String log = "";

    @Override
    public void start(Stage stage) throws Exception {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setSpacing(10);

        Button btnNewGame = new Button("Start new game");
        btnNewGame.addEventHandler(ActionEvent.ACTION, e -> {
            newGame(stage);
        });

        Button btnPlayback = new Button("Playback");
        btnPlayback.addEventHandler(ActionEvent.ACTION, e -> {
            playbackStart(stage);
        });

        vbox.getChildren().addAll(btnNewGame, btnPlayback);

        Scene newScene = new Scene(vbox);

        stage.setTitle("IJA Pacman");
        stage.setWidth(600);
        stage.setHeight(600);
        stage.setScene(newScene);
        stage.show();
    }

    public void newGame(Stage stage) {
        MazeConfigure cfg = new MazeConfigure();
        cfg.readFromFile("data/maps/maze01.txt");
        cfg.stopReading();

        maze = cfg.createMaze();

        log = Integer.toString(maze.numRows() - 2) + " " + Integer.toString(maze.numCols() - 2 ) + "\r\n";

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

         eventLoop = new Timeline(
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
        log = log.concat(maze.getStringRepresentation());

        if (direction != null) {
            pacman.move(direction);
        }

        if (pacman.getWin()) {
            endGame(stage);
            return;
        }

        if (pacman.getLives() <= 0) {
            endGame(stage);
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


    public VBox buildMaze(CommonMaze maze) {
        try {
            VBox vbox = new VBox();
            vbox.setAlignment(Pos.CENTER);
            Image imageOfGhost = new Image(new FileInputStream("data/images/ghost.png"));
            Image imageOfWall = new Image(new FileInputStream("data/images/bedrock.png"));
            Image imageOfTarget = new Image(new FileInputStream("data/images/target.png"));
            Image imageOfKey = new Image(new FileInputStream("data/images/key.png"));
            Image imageOfPacmanUp = new Image(new FileInputStream("data/images/pacmanUp.png"));
            Image imageOfPacmanRight = new Image(new FileInputStream("data/images/pacmanRight.png"));
            Image imageOfPacmanLeft = new Image(new FileInputStream("data/images/pacmanLeft.png"));
            Image imageOfPacmanDown = new Image(new FileInputStream("data/images/pacmanDown.png"));
            Image imageOfPath = new Image(new FileInputStream("data/images/dirt.png"));
            
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
                                            imageView = new ImageView(imageOfPacmanDown);
                                            break;
                                        case L:
                                            imageView = new ImageView(imageOfPacmanLeft);
                                            break;
                                        case U:
                                            imageView = new ImageView(imageOfPacmanUp);
                                            break;
                                        default:
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
        catch (Exception e) {
            System.out.println("Images are not found");
            return new VBox();
        }
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

    public void endGame(Stage stage) {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);

        Label endLabel = new Label("Game over.");
        endLabel.setAlignment(Pos.CENTER);

        Button btnNewGame = new Button("Start new game");
        btnNewGame.addEventHandler(ActionEvent.ACTION, e -> {
            newGame(stage);
        });
        Button saveButton = new Button("Save log of game");
        saveButton.addEventHandler(ActionEvent.ACTION, e -> {
            saveLog(stage);
        });

        vbox.getChildren().addAll(endLabel, btnNewGame, saveButton);


        Scene newScene = new Scene(vbox);
        stage.setScene(newScene);
        eventLoop.stop();
    }

    public void saveLog(Stage stage) {
        try {
            FileWriter myWriter = new FileWriter("data/logs/log.txt");
            myWriter.write(log);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }

    public void playbackStart(Stage stage) {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setSpacing(10);

        Button btnReadStart = new Button("Read start");
        btnReadStart.addEventHandler(ActionEvent.ACTION, e -> {
            playback(stage, 0);
        });

        Button btnReadFinish = new Button("Read finish");
        btnReadFinish.addEventHandler(ActionEvent.ACTION, e -> {
            String log = readFile("data/logs/log.txt");
            System.out.println(getLogStepsNumber(log));
            playback(stage, getLogStepsNumber(log) - 1);
        });

        vbox.getChildren().addAll(btnReadStart, btnReadFinish);
        Scene newScene = new Scene(vbox);
        stage.setScene(newScene);
    }

    public void playback(Stage stage, int step) {
        String log = readFile("data/logs/log.txt");
        String mazeString = getMazePhaseFromLog(log, step);
        setTmpFile(mazeString);

        VBox vbox = new VBox();

        MazeConfigure cfg = new MazeConfigure();
        cfg.readFromFile("data/maps/tmp.txt");
        cfg.stopReading();
        maze = cfg.createMaze();
        VBox mazeVbox = buildMaze(maze);

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(10, 10, 10, 10));
        hbox.setSpacing(10);

        Button btnNextStep = new Button("Next step");
        btnNextStep.addEventHandler(ActionEvent.ACTION, e -> {
            playback(stage, step + 1);
        });

        Button btnPrevStep = new Button("Prev step");
        btnPrevStep.addEventHandler(ActionEvent.ACTION, e -> {
            playback(stage, step + -1);
        });

        hbox.getChildren().addAll(btnNextStep, btnPrevStep);

        vbox.getChildren().addAll(mazeVbox, hbox);
        Scene newScene = new Scene(vbox);
        stage.setScene(newScene);
    }

    public String getMazePhaseFromLog(String log, int number) {
        String[] splittedLog = log.split("\r\n");

        int rows = Integer.parseInt(splittedLog[0].split(" ")[0]);

        String result = splittedLog[0] + "\r\n";
        for (int i = 0; i < rows; i++) {
            result = result.concat(splittedLog[1 + i + number * rows] + "\r\n");
        }

        return result;
    }

    public int getLogStepsNumber(String log) {
        String[] splittedLog = log.split("\r\n");
        int rows = Integer.parseInt(splittedLog[0].split(" ")[0]);

        return (splittedLog.length - 1) / rows;
    }

    public String readFile(String filePath) {
        String result = "";
        try {
            result = Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public void setTmpFile(String string) {
        try {
            FileWriter myWriter = new FileWriter("data/maps/tmp.txt");
            myWriter.write(string);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }

    public static void main(String[] args) {
        launch();
    }

}