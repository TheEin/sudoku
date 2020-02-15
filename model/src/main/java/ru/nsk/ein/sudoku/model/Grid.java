package ru.nsk.ein.sudoku.model;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Set;

/**
 * A multi-dimensional Sudoku grid
 */
public interface Grid {

    /**
     * Constant value of an unset cell
     */
    int NOT_SET = 0;

    /**
     * Number of different cell values
     *
     * @return the maximum allowed cell value
     */
    @PositiveOrZero
    int radix();

    /**
     * Size of the grid
     *
     * @return a location beyond the grid distant corner
     */
    Location size();

    /**
     * Value of a cell
     *
     * @param location a location of a cell
     * @return the cell value or {@link #NOT_SET}
     * @throws IndexOutOfBoundsException in the location is beyond the grid
     * @throws IllegalArgumentException  if the location dimensions differs
     */
    @PositiveOrZero
    int cell(Location location);

    /**
     * Value of a cell
     *
     * @param location a location of a cell
     * @param value    the cell value or {@link #NOT_SET}
     * @throws IndexOutOfBoundsException in the location is beyond the grid
     * @throws IllegalArgumentException  if the location dimensions differs
     *                                   or the value is out of range
     * @throws IllegalStateException     if setting the cell to that value will break the grid constraints
     */
    void cell(Location location, @PositiveOrZero int value);

    /**
     * Possible values for a cell those are not breaking the grid constraints
     *
     * @param location a location of a cell
     * @return a list of possible values;
     * may not be empty if the cell is not set at the location
     */
    Set<@Positive Integer> possibleValues(Location location);
}
