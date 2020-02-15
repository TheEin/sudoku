package ru.nsk.ein.sudoku.model;

import java.util.EnumSet;

/**
 * A multi-dimension Sudoku grid comprises regions of constrained cells
 *
 * @param <A>
 */
public class RegionConstraintGrid<A extends Enum<A>> implements Grid<A> {

    private final Class<A> alphabet;

    private final Location size;

    private final EnumSet<A> universe;

    public RegionConstraintGrid(Class<A> alphabet, Location size) {
        this.alphabet = alphabet;
        this.size = size;
        universe = EnumSet.allOf(alphabet);
    }

    @Override
    public Class<A> alphabet() {
        return alphabet;
    }

    @Override
    public Location size() {
        return size;
    }

    @Override
    public A cell(Location location) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void cell(Location location, A value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EnumSet<A> possibleValues(Location location) {
        throw new UnsupportedOperationException();
    }
}
