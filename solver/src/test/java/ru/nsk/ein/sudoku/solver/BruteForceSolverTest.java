package ru.nsk.ein.sudoku.solver;

import org.junit.Before;
import org.junit.Test;
import ru.nsk.ein.sudoku.model.DecimalDigit;
import ru.nsk.ein.sudoku.model.RectangularGrid;
import ru.nsk.ein.sudoku.model.RectangularRegion;
import ru.nsk.ein.sudoku.model.SudokuGrids;
import ru.nsk.ein.sudoku.text.GridPrinter;
import ru.nsk.ein.sudoku.text.GridPrinters;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;

public class BruteForceSolverTest {

    private RectangularGrid<DecimalDigit> grid;

    private BruteForceSolver<DecimalDigit, RectangularRegion, RectangularGrid<DecimalDigit>> solver;

    private GridPrinter<DecimalDigit, RectangularGrid<DecimalDigit>> printer;

    @Before
    public void setUp() throws Exception {
        grid = SudokuGrids.regular();
        solver = new BruteForceSolver<>(grid);
        printer = GridPrinters.defaultConsolePrinter(grid);
    }

    @Test
    public void testSingle() throws IOException {
        try {
            solver.solve();
            System.out.println("Grid has been solved");
        } catch (IllegalStateException e) {
            System.out.println("Grid was not solved; empty cells count = " + grid.emptyCellsCount());
        } finally {
            printer.print();
        }
    }

    @Test
    public void testAll() throws IOException {
        AtomicInteger n = new AtomicInteger();
        ScheduledExecutorService service = Executors.newScheduledThreadPool(1);
        service.scheduleWithFixedDelay(() -> {
            System.out.println("Grid has been solved " + n.get() + " times");
            System.out.println("Changes: " + Arrays.toString(solver.changes()));
        }, 1, 10, SECONDS);
        while (true) {
            try {
                solver.solve();
                n.incrementAndGet();
            } catch (IllegalStateException e) {
                service.shutdown();
                try {
                    service.awaitTermination(10, SECONDS);
                } catch (InterruptedException ignore) {
                }
                System.out.println("Grid was solved " + n + " times");
                solver.solve();
            }
        }
    }
}
