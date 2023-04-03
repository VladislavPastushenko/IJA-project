package ija.ija2022.homework2.tool.game;

import ija.ija2022.homework2.tool.common.CommonMazeObject;
import ija.ija2022.homework2.tool.common.CommonField;

import java.util.Objects;

public class PacmanObject implements CommonMazeObject {
    CommonField myField;
    int lives;

    public PacmanObject(CommonField CommonField) {
        myField = CommonField;
        lives = 3;
    }

    public boolean canMove(CommonField.Direction dir) {
        return myField.nextField(dir).canMove();
    }
    public boolean move(CommonField.Direction dir)  {
        if (!this.canMove(dir)) {
            return false;
        }

        if (Objects.nonNull(myField.nextField(dir).get())) {
            this.damage();
        }

        PacmanObject tmp = this;
        myField.remove(this);
        myField.nextField(dir).put(tmp);

        myField = myField.nextField(dir);
        return true;
    }

    public String toString() {
        return "";
    }

    public boolean isPacman() {
        return true;
    }

    public CommonField getField() {
        return myField;
    }

    public int getLives() {
        return lives;
    }

    public int damage() {
        return --lives;
    }
}