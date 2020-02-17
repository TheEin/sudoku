package ru.nsk.ein.sudoku.text;

import ru.nsk.ein.sudoku.model.Grid;

public class GridPrinters {

    public static final String DEFAULT_DELIMITER = "\t";

    private GridPrinters() {
    }

    public static <A extends Enum<A>, T extends Grid<A>> GridPrinter<A, T> printer(T grid, Appendable out, String delimiter) {
        if (grid.size().dimensions() == 2) {
            return new GridPrinter2D<>(grid, out, delimiter);
        }
        throw new IllegalArgumentException("Unsupported grid size");
    }

    public static <A extends Enum<A>, T extends Grid<A>> GridPrinter<A, T> defaultPrinter(T grid, Appendable out) {
        return printer(grid, out, DEFAULT_DELIMITER);
    }

    public static <A extends Enum<A>, T extends Grid<A>> GridPrinter<A, T> defaultConsolePrinter(T grid) {
        return printer(grid, System.out, DEFAULT_DELIMITER);
    }
}
