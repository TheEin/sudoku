package ru.nsk.ein.sudoku.solver;

import ru.nsk.ein.sudoku.model.Grid;
import ru.nsk.ein.sudoku.model.ImmutableLocation;
import ru.nsk.ein.sudoku.model.Region;

import java.util.EnumSet;
import java.util.Random;

public class SimpleSolver<A extends Enum<A>, R extends Region, T extends Grid<A, R>> extends Solver<A, R, T> {

    private final Random random = new Random();

    public SimpleSolver(T grid) {
        super(grid);
    }

    @Override
    public void solve() {
        for (ImmutableLocation location : grid.area()) {
            if (grid.cell(location) != null) {
                continue;
            }
            EnumSet<A> possibleValues = grid.possibleValues(location);
            int possibleValuesCounts = possibleValues.size();
            if (possibleValuesCounts == 0) {
                throw new IllegalStateException("Unsolvable grid");
            }
            int index = random.nextInt(possibleValuesCounts);
            A value = possibleValues.stream().skip(index).findFirst().orElseThrow();
            grid.cell(location, value);
        }
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException();
    }
}
