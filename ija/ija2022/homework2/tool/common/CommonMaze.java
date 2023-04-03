package ija.ija2022.homework2.tool.common;

import ija.ija2022.homework2.tool.common.CommonField;

import java.util.List;

public interface CommonMaze {

    CommonField getField(int row, int col);
    int numCols();
    int numRows();

    CommonMaze create(int rows, int cols, CommonField[][] board);

    public List<CommonMazeObject> ghosts();
}