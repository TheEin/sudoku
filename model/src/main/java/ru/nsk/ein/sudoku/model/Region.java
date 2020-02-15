package ru.nsk.ein.sudoku.model;

/**
 * A region of cells
 */
public interface Region extends Iterable<ImmutableLocation> {

    /**
     * Size of the region
     *
     * @return the number of region cells
     */
    int size();
}
