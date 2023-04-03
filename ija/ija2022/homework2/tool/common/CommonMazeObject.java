package ija.ija2022.homework2.tool.common;

import ija.ija2022.homework2.tool.common.CommonField;
import ija.ija2022.homework2.tool.game.MazeConfigure;

public interface CommonMazeObject {
    public boolean canMove(CommonField.Direction dir);

    public boolean move(CommonField.Direction dir);

    public boolean isPacman();

    public CommonField getField();

    public int getLives();

    public int damage();
}