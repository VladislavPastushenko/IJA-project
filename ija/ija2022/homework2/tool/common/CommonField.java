package ija.ija2022.homework2.tool.common;

import ija.ija2022.homework2.tool.common.CommonMaze;
import ija.ija2022.homework2.tool.common.CommonMazeObject;

public interface CommonField extends Observable {
    public static enum Direction {
        D, L, R, U
    }

    boolean canMove();

    CommonMazeObject get();

    boolean isEmpty();

    CommonField nextField(CommonField.Direction dirs);

    boolean put(CommonMazeObject object);
    boolean remove(CommonMazeObject object);
    boolean setMaze(CommonMaze CommonMaze);

    public CommonMaze getMaze();
}