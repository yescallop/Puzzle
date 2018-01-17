package cn.yescallop.puzzle;

public class Main {

    public static void main(String[] args) {
        PuzzleStatus p = PuzzleStatus.of(3, "7,2,8\n" +
                "4,5,6\n" +
                "1,3,0");
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
