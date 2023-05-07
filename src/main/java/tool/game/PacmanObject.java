/**
 * Project name: IJA-project
 * File name: PacmanObject.java
 * Authors: Pastushenko Vladislav(xpastu04)
 * Description: Pacman class
 */
package tool.game;

import java.util.Objects;


import tool.common.CommonField;
import tool.common.CommonMazeObject;

public class PacmanObject implements CommonMazeObject {
    CommonField myField;
    int lives;
    boolean win;

    public PacmanObject(CommonField CommonField) {
        myField = CommonField;
        lives = 1;
        win = false;
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
            return true;
        }

        PacmanObject tmp = this;
        myField.remove(this);
        myField.nextField(dir).put(tmp);

        myField = myField.nextField(dir);

        if (myField.isKey()) {
            myField.setIsKey(false);
        }

        if (myField.isTarget()) {
            if (!myField.getMaze().keys()) {
                win = true;
            }
        }
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

    public boolean getWin() {
        return win;
    }
}