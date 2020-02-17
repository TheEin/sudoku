package ru.nsk.ein.sudoku.text;

import org.junit.Before;
import org.junit.Test;
import ru.nsk.ein.sudoku.model.DecimalDigit;
import ru.nsk.ein.sudoku.model.ImmutableLocation;
import ru.nsk.ein.sudoku.model.RectangularGrid;
import ru.nsk.ein.sudoku.model.SudokuGrids;

import java.io.IOException;

public class GridPrintersText {

    private RectangularGrid<DecimalDigit> regularGrid;

    @Before
    public void setUp() throws Exception {
        regularGrid = SudokuGrids.regular();
        DecimalDigit[] universe = DecimalDigit.values();
        for (int i = 0; i < regularGrid.size().position(0); ++i) {
            ImmutableLocation location = ImmutableLocation.of(i, i);
            regularGrid.cell(location, universe[i]);
        }
    }

    @Test
    public void testDefaultConsolePrinter() throws IOException {
        GridPrinter<DecimalDigit, RectangularGrid<DecimalDigit>> printer = GridPrinters.defaultConsolePrinter(regularGrid);
        printer.print();
    }
}
