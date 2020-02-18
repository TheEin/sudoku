package ru.nsk.ein.sudoku.solver;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import ru.nsk.ein.sudoku.model.DecimalDigit;
import ru.nsk.ein.sudoku.model.Grid;
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
import static org.junit.Assert.assertTrue;

public class BruteForceSolverTest {

    private RectangularGrid<DecimalDigit> grid;

    private BruteForceSolver<DecimalDigit, RectangularRegion, RectangularGrid<DecimalDigit>> solver;

    private GridPrinter<DecimalDigit, RectangularGrid<DecimalDigit>> printer;

    @Before
    public void setUp() throws Exception {
        grid = SudokuGrids.regular();
        solver = new BruteForceSolver<>(grid);
        printer = GridPrinters.defaultConsolePrinter(grid);
        solver.solve();
        printer.print();
    }

    @Test
    public void testStart() throws IOException {
        Grid<DecimalDigit, RectangularRegion> prev = grid.snapshot();
        for (int i = 0; i < 2; ++i) {
            solver.solve();
            Grid<DecimalDigit, RectangularRegion> snapshot = grid.snapshot();
            assertTrue(prev.compareTo(snapshot) < 0);
            prev = snapshot;
        }
    }

    @Test(expected = IllegalStateException.class)
    public void testEnd() throws IOException {
        for (int i = 0; i < grid.cellsCount(); i += 3) {
            grid.cell(i, null);
        }
        solver = new BruteForceSolver<>(grid);
        int n = 0;
        try {
            Grid<DecimalDigit, RectangularRegion> prev = grid.snapshot();
            for (; n < 2000; ++n) {
                solver.solve();
                Grid<DecimalDigit, RectangularRegion> snapshot = grid.snapshot();
                assertTrue(prev.compareTo(snapshot) != 0);
                prev = snapshot;
            }
        } finally {
            System.out.println("Grid was solved " + n + " times");
            printer.print();
        }
    }

    @Test
    @Ignore
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
