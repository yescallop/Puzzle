package cn.yescallop.puzzle;

import java.util.Random;
import java.util.StringJoiner;

public class Puzzle {

    final int[][] matrix;
    final long[] index;
    final int size;

    private Puzzle(int size) {
        this.size = size;
        this.matrix = new int[size][size];
        this.index = new long[size * size];
    }

    public Puzzle(int[][] matrix) {
        this.size = matrix.length;
        this.matrix = matrix;
        this.index = new long[size * size];
        calculateIndexes();
    }

    public static Puzzle of(int size, String s) {
        Puzzle p = new Puzzle(size);
        String[] a = s.split("\\n");
        for (int y = 0; y < a.length; y++) {
            String[] ap = a[y].split(",");
            for (int x = 0; x < ap.length; x++) {
                int n = Integer.parseInt(ap[x]);
                p.matrix[y][x] = n;
                p.index[n] = (long) y << 32 | x;
            }
        }
        return p;
    }

    public static Puzzle init(int size) {
        Puzzle p = new Puzzle(size);
        int y = 0;
        int x = 0;
        long index = (long) y << 32;
        for (int i = 1; i < size * size; i++) {
            p.matrix[y][x] = i;
            p.index[i] = index | x;
            if (++x == size && y != size - 1) {
                y++;
                index = (long) y << 32;
                x = 0;
            }
        }
        p.index[0] = (long) (size - 1) << 32 | (size - 1);
        return p;
    }

    public static Puzzle generate(int size, int steps, Random random) {
        Puzzle p = init(size);
        for (int i = 0; i < steps; i++) {
            p.moveRandomly(random);
        }
        return p;
    }

    private void calculateIndexes() {
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                index[matrix[y][x]] = (long) y << 32 | x;
            }
        }
    }

    void moveRight() {
        int x = (int) index[0];
        if (x != 0) {
            int y = (int) (index[0] >> 32);
            int n = matrix[y][x - 1];
            matrix[y][x] = n;
            matrix[y][x - 1] = 0;

            long i = index[0];
            index[0] = index[n];
            index[n] = i;
        }
    }

    void moveLeft() {
        int x = (int) index[0];
        if (x != size - 1) {
            int y = (int) (index[0] >> 32);
            int n = matrix[y][x + 1];
            matrix[y][x] = n;
            matrix[y][x + 1] = 0;

            long i = index[0];
            index[0] = index[n];
            index[n] = i;
        }
    }

    void moveDown() {
        int y = (int) (index[0] >> 32);
        if (y != 0) {
            int x = (int) index[0];
            int n = matrix[y - 1][x];
            matrix[y][x] = n;
            matrix[y - 1][x] = 0;

            long i = index[0];
            index[0] = index[n];
            index[n] = i;
        }
    }

    void moveUp() {
        int y = (int) (index[0] >> 32);
        if (y != size - 1) {
            int x = (int) index[0];
            int n = matrix[y + 1][x];
            matrix[y][x] = n;
            matrix[y + 1][x] = 0;

            long i = index[0];
            index[0] = index[n];
            index[n] = i;
        }
    }

    private void moveRandomly(Random random) {
        int n = random.nextInt(4);
        switch (n) {
            case 0:
                moveRight();
                break;
            case 1:
                moveLeft();
                break;
            case 2:
                moveDown();
                break;
            case 3:
                moveUp();
        }
    }

    @Override
    public String toString() {
        StringJoiner a = new StringJoiner("\n");
        for (int y = 0; y < size; y++) {
            StringJoiner b = new StringJoiner(",");
            for (int x = 0; x < size; x++) {
                b.add(Integer.toString(matrix[y][x]));
            }
            a.add(b.toString());
        }
        return a.toString();
    }
}
