package ija.ija2022.homework2.tool.game;

import ija.ija2022.homework2.tool.common.CommonMazeObject;
import ija.ija2022.homework2.tool.common.CommonField;

import java.util.Objects;

public class GhostObject implements CommonMazeObject {
    CommonField myField;

    public GhostObject(CommonField CommonField) {
        myField = CommonField;
    }

    public boolean canMove(CommonField.Direction dir) {
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
        myField.remove(this);
        myField.nextField(dir).put(tmp);
        myField = myField.nextField(dir);
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
}