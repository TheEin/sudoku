package ru.nsk.ein.sudoku.model;

import org.springframework.lang.Nullable;

import java.util.EnumSet;

/**
 * A region of constrained grid cells
 *
 * @param <A> the grid alphabet
 */
public interface RegionConstraint<A extends Enum<A>, R extends Region> {

    /**
     * Region of grid cells
     *
     * @return the region of grid cells
     */
    R region();

    /**
     * Called upon a grid cell update to validate the possibility
     *
     * @param grid     the grid is being updated
     * @param location the cell location
     * @param from     an old cell value
     * @param to       the new cell value
     * @return the set of possible region cells values
     * @throws IllegalStateException if the update will break the constraint
     */
    EnumSet<A> cellUpdate(Grid<A, R> grid, Location<?> location, @Nullable A from, @Nullable A to);

    /**
     * Possible values for the region of the constraint
     *
     * @return the set of possible region cells values
     */
    EnumSet<A> possibleValues();
}
