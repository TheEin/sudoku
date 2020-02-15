package ru.nsk.ein.sudoku.model;

import javax.validation.constraints.Positive;

/**
 * A multi-dimensional cell location. Immutable by design
 */
public final class ImmutableLocation extends Location {

    public static ImmutableLocation of(int... positions) {
        return new ImmutableLocation(positions);
    }

    public ImmutableLocation(@Positive int[] positions) {
        super(positions);
    }

    @Override
    public ImmutableLocation toImmutable() {
        return this;
    }
}
