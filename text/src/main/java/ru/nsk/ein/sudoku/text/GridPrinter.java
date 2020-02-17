package ru.nsk.ein.sudoku.text;

import ru.nsk.ein.sudoku.model.Grid;

import java.io.IOException;

/**
 * Printer is able to present grid as a formatted text
 *
 * @param <A> a grid alphabet type
 * @param <T> a grid type
 */
public abstract class GridPrinter<A extends Enum<A>, T extends Grid<A>> {

    protected final T grid;

    protected final Appendable out;

    protected final String delimiter;

    public GridPrinter(T grid, Appendable out, String delimiter) {
        this.grid = grid;
        this.out = out;
        this.delimiter = delimiter;
    }

    public abstract void print() throws IOException;
}
