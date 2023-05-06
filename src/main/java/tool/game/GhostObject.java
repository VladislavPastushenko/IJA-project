package tool.game;

import java.util.Objects;

import tool.common.CommonField;
import tool.common.CommonMazeObject;

public class GhostObject implements CommonMazeObject {
	CommonField myField;

    public GhostObject(CommonField CommonField) {
        myField = CommonField;
    }

    public boolean canMove(CommonField.Direction dir) {
        CommonMazeObject obj = myField.nextField(dir).get();
        if (obj != null && !obj.isPacman()) {
            return false;
        }
        return myField.nextField(dir).canMove();
    }
    public boolean move(CommonField.Direction dir)  {
        if (!this.canMove(dir)) {
            return false;
        }

        CommonMazeObject pacman = myField.nextField(dir).get();
        if (Objects.nonNull(pacman)) {
            if (pacman.isPacman()) {
                pacman.damage();
            }
        }

        GhostObject tmp = this;

        this.myField.nextField(dir).put(tmp);
        this.myField.remove(tmp);
        this.myField = this.myField.nextField(dir);

        return true;
    }

    public String toString() {
        return "";
    }

    public boolean isPacman() {
        return false;
    }

    public CommonField getField() {
        return myField;
    }

    public int getLives() {
        return 0;
    }

    public int damage() {
        return 0;
    }

    public boolean getWin() {
        return false;
    };
}