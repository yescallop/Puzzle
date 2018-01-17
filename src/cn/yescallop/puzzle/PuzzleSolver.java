package cn.yescallop.puzzle;

public abstract class PuzzleSolver {

    protected final int size;
    protected int searchedCount = 0;

    public PuzzleSolver(int size) {
        this.size = size;
    }

    public abstract Route solveTo(PuzzleStatus end);

    public Route solve() {
        return this.solveTo(PuzzleStatus.init(size));
    }

    public int searchedCount() {
        return searchedCount;
    }
}
