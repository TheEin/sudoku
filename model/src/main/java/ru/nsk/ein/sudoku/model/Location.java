package ru.nsk.ein.sudoku.model;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * A multi-dimensional cell location
 */
public interface Location {

    /**
     * Number of location dimensions
     *
     * @return a positive value
     */
    @Positive
    int dimensions();

    /**
     * Position on a dimension
     *
     * @param dimension the dimension index
     * @return the position on the dimension
     */
    @PositiveOrZero
    int position(@PositiveOrZero int dimension);
}
