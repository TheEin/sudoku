package ru.nsk.ein.sudoku.model;

import lombok.ToString;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A multi-dimensional cell location
 */
@ToString
public abstract class Location<T extends Location<T>> implements Comparable<Location<?>>, Iterable<Integer>, Cloneable {

    protected final int[] positions;

    public Location(@Positive int[] positions) {
        if (positions.length == 0) {
            throw new IllegalArgumentException("Zero dimensions location is not allowed");
        }
        for (int value : positions) {
            checkPosition(value);
        }
        this.positions = positions.clone();
    }

    private static int compare(Location<?> a, Location<?> b, int dimension) {
        return Integer.signum(a.position(dimension) - b.position(dimension));
    }

    protected void checkPosition(int value) {
        if (value < 0) {
            throw new IllegalArgumentException("Location positions ought to be positive");
        }
    }

    /**
     * Number of location dimensions
     *
     * @return a positive value
     */
    @Positive
    public int dimensions() {
        return positions.length;
    }

    /**
     * Position on a dimension
     *
     * @param dimension the dimension index
     * @return the position on the dimension
     */
    @PositiveOrZero
    public int position(@PositiveOrZero int dimension) {
        return positions[dimension];
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Location<?>)) {
            return false;
        }
        Location<?> location = (Location<?>) o;
        return Arrays.equals(positions, location.positions);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(positions);
    }

    /**
     * Strictly compares this location with an other
     *
     * @param o the location to compare with
     * @return one of {@code -1, 0, 1} depending on comparison result
     * @throws IllegalArgumentException if locations dimensions are different
     *                                  or locations are not strictly comparable
     */
    @Override
    public int compareTo(Location o) {
        if (dimensions() != o.dimensions()) {
            throw new IllegalArgumentException("This location dimensions = " + dimensions() +
                    " differs from the other location dimensions = " + o.dimensions());
        }
        int c = compare(this, o, 0);
        for (int i = 1; i < dimensions(); ++i) {
            if (compare(this, o, i) != c) {
                throw new IllegalArgumentException("Locations are not strictly comparable: " +
                        "this = " + this + ", other = " + o);
            }
        }
        return c;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new LocationIterator();
    }

    public T clone() {
        try {
            return (T) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create location with zero positions with same dimensions
     *
     * @return a new location
     */
    public abstract T zero();

    /**
     * Immutable location (new or the same)
     *
     * @return the immutable location
     */
    public abstract ImmutableLocation toImmutable();

    /**
     * New mutable location with positions copy
     *
     * @return the mutable location
     */
    public MutableLocation toMutable() {
        return new MutableLocation(positions);
    }

    private class LocationIterator implements Iterator<Integer> {

        private int index;

        @Override
        public boolean hasNext() {
            return index < positions.length;
        }

        @Override
        public Integer next() {
            if (index >= positions.length) {
                throw new NoSuchElementException();
            }
            return positions[index++];
        }
    }
}
