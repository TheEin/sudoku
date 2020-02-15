package ru.nsk.ein.sudoku.model;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A multi-dimensional rectangle region of cells identified by two corners
 */
public class RectangleRegion implements Region {

    private final ImmutableLocation begin;

    private final ImmutableLocation end;

    private ThreadLocal<Integer> size;

    /**
     * Constructor
     *
     * @param begin the left-upper corner of the region (inclusive)
     * @param end   the right-bottom corner of th region (exclusive)
     * @throws IllegalArgumentException if begin is not strictly smaller than end
     */
    public RectangleRegion(ImmutableLocation begin, ImmutableLocation end) {
        if (begin.compareTo(end) >= 0) {
            throw new IllegalArgumentException("Beginning of the square ought to be strictly smaller than ending");
        }
        this.begin = begin;
        this.end = end;
        size = InheritableThreadLocal.withInitial(() -> {
            int s = length(0);
            for (int i = 1; i < begin.dimensions(); ++i) {
                s *= length(i);
            }
            return s;
        });
    }

    public static RectangleRegion of(ImmutableLocation begin, ImmutableLocation end) {
        return new RectangleRegion(begin, end);
    }

    public static RectangleRegion of2d(int x1, int x2, int y1, int y2) {
        return RectangleRegion.of(ImmutableLocation.of(x1, y1), ImmutableLocation.of(x2, y2));
    }

    public static RectangleRegion of3d(int x1, int x2, int y1, int y2, int z1, int z2) {
        return RectangleRegion.of(ImmutableLocation.of(x1, y1, z1), ImmutableLocation.of(x2, y2, z2));
    }

    @Override
    public Iterator<ImmutableLocation> iterator() {
        return new RegionIterator();
    }

    @Override
    public int size() {
        return size.get();
    }

    private int length(int dimension) {
        return end.position(dimension) - begin.position(dimension);
    }

    private class RegionIterator implements Iterator<ImmutableLocation> {

        private MutableLocation next = begin.toMutable();

        @Override
        public boolean hasNext() {
            return next != null;
        }

        @Override
        public ImmutableLocation next() {
            if (next == null) {
                throw new NoSuchElementException();
            }
            ImmutableLocation location = next.toImmutable();
            for (int i = 0; ; ) {
                if (next.incrementAndGet(i) < end.position(i)) {
                    break;
                }
                next.position(i, 0);
                if (++i == next.dimensions()) {
                    next = null;
                    break;
                }
            }
            return location;
        }
    }
}
