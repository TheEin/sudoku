package ru.nsk.ein.sudoku.text;

import ru.nsk.ein.sudoku.model.RectangularGrid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

public class GridText {

    public static final String DEFAULT_DELIMITER = "\t";

    private GridText() {
    }

    public static <A extends Enum<A>, T extends RectangularGrid<A>> GridPrinter<A, T> printer(
            T grid, Appendable out, String delimiter) {

        if (grid.size().dimensions() == 2) {
            return new GridPrinter2D<>(grid, out, delimiter);
        }
        throw new IllegalArgumentException("Unsupported grid size");
    }

    public static <A extends Enum<A>, T extends RectangularGrid<A>> GridPrinter<A, T> defaultPrinter(
            T grid, Appendable out) {

        return printer(grid, out, DEFAULT_DELIMITER);
    }

    public static <A extends Enum<A>, T extends RectangularGrid<A>> GridPrinter<A, T> defaultConsolePrinter(T grid) {
        return printer(grid, System.out, DEFAULT_DELIMITER);
    }

    public static <A extends Enum<A>, T extends RectangularGrid<A>> GridLoader<A, T> loader(
            T grid, BufferedReader in, String delimiter) {

        if (grid.size().dimensions() == 2) {
            return new GridLoader2D<>(grid, in, delimiter);
        }
        throw new IllegalArgumentException("Unsupported grid size");
    }

    public static <A extends Enum<A>, T extends RectangularGrid<A>> GridLoader<A, T> defaultLoader(
            T grid, BufferedReader in) {

        return loader(grid, in, DEFAULT_DELIMITER);
    }

    public static <A extends Enum<A>, T extends RectangularGrid<A>> void loadFromResource(
            T grid, String resource) throws IOException {

        ClassLoader classLoader = Optional.ofNullable(Thread.currentThread().getContextClassLoader())
                .orElseGet(ClassLoader::getSystemClassLoader);
        InputStream inputStream = classLoader.getResourceAsStream(resource);
        if (inputStream == null) {
            throw new IOException("Resource not found: " + resource);
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            loader(grid, reader, DEFAULT_DELIMITER).load();
        }
    }
}
