package ru.nsk.ein.sudoku.model;

/**
 * A multi-dimensional region of cells
 */
public interface Region extends Iterable<ImmutableLocation> {

    /**
     * Size of the region
     *
     * @return the number of region cells
     */
    int size();

    /**
     * Checks whether a location is contained in this regions
     *
     * @param location the location
     * @return the check result
     */
    default boolean contains(Location<?> location) {
        for (ImmutableLocation l : this) {
            if (l.equals(location)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether an other region is fully contained in this region
     *
     * @param region the other region
     * @return the check result
     */
    default boolean contains(Region region) {
        for (ImmutableLocation location : region) {
            if (!contains(location)) {
                return false;
            }
        }
        return true;
    }
}
