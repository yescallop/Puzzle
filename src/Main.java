import cn.yescallop.puzzle.Puzzle;
import cn.yescallop.puzzle.PuzzleSolver;
import cn.yescallop.puzzle.Record;

import java.util.Random;

public class Main {

    public static void main(String[] args) {
        Random random = new Random();

        while (true) {
            Puzzle p = Puzzle.generate(10, 100000, random);
            Record r = new Record();
            PuzzleSolver solver = new PuzzleSolver(p, r);
            //long start = System.nanoTime();
            solver.solve();
            //TODO: Solve the problem causes these two numbers to be unsolved
            if (!solver.solved(87) || !solver.solved(97))
                System.out.println("fucked up");
            //long stop = System.nanoTime();
        }
        //System.out.println((stop - start) / 1000000000d);
        //System.out.println(p);
    }
}
