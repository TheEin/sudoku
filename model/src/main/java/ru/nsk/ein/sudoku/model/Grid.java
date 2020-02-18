package ru.nsk.ein.sudoku.model;

import org.springframework.lang.Nullable;

import java.util.EnumSet;
import java.util.Optional;

/**
 * A multi-dimensional Sudoku grid
 *
 * @param <A> the grid alphabet
 * @param <R> the grid area type
 */
public interface Grid<A extends Enum<A>, R extends Region> extends Comparable<Grid<A, R>> {

    /**
     * Alphabet of a grid
     *
     * @return the alphabet enum type
     */
    Class<A> alphabet();

    /**
     * All alphabet values
     *
     * @return the alphabet enum set
     */
    EnumSet<A> universe();

    /**
     * Region of a grid
     *
     * @return the grid region
     */
    R area();

    /**
     * Count of grid cells
     *
     * @return the cell count
     */
    default int cellsCount() {
        return area().size();
    }

    /**
     * Count of empty grid cells
     *
     * @return the empty cells count
     */
    default int emptyCellsCount() {
        int c = 0;
        for (int i = 0; i < cellsCount(); ++i) {
            if (cell(i) == null) {
                ++c;
            }
        }
        return c;
    }

    /**
     * Value of a cell
     *
     * @param index plain location index
     * @return the cell value or {@code null} if it's not set
     * @throws IndexOutOfBoundsException if the location index is out of range
     * @throws IllegalArgumentException  if the location dimensions differs
     */
    @Nullable
    A cell(int index);

    /**
     * Value of a cell
     *
     * @param location a location of a cell
     * @return the cell value or {@code null} if it's not set
     * @throws IndexOutOfBoundsException if the location is beyond the grid
     * @throws IllegalArgumentException  if the location dimensions differs
     */
    @Nullable
    default A cell(Location<?> location) {
        return cell(locationIndex(location));
    }

    /**
     * Set a value of a cell
     *
     * @param index plain location index
     * @param value the cell value or {@code null} to unset
     * @throws IndexOutOfBoundsException in the location is beyond the grid
     * @throws IllegalArgumentException  if the location dimensions differs
     * @throws IllegalStateException     if setting the cell to that value will break the grid constraints
     */
    void cell(int index, @Nullable A value);

    /**
     * Set a value of a cell
     *
     * @param location a location of a cell
     * @param value    the cell value or {@code null} to unset
     * @throws IndexOutOfBoundsException in the location is beyond the grid
     * @throws IllegalArgumentException  if the location dimensions differs
     * @throws IllegalStateException     if setting the cell to that value will break the grid constraints
     */
    void cell(Location<?> location, @Nullable A value);

    /**
     * Possible values for a cell those are not breaking the grid constraints
     *
     * @param index plain location index
     * @return a list of possible values;
     * may not be empty if the cell is not set at the location
     */
    EnumSet<A> possibleValues(int index);

    /**
     * Possible values for a cell those are not breaking the grid constraints
     *
     * @param location a location of a cell
     * @return a list of possible values;
     * may not be empty if the cell is not set at the location
     */
    default EnumSet<A> possibleValues(Location<?> location) {
        return possibleValues(locationIndex(location));
    }

    /**
     * Calculate location index
     *
     * @param location location of a cell
     * @return plain location index
     */
    int locationIndex(Location<?> location);

    /**
     * Create an empty copy of the grid
     *
     * @return the copy
     */
    <T extends Grid<A, R>> T emptyCopy();

    /**
     * Create a read-only copy of the grid
     *
     * @return the copy
     */
    default Grid<A, R> snapshot() {
        return new SnapshotGrid<>(this);
    }

    /**
     * Test whether the grid is solved
     *
     * @return the check result
     * @throws IllegalStateException if the grid is unsolvable
     */
    default boolean isSolved() {
        boolean solved = true;
        for (ImmutableLocation location : area()) {
            A value = cell(location);
            if (value == null) {
                if (possibleValues(location).isEmpty()) {
                    throw new IllegalStateException("Grid is unsolvable");
                }
                solved = false;
            }
        }
        return solved;
    }

    /**
     * Compares with an another grid cell by cell
     *
     * @param grid the other grid
     * @return the comparison result
     * @throws IllegalStateException if the grid regions differ
     */
    default int compareTo(Grid<A, R> grid) {
        if (cellsCount() != grid.cellsCount()) {
            throw new IllegalArgumentException("Grid regions differ");
        }
        for (int i = 0; i < cellsCount(); ++i) {
            int v1 = Optional.ofNullable(cell(i)).map(Enum::ordinal).orElse(-1);
            int v2 = Optional.ofNullable(grid.cell(i)).map(Enum::ordinal).orElse(-1);
            if (v1 != v2) {
                return v1 - v2;
            }
        }
        return 0;
    }
}
