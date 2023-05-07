package tool.game;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.skin.TextInputControlSkin.Direction;
import tool.common.CommonField;
import tool.common.CommonMaze;
import tool.common.CommonMazeObject;

import java.util.concurrent.ThreadLocalRandom;

public class PacmanMaze implements CommonMaze {
    int mazeRows;
    int mazeCols;
    CommonField mazeBoard[][];

    public PacmanMaze(int rows, int cols) {
        mazeRows = rows + 2;
        mazeCols = cols + 2;
        mazeBoard = new CommonField[mazeRows][mazeCols];
    }

    public CommonMaze create(int rows, int cols, CommonField[][] board) {
        for (int row = 0; row < mazeRows; row++) {
            for (int col = 0; col < mazeCols; col++) {
                if (row == 0 || col == 0 || row == mazeRows - 1 || col == mazeCols - 1) {
                    WallField wall = new WallField(row, col);
                    wall.setMaze(this);
                    mazeBoard[row][col] = wall;
                } else {
                    CommonField cell = board[row - 1][col - 1];
                    cell.setMaze(this);
                    mazeBoard[row][col] = cell;
                }
            }
        }

        return this;
    }

    public CommonField getField(int row, int col) {
        return mazeBoard[row][col];
    }
    public int numCols() {
        return mazeCols;
    }
    public int numRows() {
        return mazeRows;
    }

    public List<CommonMazeObject> ghosts() {
        List<CommonMazeObject> ghostsList = new ArrayList<>();

        for (int row = 0; row < mazeRows; row++) {
            for (int col = 0; col < mazeCols; col++) {
                if (row == 0 || col == 0 || row == mazeRows - 1 || col == mazeCols - 1) {
                    continue;
                }
                CommonField field = mazeBoard[row][col];
                CommonMazeObject mazeObject = field.get();

                if (mazeObject != null && !mazeObject.isPacman()) {
                    ghostsList.add(mazeObject);
                }
            }
        }

        return ghostsList;
    }

    public boolean keys() {

        for (int row = 0; row < mazeRows; row++) {
            for (int col = 0; col < mazeCols; col++) {
                if (row == 0 || col == 0 || row == mazeRows - 1 || col == mazeCols - 1) {
                    continue;
                }
                CommonField field = mazeBoard[row][col];
                if (field.isKey()) {
                    return true;
                }
            }
        }

        return false;
    }

    public void randomGhostsMovement() {
        List<CommonMazeObject> ghostsList = ghosts();
        for (int i = 0; i < ghostsList.size(); i++) {
            while (true) {
                int randomNum = ThreadLocalRandom.current().nextInt(0, 4);
                CommonField.Direction[] directions = {CommonField.Direction.U, CommonField.Direction.D, CommonField.Direction.L, CommonField.Direction.R};
                CommonField.Direction direction = directions[randomNum];
                if (ghostsList.get(i).canMove(direction)) {
                    ghostsList.get(i).move(direction);
                    break;
                }
            }
        }
    }

    public String getStringRepresentation() {
        String result = "";
        for (int row = 0; row < mazeRows; row++) {
            for (int col = 0; col < mazeCols; col++) {
                if (row == 0 || col == 0 || row == mazeRows - 1 || col == mazeCols - 1) {
                    continue;
                }
                CommonField field = mazeBoard[row][col];

                if (field.canMove()) {
                    if (field.isEmpty()) {
                        result = result.concat(".");
                    }
                    else {
                        CommonMazeObject mazeObject = field.get();
                        if (mazeObject.isPacman()) {
                            result = result.concat("S");
                        } else {
                            result = result.concat("G");
                        }
                    }
                }
                else {
                    result = result.concat("X");
                }
            }
            if (row == mazeRows - 1 || row == 0) {
                continue;
            }
            result = result.concat("\r\n");
        }
        return result;
    }
}