package ru.nsk.ein.sudoku.model;

import javax.validation.constraints.Positive;
import java.util.ArrayList;
import java.util.List;

/**
 * Sudoku grid factories
 */
public class SudokuGrids {

    private SudokuGrids() {
    }

    /**
     * Rectangular Sudoku grid with non-intersectional blocks those fully covering the grid
     *
     * @param <A>         the grid alphabet
     * @param alphabet    an alphabet of a grid
     * @param gridWidth   a total grid width in cells
     * @param gridHeight  a total grid height in cells
     * @param blockWidth  a width in cells of the every block
     * @param blockHeight a height in cells of the every block
     * @return the rectangular grid
     */
    public static <A extends Enum<A>> Grid<A> rectangular(Class<A> alphabet,
                                                          @Positive int gridWidth, @Positive int gridHeight,
                                                          @Positive int blockWidth, @Positive int blockHeight) {

        if (gridWidth <= 0 || gridHeight <= 0 || blockWidth <= 0 || blockHeight <= 0) {
            throw new IllegalArgumentException("Size elements ought to be positive");
        }
        if (gridWidth % blockWidth != 0) {
            throw new IllegalArgumentException("Block width has to correlate to grid width");
        }
        if (gridHeight % blockHeight != 0) {
            throw new IllegalArgumentException("Block height has to correlate to grid height");
        }
        int blocksCount = (gridWidth / blockWidth) * (gridHeight / blockHeight);

        List<Region> uniqueRegions = new ArrayList<>(blocksCount + gridWidth + gridHeight);

        for (int x = 0; x < gridWidth; ) {
            uniqueRegions.add(RectangularRegion.of2d(x, ++x, 0, gridHeight));
        }
        for (int y = 0; y < gridHeight; ) {
            uniqueRegions.add(RectangularRegion.of2d(0, gridWidth, y, ++y));
        }
        for (int x1 = 0, x2 = blockWidth; x1 < gridWidth; x1 = x2, x2 += blockWidth) {
            for (int y = 0; y < gridHeight; ) {
                uniqueRegions.add(RectangularRegion.of2d(x1, x2, y, y += blockHeight));
            }
        }
        return new RegionConstraintGrid<>(alphabet, ImmutableLocation.of(gridWidth, gridHeight), uniqueRegions);
    }

    /**
     * Square Sudoku grid is having the same characteristics as the {@link #rectangular(Class, int, int, int, int) rectangular}
     * except diagonal symmetry
     *
     * @param <A>       the grid alphabet
     * @param alphabet  an alphabet of a grid
     * @param gridSize  a total width and a total height of the grid in cells
     * @param blockSize a width and a height in cells of the every block
     * @return the square grid
     */
    public static <A extends Enum<A>> Grid<A> square(Class<A> alphabet, @Positive int gridSize, @Positive int blockSize) {
        return rectangular(alphabet, gridSize, gridSize, blockSize, blockSize);
    }

    /**
     * Regular Sudoku grid configuration
     *
     * @return square 9x9 grid with 3x3 blocks with 1-9 numbers
     */
    public static Grid<DecimalDigit> regular() {
        return square(DecimalDigit.class, 9, 3);
    }
}
