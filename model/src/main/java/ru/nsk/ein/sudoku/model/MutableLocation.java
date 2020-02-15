package ru.nsk.ein.sudoku.model;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

/**
 * A multi-dimensional cell location. Mutable by design
 */
public class MutableLocation extends Location {

    public MutableLocation(@Positive int[] positions) {
        super(positions);
    }

    public static MutableLocation of(int... positions) {
        return new MutableLocation(positions);
    }

    /**
     * Set position on a dimension
     *
     * @param dimension the dimension index
     * @param value     the position on the dimension
     */
    public void position(@PositiveOrZero int dimension, @PositiveOrZero int value) {
        checkPosition(value);
        positions[dimension] = value;
    }

    /**
     * Increment a position on a dimension
     *
     * @param dimension the dimension index
     * @return the incremented value of the position
     */
    @PositiveOrZero
    public int incrementAndGet(@PositiveOrZero int dimension) {
        int value = position(dimension) + 1;
        position(dimension, value);
        return value;
    }

    @Override
    public ImmutableLocation toImmutable() {
        return new ImmutableLocation(positions);
    }
}
