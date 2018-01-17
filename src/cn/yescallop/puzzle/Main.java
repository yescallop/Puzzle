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
        System.out.println("�ܹ�������" + solver.searchedCount() + "��״̬");
        System.out.println("��ʱ��" + (time / 1000d) + "��");
        System.out.println("��Ҫ�ƶ���" + route.length() + "��");
        System.out.println("·����" + route);
    }
}
