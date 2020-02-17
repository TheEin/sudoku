package ru.nsk.ein.sudoku.solver;

import ru.nsk.ein.sudoku.model.Grid;
import ru.nsk.ein.sudoku.model.Region;

/**
 * Solver is able to fill empty cells without breaking the grid constraints
 *
 * @param <A>
 * @param <T>
 */
public abstract class Solver<A extends Enum<A>, R extends Region, T extends Grid<A, R>> {

    protected final T grid;

    public Solver(T grid) {
        this.grid = grid;
    }

    public T grid() {
        return grid;
    }

    /**
     * Try to solve the grid.
     * Grid may be solved many times starting from preset cell values
     *
     * @throws IllegalStateException if the solver is unable to solve
     */
    public abstract void solve();

    /**
     * Reset the grid cell values to the initial state before first solve
     */
    public abstract void reset();
}
