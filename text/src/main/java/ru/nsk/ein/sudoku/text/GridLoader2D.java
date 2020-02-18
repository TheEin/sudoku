package ru.nsk.ein.sudoku.text;

import ru.nsk.ein.sudoku.model.MutableLocation;
import ru.nsk.ein.sudoku.model.RectangularGrid;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GridLoader2D<A extends Enum<A>, T extends RectangularGrid<A>> extends GridLoader<A, T> {

    protected final int width;

    protected final int height;

    protected final Map<String, A> alphabet;

    public GridLoader2D(T grid, BufferedReader in, String delimiter) {
        super(grid, in, delimiter);
        if (grid.size().dimensions() != 2) {
            throw new IllegalArgumentException("Only 2D grids are supported");
        }
        width = grid.size().position(0);
        height = grid.size().position(1);
        alphabet = new HashMap<>();
        for (A value : grid.universe()) {
            alphabet.put(value.toString(), value);
        }
    }

    @Override
    public void load() throws IOException {
        MutableLocation location = MutableLocation.of(0, 0);
        do {
            String line = in.readLine();
            if (line == null) {
                break;
            }
            for (String s : line.split(delimiter)) {
                s = s.trim();
                if (s.isEmpty()) {
                    continue;
                }
                A value = alphabet.get(s);
                grid.cell(location, value);
                if (location.incrementAndGet(0) == width) {
                    break;
                }
            }

            if (location.position(0) < width) {
                do {
                    grid.cell(location, null);
                } while (location.incrementAndGet(0) < width);
            }
            location.position(0, 0);

        } while (location.incrementAndGet(1) < height);

        if (location.position(1) < height) {
            do {
                do {
                    grid.cell(location, null);
                } while (location.incrementAndGet(0) < width);
                location.position(0, 0);
            } while (location.incrementAndGet(1) < height);
        }
    }
}
