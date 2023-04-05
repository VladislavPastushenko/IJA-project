package ija.ija2022.homework2.tool.game;

import ija.ija2022.homework2.tool.common.CommonField;
import ija.ija2022.homework2.tool.common.CommonMazeObject;
import ija.ija2022.homework2.tool.common.CommonMaze;
import ija.ija2022.homework2.tool.common.AbstractObservableField;

import ija.ija2022.homework2.tool.game.PacmanObject;

public class WallField extends AbstractObservableField implements CommonField {
    int fieldRow;
    int fieldCol;

    CommonMaze currentMaze;

    public WallField (int row, int col) {
        fieldRow = row;
        fieldCol = col;
    }

    // done
    public boolean canMove() {
        return false;
    }

    // done
    public boolean isEmpty() {
        return true;
    }

    public boolean equals(Object obj) {
        if (this == obj){ // Test for identity
            return true;
        }
        if (!(obj instanceof WallField)){ // Test before casting
            return false;
        }
        WallField my_wall_obj = (WallField) obj; // Casting
        return (my_wall_obj.fieldRow == (this.fieldRow) && my_wall_obj.fieldCol == (this.fieldCol));
    }

    public CommonMazeObject get() {
        return null;
    }

    public CommonMaze getMaze() {
        return currentMaze;
    }
    public boolean setMaze(CommonMaze CommonMaze) {
        currentMaze = CommonMaze;
        return true;
    }

    public CommonField nextField(CommonField.Direction dirs) {
        switch (dirs) {
            case D:
                return currentMaze.getField(fieldRow + 1, fieldCol);
            case L:
                return currentMaze.getField(fieldRow, fieldCol - 1);
            case R:
                return currentMaze.getField(fieldRow, fieldCol + 1);
            case U:
                return currentMaze.getField(fieldRow - 1, fieldCol);
        }
        return this;
    }

    public boolean put(CommonMazeObject object) {
        throw new UnsupportedOperationException("");
    }
    public boolean remove(CommonMazeObject object) {
        return true;
    }

	public boolean contains(CommonMazeObject obj) {
		return false;
	}
}