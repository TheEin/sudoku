package ru.nsk.ein.sudoku.model;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A rectangle region of cells identified by two corners
 */
public class RectangleRegion implements Region {

    private final ImmutableLocation begin;

    private final ImmutableLocation end;

    private ThreadLocal<Integer> size;

    public RectangleRegion(ImmutableLocation begin, ImmutableLocation end) {
        if (begin.compareTo(end) >= 0) {
            throw new IllegalArgumentException("Beginning of the square ought ot be strictly smaller than ending");
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

    @Override
    public Iterator<ImmutableLocation> iterator() {
        return new RegionIterator();
    }

    @Override
    public int size() {
        return size.get();
    }

    private int length(int dimension) {
        return begin.position(dimension) - end.position(dimension) + 1;
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
                if (next.incrementAndGet(i) <= end.position(i)) {
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
