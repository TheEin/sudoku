package ru.nsk.ein.sudoku.solver;

import ru.nsk.ein.sudoku.model.Grid;
import ru.nsk.ein.sudoku.model.Region;

public class SimpleSolver<A extends Enum<A>, R extends Region, T extends Grid<A, R>> extends Solver<A, R, T> {

    public SimpleSolver(T grid) {
        super(grid);
    }

    @Override
    public void solve() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException();
    }
}
