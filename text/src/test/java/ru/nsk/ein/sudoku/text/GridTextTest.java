package ru.nsk.ein.sudoku.text;

import org.junit.Before;
import org.junit.Test;
import ru.nsk.ein.sudoku.model.DecimalDigit;
import ru.nsk.ein.sudoku.model.ImmutableLocation;
import ru.nsk.ein.sudoku.model.RectangularGrid;
import ru.nsk.ein.sudoku.model.SudokuGrids;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class GridTextTest {

    private RectangularGrid<DecimalDigit> grid;

    @Before
    public void setUp() throws Exception {
        grid = SudokuGrids.regular();
    }

    @Test
    public void testDefaultConsolePrinter() throws IOException {
        DecimalDigit[] universe = DecimalDigit.values();
        for (int i = 0; i < grid.size().position(0); ++i) {
            ImmutableLocation location = ImmutableLocation.of(i, i);
            grid.cell(location, universe[i]);
        }
        GridPrinter<DecimalDigit, RectangularGrid<DecimalDigit>> printer = GridText.defaultConsolePrinter(grid);
        printer.print();
    }

    @Test
    public void testLoadFromResource() throws IOException {
        GridText.loadFromResource(grid, "sample_grid.txt");
        StringBuilder b = new StringBuilder();
        GridPrinter<DecimalDigit, RectangularGrid<DecimalDigit>> printer = GridText.defaultPrinter(grid, b);
        printer.print();
        assertEquals(629202955, b.toString().hashCode());
    }
}
