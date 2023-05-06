package tool.common;

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
    boolean contains(CommonMazeObject obj);

    public CommonMaze getMaze();

    public boolean isTarget();
    public boolean isKey();
    public void setIsKey(boolean value);
}