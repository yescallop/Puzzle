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
        System.out.println("�ܹ�������" + solver.searchedCount() + "��״̬");
        System.out.println("��ʱ��" + (time / 1000d) + "��");
        System.out.println("��Ҫ�ƶ���" + route.length() + "��");
        System.out.println("·����" + route);
    }
}
