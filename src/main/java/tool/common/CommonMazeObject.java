package tool.common;
import java.util.List;

public interface CommonMazeObject {
    public boolean canMove(CommonField.Direction dir);

    public boolean move(CommonField.Direction dir);

    public boolean isPacman();

    public CommonField getField();

    public int getLives();

    public int damage();

    public boolean getWin();

}