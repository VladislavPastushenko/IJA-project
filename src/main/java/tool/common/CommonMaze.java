package tool.common;

import java.util.List;

public interface CommonMaze {

    CommonField getField(int row, int col);
    int numCols();
    int numRows();

    CommonMaze create(int rows, int cols, CommonField[][] board);

    public List<CommonMazeObject> ghosts();

    public void randomGhostsMovement();

    public boolean keys();

    public String getStringRepresentation();
}