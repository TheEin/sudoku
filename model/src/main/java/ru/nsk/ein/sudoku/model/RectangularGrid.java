package ru.nsk.ein.sudoku.model;

public interface RectangularGrid<A extends Enum<A>> extends Grid<A, RectangularRegion> {

    /**
     * Size of the grid
     *
     * @return a location beyond the grid distant corner
     */
    ImmutableLocation size();

    /**
     * Calculate location index
     *
     * @param location location of a cell
     * @return plain location index
     */
    default int locationIndex(Location<?> location) {
        int index = 0;
        for (int i = 0; i < size().dimensions(); ++i) {
            index = index * size().position(i) + location.position(i);
        }
        return index;
    }
}
