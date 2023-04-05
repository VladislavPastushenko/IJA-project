package ija.ija2022.homework2.tool.game;

import ija.ija2022.homework2.tool.common.CommonField;
import ija.ija2022.homework2.tool.common.CommonMazeObject;
import ija.ija2022.homework2.tool.common.CommonMaze;
import ija.ija2022.homework2.tool.common.AbstractObservableField;

import ija.ija2022.homework2.tool.game.PacmanObject;


public class PathField extends AbstractObservableField implements CommonField {
    int fieldRow;
    int fieldCol;

    CommonMazeObject fieldObject;

    CommonMaze currentMaze;

    public PathField (int row, int col) {
        fieldRow = row;
        fieldCol = col;

        fieldObject = null;
    }

    public boolean canMove() {
        return true;
    }

    public boolean isEmpty() {
        return fieldObject == null;
    }

    public boolean equals(Object obj) {
        if (this == obj){ // Test for identity
            return true;
        }
        if (!(obj instanceof PathField)){ // Test before casting
            return false;
        }
        PathField my_path_obj = (PathField) obj; // Casting
        return (my_path_obj.fieldRow == (this.fieldRow) && my_path_obj.fieldCol == (this.fieldCol));
    }

    public CommonMazeObject get() {
        return fieldObject;
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
        if (!this.isEmpty()) {
            return false;
        }
        this.notifyObservers();
        this.fieldObject = object;
        return true;
    }

    public boolean remove(CommonMazeObject object) {
        if (this.isEmpty()) {
            return false;
        }
        this.notifyObservers();
        this.fieldObject = null;
        return true;
    }

	public boolean contains(CommonMazeObject obj) {
		return obj == this.fieldObject;
	}
}