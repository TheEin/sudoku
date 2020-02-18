package ru.nsk.ein.sudoku.model;

import java.lang.reflect.Array;
import java.util.EnumSet;

public class SnapshotGrid<A extends Enum<A>, R extends Region> implements Grid<A, R> {

    private final Grid<A, R> prototype;

    private final Class<A> alphabet;

    private final EnumSet<A> universe;

    private final R area;

    private final A[] cells;

    public SnapshotGrid(Grid<A, R> prototype) {
        this.prototype = prototype;
        alphabet = prototype.alphabet();
        universe = prototype.universe();
        area = prototype.area();
        cells = (A[]) Array.newInstance(alphabet, prototype.cellsCount());
        for (int i = 0; i < cells.length; ++i) {
            cells[i] = prototype.cell(i);
        }
    }

    @Override
    public Class<A> alphabet() {
        return alphabet;
    }

    @Override
    public EnumSet<A> universe() {
        return universe;
    }

    @Override
    public R area() {
        return area;
    }

    @Override
    public A cell(int index) {
        return cells[index];
    }

    @Override
    public void cell(int index, A value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void cell(Location<?> location, A value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EnumSet<A> possibleValues(int index) {
        return EnumSet.noneOf(alphabet);
    }

    @Override
    public int locationIndex(Location<?> location) {
        return prototype.locationIndex(location);
    }

    @Override
    public <T extends Grid<A, R>> T emptyCopy() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Grid<A, R> snapshot() {
        return this;
    }
}
