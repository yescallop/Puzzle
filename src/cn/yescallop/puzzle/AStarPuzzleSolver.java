package cn.yescallop.puzzle;

import java.awt.*;
import java.util.*;
import java.util.Queue;

public class AStarPuzzleSolver extends PuzzleSolver {

    private Queue<PuzzleStatus> openQueue = new PriorityQueue<>(Comparator.comparingInt(a -> a.f));
    private Set<PuzzleStatus> openSet = new HashSet<>();
    private Set<PuzzleStatus> closedSet = new HashSet<>();

    public AStarPuzzleSolver(PuzzleStatus cur) {
        super(cur.size);
        openQueue.add(cur);
        openSet.add(cur);
    }

    @Override
    public Route solveTo(PuzzleStatus end) {
        end.calculateHash();
        Point[] index = end.createIndex();
        PuzzleStatus cur = null;
        solve:
        while (openQueue.size() > 0) {
            cur = openQueue.remove();
            openSet.remove(cur);
            if (!closedSet.add(cur)) continue;
            for (int i = 0; i < 4; i++) {
                PuzzleStatus t = cur.cloneWithMove(i);
                if (t != null) {
                    t.estimateCost(index);
                    if (t.h == 0) {
                        cur = t;
                        break solve;
                    }
                    t.calculateHash();
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