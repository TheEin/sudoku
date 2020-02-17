package ru.nsk.ein.sudoku.text;

import ru.nsk.ein.sudoku.model.ImmutableLocation;
import ru.nsk.ein.sudoku.model.RectangularGrid;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class GridPrinter2D<A extends Enum<A>, T extends RectangularGrid<A>> extends GridPrinter<A, T> {

    protected final int width;

    protected final int height;

    protected final String empty;

    protected final List<String> alphabet;

    public GridPrinter2D(T grid, Appendable out, String delimiter) {
        super(grid, out, delimiter);
        if (grid.size().dimensions() != 2) {
            throw new IllegalArgumentException("Only 2D grids are supported");
        }
        width = grid.size().position(0);
        height = grid.size().position(1);
        int padding = grid.universe().stream()
                .map(Objects::toString)
                .map(String::length)
                .max(Integer::compareTo).orElseThrow();
        empty = " ".repeat(padding);
        alphabet = grid.universe().stream()
                .map(Objects::toString)
                .map(s -> s + " ".repeat(padding - s.length()))
                .collect(Collectors.toUnmodifiableList());
    }

    public void print() throws IOException {
        printCellValues();
        printPossibleValuesCounts();
    }

    protected void printCellValues() throws IOException {
        out.append("Cells values\n");
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (x > 0) {
                    out.append(delimiter);
                }
                ImmutableLocation location = ImmutableLocation.of(x, y);
                String cell = Optional.ofNullable(grid.cell(location))
                        .map(Enum::ordinal)
                        .map(alphabet::get)
                        .orElse(empty);
                out.append(cell);
            }
            out.append('\n');
        }
    }

    protected void printPossibleValuesCounts() throws IOException {
        out.append("Possible values counts\n");
        for (int y = 0; y < height; ++y) {
            for (int x = 0; x < width; ++x) {
                if (x > 0) {
                    out.append(delimiter);
                }
                ImmutableLocation location = ImmutableLocation.of(x, y);
                out.append(Integer.toString(grid.possibleValues(location).size()));
            }
            out.append('\n');
        }
    }
}
