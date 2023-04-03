package ija.ija2022.homework2.tool.game;

import ija.ija2022.homework2.tool.common.CommonMaze;
import ija.ija2022.homework2.tool.common.CommonField;
import ija.ija2022.homework2.tool.common.CommonMazeObject;

import ija.ija2022.homework2.tool.game.PathField;
import ija.ija2022.homework2.tool.game.WallField;

import java.util.ArrayList;
import java.util.List;

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
}