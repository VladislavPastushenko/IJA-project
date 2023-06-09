/**
 * Project name: IJA-project
 * File name: Main.java
 * Date: 06.05.2023
 * Last update: 06.05.2023
 * Authors: Kravchuk Marina(xkravc02) + Pastushenko Vladislav(xpastu04)
 * Description: Main class is the entry point of the game application.
 */
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;

import java.io.FileInputStream;

import tool.common.*;
import tool.common.CommonField.Direction;
import tool.game.*;

/**
 * The Main class is the entry point of the game application.
 */
public class Main extends Application {
    CommonMazeObject pacman;
    Direction direction = null;
    CommonMaze maze;
    Timeline eventLoop;
    Boolean playPauseEventLoop = false;
    String log = "";
    String details = "";

    //starting point (Home page)
    @Override
    public void start(Stage stage) throws Exception {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);

        Button btnNewGame = new Button("Start new game");
        btnNewGame.addEventHandler(ActionEvent.ACTION, e -> {
            selectGame(stage);
        });

        Button btnPlayback = new Button("Playback");
        btnPlayback.addEventHandler(ActionEvent.ACTION, e -> {
            playbackStart(stage);
        });

        vbox.getChildren().addAll(btnNewGame, btnPlayback);

        Scene newScene = new Scene(vbox);

        newScene.getStylesheets().add("style.css");

        stage.setTitle("IJA Pacman");

        stage.setScene(newScene);
        stage.setMinWidth(600);
        stage.setMinHeight(600);
        stage.show();
    }

    //Choosing a map
    public void selectGame(Stage stage) {
        VBox vbox = new VBox();
        vbox.setMinWidth(600);
        vbox.setMinHeight(600);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setAlignment(Pos.CENTER);

        Button firstMapButton = new Button("Choose 1 map");
        firstMapButton.addEventHandler(ActionEvent.ACTION, e -> {
            newGame(stage, "data/maps/mapa01.txt");
        });

        Button secondMapButton = new Button("Choose 2 map");
        secondMapButton.addEventHandler(ActionEvent.ACTION, e -> {
            newGame(stage, "data/maps/mapa02.txt");
        });

        Button thirdMapButton = new Button("Choose 3 map");
        thirdMapButton.addEventHandler(ActionEvent.ACTION, e -> {
            newGame(stage, "data/maps/mapa03.txt");
        });

        vbox.getChildren().addAll(firstMapButton, secondMapButton, thirdMapButton);

        Scene newScene = new Scene(vbox);

        newScene.getStylesheets().add("style.css");
        stage.setScene(newScene);
    }


    public void newGame(Stage stage, String path) {
        MazeConfigure cfg = new MazeConfigure();
        cfg.readFromFile(path);
        cfg.stopReading();

        maze = cfg.createMaze();

        log = Integer.toString(maze.numRows() - 2) + " " + Integer.toString(maze.numCols() - 2 ) + "\r\n";

        VBox vbox = buildMaze(maze);
        Label detailsLabel = new Label(details);
        detailsLabel.setPadding(new Insets(10, 0, 0, 0));
        detailsLabel.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(detailsLabel);

        Scene primaryScene = new Scene(vbox);
        primaryScene.getStylesheets().add("style.css");
        stage.setScene(primaryScene);

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
                 new KeyFrame(Duration.seconds(0.3),
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
            endGame(stage, "You won!");
            return;
        }

        if (pacman.getLives() <= 0) {
            endGame(stage, "You died. Game over.");
            return;
        }

        maze.randomGhostsMovement();

        VBox vbox = buildMaze(maze);
        Label detailsLabel = new Label(details);
        detailsLabel.setFont(new Font("Arial", 20));
        detailsLabel.setPadding(new Insets(10, 0, 0, 0));
        detailsLabel.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(detailsLabel);
        Scene newScene = new Scene(vbox);
        newScene.getStylesheets().add("style.css");
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
                                imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                                    details = "It is our exit!";
                                });
                            }
                            else if (field.isKey()) {
                                imageView = new ImageView(imageOfKey);
                                imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                                    details = "Collect all keys to open the exit!";
                                });
                            }
                            else {
                                imageView = new ImageView(imageOfPath);
                                imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                                    details = "Just path";
                                });
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
                                imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                                    details = "Pacman";
                                });
                            } else {
                                imageView = new ImageView(imageOfGhost);
                                imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                                    details = "It's a ghost! Be careful!";
                                });
                            }
                        }
                    }
                    else {
                        imageView = new ImageView(imageOfWall);
                        imageView.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
                            details = "It's just a wall";
                        });
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

    public void endGame(Stage stage, String text) {
        VBox vbox = new VBox();
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);

        Label endLabel = new Label(text);
        endLabel.setAlignment(Pos.CENTER);

        Button btnNewGame = new Button("Start new game");
        btnNewGame.addEventHandler(ActionEvent.ACTION, e -> {
            selectGame(stage);
        });
        Button saveButton = new Button("Save log of game");
        saveButton.addEventHandler(ActionEvent.ACTION, e -> {
            saveLog(stage);
        });
        Button btnPlayback = new Button("Playback");
        btnPlayback.addEventHandler(ActionEvent.ACTION, e -> {
            playbackStart(stage);
        });

        vbox.getChildren().addAll(endLabel, btnNewGame, saveButton, btnPlayback);

        Scene newScene = new Scene(vbox);
        newScene.getStylesheets().add("style.css");
        stage.setScene(newScene);
        eventLoop.stop();
    }

    public void saveLog(Stage stage) {
        try {
            FileWriter myWriter = new FileWriter("data/logs/log.txt");
            myWriter.write(log);
            myWriter.close();
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }

    public void playbackStart(Stage stage) {
        VBox vbox = new VBox();
        vbox.setMinWidth(600);
        vbox.setMinHeight(600);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);

        Label header = new Label("Choose the option");

        Button btnReadStart = new Button("Read from start");
        btnReadStart.addEventHandler(ActionEvent.ACTION, e -> {
            playback(stage, 0);
        });

        Button btnReadFinish = new Button("Read from finish");
        btnReadFinish.addEventHandler(ActionEvent.ACTION, e -> {
            String log = readFile("data/logs/log.txt");
            playback(stage, getLogStepsNumber(log) - 1);
        });

        vbox.getChildren().addAll(header, btnReadStart, btnReadFinish);
        Scene newScene = new Scene(vbox);
        newScene.getStylesheets().add("style.css");
        stage.setScene(newScene);
    }

    public void playback(Stage stage, int step) {
        String log = readFile("data/logs/log.txt");
        String mazeString = getMazePhaseFromLog(log, step);
        setTmpFile(mazeString);

        VBox vbox = new VBox();
        vbox.setPadding(new Insets(0, 0, 10, 0));

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
        int stepsNumber = getLogStepsNumber(log);
        if (stepsNumber == step + 1) {
            btnNextStep.setDisable(true);
        }

        Button btnPrevStep = new Button("Prev step");
        btnPrevStep.addEventHandler(ActionEvent.ACTION, e -> {
            playback(stage, step - 1);
        });
        if (step == 0) {
            btnPrevStep.setDisable(true);
        }

        Button playAndPause = new Button("Play/pause");
        playAndPause.addEventHandler(ActionEvent.ACTION, e -> {
            playPauseEventLoop = !playPauseEventLoop;
            if (playPauseEventLoop) {
                playback(stage, step + 1);
                return;
            }
        });
        if (stepsNumber == step + 1) {
            playPauseEventLoop = false;
            playAndPause.setDisable(true);
        }

        Button exitButton = new Button("Exit");
        exitButton.addEventHandler(ActionEvent.ACTION, e -> {
            playPauseEventLoop = false;
            try {
                start(stage);
            } finally {
                return;
            }
        });
        if (playPauseEventLoop) {
            exitButton.setDisable(true);
        }


        hbox.getChildren().addAll(btnPrevStep, playAndPause, btnNextStep);
        hbox.setAlignment(Pos.CENTER);

        vbox.getChildren().addAll(mazeVbox, hbox, exitButton);
        vbox.setAlignment(Pos.CENTER);
        Scene newScene = new Scene(vbox);
        newScene.getStylesheets().add("style.css");
        stage.setScene(newScene);

        if (playPauseEventLoop) {
            delay(500, () -> playback(stage, step + 1));
        }
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
          } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
          }
    }

    public static void main(String[] args) {
        launch();
    }

}