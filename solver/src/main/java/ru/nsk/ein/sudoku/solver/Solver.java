package ru.nsk.ein.sudoku.solver;

import ru.nsk.ein.sudoku.model.Grid;

/**
 * Solver is able to fill empty cells without breaking the grid constraints
 *
 * @param <A>
 * @param <T>
 */
public abstract class Solver<A extends Enum<A>, T extends Grid<A>> {

    protected final T grid;

    protected Solver(T grid) {
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
