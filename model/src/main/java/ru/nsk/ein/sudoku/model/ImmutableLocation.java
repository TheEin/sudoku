package ru.nsk.ein.sudoku.model;

import javax.validation.constraints.Positive;

/**
 * A multi-dimensional cell location. Immutable by design
 */
public final class ImmutableLocation extends Location<ImmutableLocation> {

    public ImmutableLocation(@Positive int[] positions) {
        super(positions);
    }

    public static ImmutableLocation of(int... positions) {
        return new ImmutableLocation(positions);
    }

    @Override
    public ImmutableLocation zero() {
        return ImmutableLocation.of(new int[dimensions()]);
    }

    @Override
    public ImmutableLocation toImmutable() {
        return this;
    }
}
