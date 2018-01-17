package cn.yescallop.puzzle;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        PuzzleStatus p = PuzzleStatus.generate(3, 500, new Random());
        System.out.println(p);
        PuzzleSolver solver = new BFSPuzzleSolver(p);
        long start = System.currentTimeMillis();
        Route route = solver.solve();
        long time = System.currentTimeMillis() - start;
        System.out.println("总共搜索：" + solver.searchedCount() + "个状态");
        System.out.println("用时：" + (time / 1000d) + "秒");
        System.out.println("需要移动：" + route.length() + "步");
        System.out.println("路径：" + route);
    }
}
