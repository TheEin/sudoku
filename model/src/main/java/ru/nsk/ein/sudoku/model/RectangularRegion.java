package ru.nsk.ein.sudoku.model;

import lombok.ToString;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A multi-dimensional rectangular region of cells identified by two corners
 */
@ToString(onlyExplicitlyIncluded = true)
public class RectangularRegion implements Region {

    @ToString.Include
    private final ImmutableLocation begin;

    @ToString.Include
    private final ImmutableLocation end;

    private ThreadLocal<Integer> size;

    /**
     * Constructor
     *
     * @param begin the left-upper corner of the region (inclusive)
     * @param end   the right-bottom corner of th region (exclusive)
     * @throws IllegalArgumentException if begin is not strictly smaller than end
     */
    public RectangularRegion(ImmutableLocation begin, ImmutableLocation end) {
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

    public static RectangularRegion of(ImmutableLocation begin, ImmutableLocation end) {
        return new RectangularRegion(begin, end);
    }

    public static RectangularRegion of(ImmutableLocation size) {
        return new RectangularRegion(size.zero(), size);
    }

    public static RectangularRegion of2d(int x1, int x2, int y1, int y2) {
        return RectangularRegion.of(ImmutableLocation.of(x1, y1), ImmutableLocation.of(x2, y2));
    }

    public static RectangularRegion of3d(int x1, int x2, int y1, int y2, int z1, int z2) {
        return RectangularRegion.of(ImmutableLocation.of(x1, y1, z1), ImmutableLocation.of(x2, y2, z2));
    }

    @Override
    public boolean contains(Location<?> location) {
        if (begin.dimensions() != location.dimensions()) {
            return false;
        }
        for (int i = 0; i < begin.dimensions(); ++i) {
            if (begin.position(i) > location.position(i)) {
                return false;
            }
        }
        for (int i = 0; i < end.dimensions(); ++i) {
            if (location.position(i) >= end.position(i)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean contains(Region region) {
        if (region instanceof RectangularRegion) {
            RectangularRegion rr = (RectangularRegion) region;
            if (begin.dimensions() != rr.begin.dimensions()) {
                return false;
            }
            for (int i = 0; i < begin.dimensions(); ++i) {
                if (begin.position(i) > rr.begin.position(i)) {
                    return false;
                }
            }
            for (int i = 0; i < end.dimensions(); ++i) {
                if (rr.end.position(i) > end.position(i)) {
                    return false;
                }
            }
            return true;
        } else {
            for (ImmutableLocation location : region) {
                if (!contains(location)) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public Iterator<ImmutableLocation> iterator() {
        return new RegionIterator();
    }

    @Override
    @ToString.Include
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
                next.position(i, begin.position(i));
                if (++i == next.dimensions()) {
                    next = null;
                    break;
                }
            }
            return location;
        }
    }
}
