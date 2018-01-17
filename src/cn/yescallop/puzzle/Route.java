package cn.yescallop.puzzle;

import java.util.ArrayList;
import java.util.List;

public class Route {

    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;

    private final int[] route;

    private Route(int[] route) {
        this.route = route;
    }

    public static Route fromTail(PuzzleStatus p) {
        List<Integer> list = new ArrayList<>();
        while (p.move != -1) {
            list.add(p.move);
            p = p.parent;
        }
        int[] route = new int[list.size()];
        int j = 0;
        for (int i = list.size() - 1; i >= 0; i--) {
            route[j++] = list.get(i);
        }
        return new Route(route);
    }

    public int length() {
        return route.length;
    }

    public int[] array() {
        return route;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (int i : route) {
            switch (i) {
                case UP:
                    builder.append('U');
                    break;
                case DOWN:
                    builder.append('D');
                    break;
                case LEFT:
                    builder.append('L');
                    break;
                case RIGHT:
                    builder.append('R');
                    break;
            }
        }
        return builder.toString();
    }
}
