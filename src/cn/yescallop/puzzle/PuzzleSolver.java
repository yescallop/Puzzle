package cn.yescallop.puzzle;

import static cn.yescallop.puzzle.Record.*;

public class PuzzleSolver {

    private final Puzzle puzzle;
    private final Record record;
    private final boolean recording;
    private final long[] solvedIndex;

    public PuzzleSolver(Puzzle puzzle, Record record) {
        this.puzzle = puzzle;
        this.record = record;
        this.recording = record != null;
        this.solvedIndex = new long[puzzle.size * puzzle.size];
        calculateSolvedIndexes();
    }


    private void calculateSolvedIndexes() {
        int y = 0;
        int x = 0;
        long index = (long) y << 32;
        for (int i = 1; i < puzzle.size * puzzle.size; i++) {
            solvedIndex[i] = index | x;
            if (++x == puzzle.size && y != puzzle.size - 1) {
                y++;
                index = (long) y << 32;
                x = 0;
            }
        }
        solvedIndex[0] = (long) (puzzle.size - 1) | (puzzle.size - 1);
    }

    /* --- Move Move Move --- */

    private void moveRight() {
        puzzle.moveRight();
        if (recording) {
            record.put(RIGHT);
        }
    }

    private void moveLeft() {
        puzzle.moveLeft();
        if (recording) {
            record.put(LEFT);
        }
    }

    private void moveDown() {
        puzzle.moveDown();
        if (recording) {
            record.put(DOWN);
        }
    }

    private void moveUp() {
        puzzle.moveUp();
        if (recording) {
            record.put(UP);
        }
    }

    /* --- Push Push Push --- */

    private void pushLeft() {
        moveRight();
        moveDown();
        moveLeft();
        moveUp();
        moveRight();
    }

    private void pushRight() {
        moveLeft();
        moveDown();
        moveRight();
        moveUp();
        moveLeft();
    }

    private void pushUp() {
        if ((int) puzzle.index[0] == puzzle.size - 1) {
            moveRight();
            moveDown();
            moveDown();
            moveLeft();
            moveUp();
        } else {
            moveLeft();
            moveDown();
            moveDown();
            moveRight();
            moveUp();
        }
    }

    private void pushDown() {
        if ((int) puzzle.index[0] == puzzle.size - 1) {
            moveDown();
            moveRight();
            moveUp();
            moveUp();
            moveLeft();
        } else {
            moveDown();
            moveLeft();
            moveUp();
            moveUp();
            moveRight();
        }
    }

    /* --- Solve Solve Solve --- */

    public void solve() {
        for (int n = 0; n < puzzle.size - 3; n++) {
            solveRow(n);
            solveColumn(n);
        }
        solveRow(puzzle.size - 3);
        //solveLast();
    }

    private void solveLast() {
        long firstIndex = puzzle.index[puzzle.size * (puzzle.size - 1) - 2];
        int firstX = (int) firstIndex - puzzle.size + 3;
        int firstY = (int) (firstIndex >> 32) - puzzle.size + 2;
        int spaceX = (int) puzzle.index[0] - puzzle.size + 3;
        int spaceY = (int) (puzzle.index[0] >> 32) - puzzle.size + 2;
        //Make
        //    1,2,3
        //    4,5,0
        //Firstly, make
        //    1,0,X
        //    X,X,X
        if (spaceX == 0 && spaceY == 0) {
            //0,X,X
            //X,X,X
            if (firstX == 0) {
                //0,X,X
                //1,X,X
                moveUp();
                moveLeft();
                moveDown();
            } else if (firstX == 1) {
                if (firstY == 0) {
                    //0,1,X
                    //X,X,X
                    moveLeft();
                } else {
                    //0,X,X
                    //X,1,X
                    moveLeft();
                    moveUp();
                    moveRight();
                    moveDown();
                    moveLeft();
                }
            } else {
                if (firstY == 0) {
                    //0,X,1
                    //X,X,X
                    moveLeft();
                    moveLeft();
                    moveUp();
                    moveRight();
                    moveRight();
                    moveDown();

                    moveLeft();
                } else {
                    //0,X,X
                    //X,X,1
                    moveLeft();
                    moveLeft();
                    moveUp();
                    moveRight();
                    moveRight();
                    moveDown();

                    moveLeft();
                    moveLeft();
                    moveUp();
                    moveRight();
                    moveRight();
                    moveDown();

                    moveLeft();
                }
            }
        } else {
            //X,0,X
            //X,X,X
            if (firstX == 0 && firstY == 1) {
                //X,0,X
                //1,X,X
                moveRight();
                moveUp();
                moveLeft();
                moveDown();
            } else if (firstX == 1) {
                //X,0,X
                //X,1,X
                moveUp();
                moveRight();
                moveDown();
                moveLeft();
            } else if (firstX != 0 || firstY != 0) {
                if (firstY == 0) {
                    //X,0,1
                    //X,X,X
                    moveLeft();
                    moveUp();
                    moveRight();
                    moveRight();
                    moveDown();

                    moveLeft();
                } else {
                    //X,0,X
                    //X,X,1
                    moveLeft();
                    moveUp();
                    moveRight();
                    moveRight();
                    moveDown();

                    moveLeft();
                    moveLeft();
                    moveUp();
                    moveRight();
                    moveRight();
                    moveDown();

                    moveLeft();
                }
            }
        }
        //TODO
    }

    /* --- Row Part --- */

    private void solveRow(int n) {
        for (int i = n * (puzzle.size + 1) + 1; i < (n + 1) * puzzle.size; i++) {
            solveInRow(i);
        }
        solveLastInRow((n + 1) * puzzle.size);
    }

    private void solveInRow(int n) {
        if (solved(n))
            return;
        locateTo(n);
        int x = (int) puzzle.index[n];
        int y = (int) (puzzle.index[n] >> 32);
        int toX = (int) solvedIndex[n];
        int toY = (int) (solvedIndex[n] >> 32);

        if (x > toX) {
            for (int k = toX; k < x; k++) {
                pushLeft();
            }
        } else if (x < toX) {
            for (int k = x; k < toX; k++) {
                pushRight();
            }
        }

        if (y != toY) {
            for (int k = toY; k < y; k++) {
                pushUp();
            }
        }
    }

    private void solveLastInRow(int n) {
        if (solved(n))
            return;
        locateTo(n);
        int x = (int) puzzle.index[n];
        int y = (int) (puzzle.index[n] >> 32);
        int toX = puzzle.size - 1;
        int toY = (int) (solvedIndex[n] >> 32);

        if (x != toX) {
            for (int k = x; k < toX; k++) {
                pushRight();
            }
        }

        for (int k = toY + 1; k < y; k++) {
            pushUp();
        }
        moveRight();
        moveRight();
        moveDown();
        moveDown();
        moveLeft();
        moveLeft();
        moveUp();
        moveRight();
        moveDown();
        moveRight();
        moveUp();
    }

    /* --- Column Part --- */

    private void solveColumn(int n) {
        for (int i = n + 1; i < puzzle.size - 1; i++) {
            solveInColumn(i * puzzle.size + n + 1);
        }
        solveLastInColumn((puzzle.size - 1) * puzzle.size + n + 1);
    }

    private void solveInColumn(int n) {
        if (solved(n))
            return;
        locateTo(n);
        int x = (int) puzzle.index[n];
        int y = (int) (puzzle.index[n] >> 32);
        int toX = (int) solvedIndex[n];
        int toY = (int) (solvedIndex[n] >> 32);

        if (y > toY) {
            for (int k = toY; k < y; k++) {
                pushUp();
            }
        } else if (y < toY) {
            for (int k = y; k < toY; k++) {
                pushDown();
            }
        }

        if (x != toX) {
            for (int k = toX; k < x; k++) {
                pushLeft();
            }
        }
    }

    private void solveLastInColumn(int n) {
        if (solved(n))
            return;
        int x = (int) puzzle.index[n];
        int y = (int) (puzzle.index[n] >> 32);
        int toX = (int) solvedIndex[n];
        int toY = puzzle.size - 1;

        if (y == toY && toX + 1 == x) {
            moveLeft();
            return;
        }

        locateTo(n);

        for (int k = y + 1; k < toY; k++) {
            pushDown();
        }

        for (int k = toX + 1; k < x; k++) {
            pushLeft();
        }

        moveDown();
        moveDown();
        moveRight();
        moveUp();
        moveUp();
        moveLeft();
        moveDown();
        moveRight();
        moveDown();
        moveLeft();
    }

    public boolean solved(int n) {
        return solvedIndex[n] == puzzle.index[n];
    }

    private void locateTo(int n) {
        int x = (int) puzzle.index[n];
        int y = (int) (puzzle.index[n] >> 32);
        int spaceX = (int) puzzle.index[0];
        int spaceY = (int) (puzzle.index[0] >> 32);

        if (spaceX == x) {
            if (y + 1 == spaceY)
                return;

            if (spaceY > y) {
                for (int k = y + 1; k < spaceY; k++) {
                    moveDown();
                }
            } else {
                for (int k = spaceY; k < y; k++) {
                    moveUp();
                }
            }
        } else if (spaceY == y) {
            if (spaceX > x) {
                for (int k = x + 1; k < spaceX; k++) {
                    moveRight();
                }
            } else {
                for (int k = spaceX + 1; k < x; k++) {
                    moveLeft();
                }
            }
            if (y == puzzle.size - 1) {
                moveDown();
                if (spaceX > x) {
                    moveRight();
                } else {
                    moveLeft();
                }
                moveUp();
            } else {
                moveUp();
                if (spaceX > x) {
                    moveRight();
                } else {
                    moveLeft();
                }
            }
        } else {
            if (spaceY > y) {
                if (spaceX > x) {
                    for (int k = x; k < spaceX; k++) {
                        moveRight();
                    }
                } else if (spaceX < x) {
                    for (int k = spaceX; k < x; k++) {
                        moveLeft();
                    }
                }
                for (int k = y + 1; k < spaceY; k++) {
                    moveDown();
                }
            } else if (spaceY < y) {
                for (int k = spaceY; k < y; k++) {
                    moveUp();
                }

                if (spaceX > x) {
                    for (int k = x + 1; k < spaceX; k++) {
                        moveRight();
                    }
                } else if (spaceX < x) {
                    for (int k = spaceX + 1; k < x; k++) {
                        moveLeft();
                    }
                }
                if (y == puzzle.size - 1) {
                    moveDown();
                    if (spaceX > x) {
                        moveRight();
                    } else {
                        moveLeft();
                    }
                    moveUp();
                } else {
                    moveUp();
                    if (spaceX > x) {
                        moveRight();
                    } else {
                        moveLeft();
                    }
                }
            }
        }
    }
}
