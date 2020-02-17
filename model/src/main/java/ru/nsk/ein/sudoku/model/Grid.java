package ru.nsk.ein.sudoku.model;

import org.springframework.lang.Nullable;

import java.util.EnumSet;

/**
 * A multi-dimensional Sudoku grid
 *
 * @param <A> the grid alphabet
 */
public interface Grid<A extends Enum<A>> {

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
     * Size of the grid
     *
     * @return a location beyond the grid distant corner
     */
    ImmutableLocation size();

    /**
     * Value of a cell
     *
     * @param location a location of a cell
     * @return the cell value or {@code null} if it's not set
     * @throws IndexOutOfBoundsException in the location is beyond the grid
     * @throws IllegalArgumentException  if the location dimensions differs
     */
    @Nullable
    A cell(Location<?> location);

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
     * @param location a location of a cell
     * @return a list of possible values;
     * may not be empty if the cell is not set at the location
     */
    EnumSet<A> possibleValues(Location<?> location);

    /**
     * Test whether the grid is solved
     *
     * @return the check result
     * @throws IllegalStateException if the grid is unsolvable
     */
    default boolean isSolved() {
        boolean solved = true;
        RectangularRegion area = RectangularRegion.of(size());
        for (ImmutableLocation location : area) {
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
}
