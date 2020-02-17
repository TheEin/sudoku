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

public class SimpleSolverTest {

    private RectangularGrid<DecimalDigit> grid;

    private Solver<DecimalDigit, RectangularRegion, RectangularGrid<DecimalDigit>> solver;

    private GridPrinter<DecimalDigit, RectangularGrid<DecimalDigit>> printer;

    @Before
    public void setUp() throws Exception {
        grid = SudokuGrids.regular();
        solver = new SimpleSolver<>(grid);
        printer = GridPrinters.defaultConsolePrinter(grid);
    }

    @Test
    public void test() throws IOException {
        try {
            solver.solve();
            System.out.println("Grid has been solved");
        } catch (IllegalStateException e) {
            System.out.println("Grid was not solved; empty cells count = " + grid.emptyCellsCount());
        } finally {
            printer.print();
        }
    }
}
