package ru.nsk.ein.sudoku.model;

public class ImmutableLocationTest extends LocationTest<ImmutableLocation> {

    @Override
    protected ImmutableLocation createLocation(int[] positions) {
        return new ImmutableLocation(positions);
    }
}
