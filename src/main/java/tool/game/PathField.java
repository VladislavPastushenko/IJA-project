/**
 * Project name: IJA-project
 * File name: PathField.java
 * Authors: Kravchuk Marina(xkravc02)
 * Description: Class for the paths in the maze.
 */
package tool.game;

import tool.common.AbstractObservableField;
import tool.common.CommonField;
import tool.common.CommonMaze;
import tool.common.CommonMazeObject;

public class PathField extends AbstractObservableField implements CommonField {
    int fieldRow;
    int fieldCol;
    boolean isTarget;
    boolean isKey;

    CommonMazeObject ghostObject;
    CommonMazeObject pacmanObject;

    CommonMaze currentMaze;

    public PathField (int row, int col) {
        fieldRow = row;
        fieldCol = col;

        ghostObject = null;
        pacmanObject = null;
        isTarget = false;
    }

    public boolean canMove() {
        return true;
    }

    public boolean isEmpty() {
        return (ghostObject == null) && (pacmanObject == null);
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
        if (pacmanObject != null){
            return pacmanObject;
        }
        return ghostObject;
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
        if (object instanceof GhostObject){
            this.ghostObject = object;
        } else {
            this.pacmanObject = object;
        }
        this.notifyObservers();
        return true;
    }

    public boolean remove(CommonMazeObject object) {
        if (this.isEmpty()) {
            return false;
        }
        if (object instanceof GhostObject){
            this.ghostObject = null;
        } else {
            this.pacmanObject = null;
        }
        this.notifyObservers();
        return true;
    }

	public boolean contains(CommonMazeObject obj) {
		return obj == this.ghostObject;
	}

    public void setIsTarget() {
        isTarget = true;
    }

    public boolean isTarget() {
        return isTarget;
    }

    public void setIsKey(boolean value) {
        isKey = value;
    }

    public boolean isKey() {
        return isKey;
    }
}