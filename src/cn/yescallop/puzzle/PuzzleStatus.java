package cn.yescallop.puzzle;

import java.util.Random;
import java.util.StringJoiner;

public class PuzzleStatus {

    private static final int[] dx = {0, 0, 1, -1};
    private static final int[] dy = {1, -1, 0, 0};
    final int size;
    final int move;
    final PuzzleStatus parent;
    int[][] matrix;
    int hash;
    int spaceX, spaceY;

    private PuzzleStatus(int[][] matrix, int spaceX, int spaceY, int move, PuzzleStatus parent) {
        this.size = matrix.length;
        this.matrix = matrix;
        this.spaceX = spaceX;
        this.spaceY = spaceY;
        this.move = move;
        this.parent = parent;
    }

    public static PuzzleStatus of(int size, String s) {
        int[][] matrix = new int[size][size];
        int spaceX = -1, spaceY = -1;
        String[] a = s.split("\\n");
        for (int y = 0; y < a.length; y++) {
            String[] ap = a[y].split(",");
            for (int x = 0; x < ap.length; x++) {
                int n = Integer.parseInt(ap[x]);
                matrix[y][x] = n;
                if (n == 0) {
                    spaceX = x;
                    spaceY = y;
                }
            }
        }
        if (spaceX == -1)
            throw new IllegalArgumentException("No space");
        return new PuzzleStatus(matrix, spaceX, spaceY, -1, null);
    }

    public static PuzzleStatus init(int size) {
        int[][] matrix = new int[size][size];
        int tmp = 1;
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                matrix[y][x] = tmp++;
            }
        }
        matrix[size - 1][size - 1] = 0;
        return new PuzzleStatus(matrix, size - 1, size - 1, -1, null);
    }

    public static PuzzleStatus generate(int size, int steps, Random random) {
        PuzzleStatus p = init(size);
        for (int i = 0; i < steps; i++) {
            if (!p.moveRandomly(random)) i--;
        }
        return p;
    }

    static int deepHashCode(int[][] a) {
        int result = 1;
        for (int[] element : a) {
            int elementHash = 1;
            for (int e : element)
                elementHash = elementHash * 31 + e;
            result = result * 31 + elementHash;
        }
        return result;
    }

    public boolean move(int c) {
        int x = spaceX + dx[c];
        int y = spaceY + dy[c];
        if (x != size && x != -1 && y != size && y != -1) {
            matrix[spaceY][spaceX] = matrix[y][x];
            matrix[y][x] = 0;
            spaceX = x;
            spaceY = y;
            return true;
        }
        return false;
    }

    public boolean moveRandomly(Random random) {
        return this.move(random.nextInt(4));
    }

    void calculateHash() {
        this.hash = deepHashCode(matrix);
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

    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    @Override
    public boolean equals(Object obj) {
        int[][] matrix = ((PuzzleStatus) obj).matrix;
        for (int y = 0; y < size; y++) {
            for (int x = 0; x < size; x++) {
                if (matrix[y][x] != this.matrix[y][x])
                    return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    public PuzzleStatus cloneWithMove(int c) {
        int x = spaceX + dx[c];
        int y = spaceY + dy[c];
        if (x != size && x != -1 && y != size && y != -1) {
            int n = matrix[y][x];
            matrix[spaceY][spaceX] = n;
            matrix[y][x] = 0;
            int[][] matrix = new int[size][size];
            for (int i = 0; i < size; i++) {
                System.arraycopy(this.matrix[i], 0, matrix[i], 0, size);
            }
            this.matrix[spaceY][spaceX] = 0;
            this.matrix[y][x] = n;
            return new PuzzleStatus(matrix, x, y, c, this);
        }
        return null;
    }
}
