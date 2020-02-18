package ru.nsk.ein.sudoku.solver;

import org.junit.Before;
import org.junit.Test;
import ru.nsk.ein.sudoku.model.DecimalDigit;
import ru.nsk.ein.sudoku.model.Grid;
import ru.nsk.ein.sudoku.model.RectangularGrid;
import ru.nsk.ein.sudoku.model.RectangularRegion;
import ru.nsk.ein.sudoku.model.SudokuGrids;
import ru.nsk.ein.sudoku.text.GridPrinter;
import ru.nsk.ein.sudoku.text.GridText;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BruteForceSolverTest {

    private static final int EXPECTED_COUNT = 1728;

    private RectangularGrid<DecimalDigit> grid;

    private BruteForceSolver<DecimalDigit, RectangularRegion, RectangularGrid<DecimalDigit>> solver;

    private GridPrinter<DecimalDigit, RectangularGrid<DecimalDigit>> printer;

    @Before
    public void setUp() throws Exception {
        grid = SudokuGrids.regular();
        solver = new BruteForceSolver<>(grid);
        printer = GridText.defaultConsolePrinter(grid);
        solver.solve();
        printer.print();
    }

    @Test
    public void testStart() throws IOException {
        Grid<DecimalDigit, RectangularRegion> prev = grid.snapshot();
        for (int i = 0; i < 1000; ++i) {
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
            assertEquals(EXPECTED_COUNT, n);
        }
    }

    @Test
    public void testFork() throws IOException {
        GridText.loadFromResource(grid, "sample_grid.txt");
        System.out.println("grid = sample_grid");
        printer.print();

        solver = new BruteForceSolver<>(grid);
        solver.solve();
        int n = 1;

        System.out.println("grid first solve");
        printer.print();

        BruteForceSolver<DecimalDigit, RectangularRegion, RectangularGrid<DecimalDigit>> fork = solver.fork();
        GridPrinter<DecimalDigit, RectangularGrid<DecimalDigit>> forkPrinter = GridText.defaultConsolePrinter(fork.grid());

        System.out.println("fork");
        forkPrinter.print();

        System.out.println("grid after fork");
        printer.print();

        for (; n < 2000; ++n) {
            try {
                fork.solve();
            } catch (IllegalStateException ignore) {
                break;
            }
        }
        for (; n < 2000; ++n) {
            try {
                solver.solve();
            } catch (IllegalStateException ignore) {
                break;
            }
        }
        assertEquals(EXPECTED_COUNT, n);
    }
}
