package tool.game;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import tool.common.CommonField;
import tool.common.CommonMaze;

public class MazeConfigure {
    public static enum State {
        CREATED,
        ERROR,
        FINISHED,
        PROCESSING
    }

    State state;

    int mazeCols;
    int mazeRows;

    int processedLinePointer;

    CommonField mazeBoard[][];


    public MazeConfigure() {
        this.state = State.CREATED;
    }

    public boolean readFromFile(String path) {
        int row = 0;
        int col = 0;
        int tmprow = 0;
        int tmpcol = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (tmprow == 0){
                    String[] substr;
                    substr = line.split(" ");
                    tmprow = Integer.parseInt(substr[0]); 
                    tmpcol = Integer.parseInt(substr[1]);
                } else {
                    if (line.length() != tmpcol){
                        this.state = State.ERROR;
                        return false;
                    }
                    row++;
                }
                
            }
            System.out.println(tmprow);
            System.out.print(tmpcol);
        } catch (IOException e) {
            System.err.println("error" + e.getMessage());
        }
        if (tmprow != row){
            return false;
        }
        this.startReading(tmprow, tmpcol);
        this.processLine(path);
        return true;
    }

    public void startReading(int rows, int cols) {
        this.state = State.PROCESSING;

        mazeRows = rows;
        mazeCols = cols;

        processedLinePointer = 0;

        mazeBoard = new CommonField[mazeRows][mazeCols];
    }

    public boolean processLine(String path) {
        
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                for (int i = 0; i < line.length(); i++) {
                    switch (line.charAt(i)) {
                        case 'X':
                            mazeBoard[processedLinePointer][i] = new WallField(processedLinePointer + 1, i + 1);
                            break;
                        case '.':
                            mazeBoard[processedLinePointer][i] = new PathField(processedLinePointer + 1, i + 1);
                            break;
                        case 'T':
                            mazeBoard[processedLinePointer][i] = new PathField(processedLinePointer + 1, i + 1);
                            break;
                        case 'G':
                            PathField commonFieldGhost = new PathField(processedLinePointer + 1, i + 1);
                            GhostObject ghost = new GhostObject(commonFieldGhost);
                            commonFieldGhost.put(ghost);
                            mazeBoard[processedLinePointer][i] = commonFieldGhost;
                            break;
                        case 'K':
                            mazeBoard[processedLinePointer][i] = new PathField(processedLinePointer + 1, i + 1);
                            break;
                        case 'S':
                            PathField commonFieldPacman = new PathField(processedLinePointer + 1, i + 1);
                            PacmanObject pacman = new PacmanObject(commonFieldPacman);
                            commonFieldPacman.put(pacman);
                            mazeBoard[processedLinePointer][i] = commonFieldPacman;
                            break;
                        default:
                            this.state = State.ERROR;
                            return false;
                    }
        
                }
                processedLinePointer += 1;
            }
        } catch (IOException e) {
            System.err.println("error" + e.getMessage());
        }
    
        return true;
    }

    public boolean stopReading() {
        if (processedLinePointer != mazeRows) {
            this.state = State.ERROR;
            return false;
        }

        this.state = State.FINISHED;
        return true;
    }

    public CommonMaze createMaze() {
        if (this.state != State.FINISHED) {
            return null;
        }

        this.state = State.CREATED;

        CommonMaze pacmanMaze = new PacmanMaze(mazeRows, mazeCols);
        return pacmanMaze.create(mazeRows, mazeCols, mazeBoard);
    }
}