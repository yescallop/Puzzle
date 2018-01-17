package cn.yescallop.puzzle;

import java.util.*;

public class BFSPuzzleSolver extends PuzzleSolver {

    private Queue<PuzzleStatus> openQueue = new LinkedList<>();
    private Set<PuzzleStatus> openSet = new HashSet<>();
    private Set<PuzzleStatus> closedSet = new HashSet<>();

    public BFSPuzzleSolver(PuzzleStatus cur) {
        super(cur.size);
        openQueue.add(cur);
        openSet.add(cur);
    }

    @Override
    public Route solveTo(PuzzleStatus end) {
        int[][] solvedMatrix = end.matrix;
        end.calculateHash();
        long solvedHash = end.hash;
        PuzzleStatus cur = null;
        solve:
        while (openQueue.size() > 0) {
            cur = openQueue.remove();
            openSet.remove(cur);
            if (!closedSet.add(cur)) continue;
            for (int i = 0; i < 4; i++) {
                PuzzleStatus t = cur.cloneWithMove(i);
                if (t != null) {
                    t.calculateHash();
                    if (t.hash == solvedHash && Arrays.deepEquals(t.matrix, solvedMatrix)) {
                        cur = t;
                        break solve;
                    }
                    if (!closedSet.contains(t) && !openSet.contains(t)) {
                        openQueue.add(t);
                        openSet.add(t);
                    }
                }
            }
        }
        this.searchedCount = closedSet.size();
        this.openQueue = null;
        this.openSet = null;
        this.closedSet = null;
        return Route.fromTail(cur);
    }
}