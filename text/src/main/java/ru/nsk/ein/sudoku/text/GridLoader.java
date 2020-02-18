package ru.nsk.ein.sudoku.text;

import ru.nsk.ein.sudoku.model.RectangularGrid;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * Loader is able to set a grid from an input text
 *
 * @param <A> a grid alphabet type
 * @param <T> a grid type
 */
public abstract class GridLoader<A extends Enum<A>, T extends RectangularGrid<A>> {

    protected final T grid;

    protected final BufferedReader in;

    protected final String delimiter;

    public GridLoader(T grid, BufferedReader in, String delimiter) {
        this.grid = grid;
        this.in = in;
        this.delimiter = delimiter;
    }

    public abstract void load() throws IOException;
}
