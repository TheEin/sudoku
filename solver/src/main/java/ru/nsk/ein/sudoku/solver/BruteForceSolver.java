package ru.nsk.ein.sudoku.solver;

import ru.nsk.ein.sudoku.model.Grid;
import ru.nsk.ein.sudoku.model.Region;

import java.util.EnumSet;
import java.util.Iterator;

/**
 * Brute force solver implementation
 *
 * @param <A> the grid alphabet
 * @param <R> the grid area type
 * @param <T> the grid type
 */
public class BruteForceSolver<A extends Enum<A>, R extends Region, T extends Grid<A, R>> extends Solver<A, R, T> {

    private boolean[] fixed;

    private int index = -1;

    private A value;

    public BruteForceSolver(T grid) {
        super(grid);
        fixed = new boolean[grid.cellsCount()];
        for (int i = 0; i < fixed.length; ++i) {
            fixed[i] = grid.cell(i) != null;
        }
    }

    private boolean nextEmpty() {
        do {
            ++index;
            if (index == fixed.length) {
                return false;
            }
            value = grid.cell(index);
        } while (value != null);
        return true;
    }

    private boolean prevChanged() {
        do {
            --index;
            if (index < 0) {
                return false;
            }
        } while (fixed[index]);
        value = grid.cell(index);
        return true;
    }

    @Override
    public void solve() {
        if (index == fixed.length) {
            if (!prevChanged()) {
                throw new IllegalStateException("Unsolvable grid");
            }
        } else if (!nextEmpty()) {
            return;
        }
        while (true) {
            if (value != null) {
                grid.cell(index, null);
            }
            EnumSet<A> possibleValues = grid.possibleValues(index);
            Iterator<A> iterator = possibleValues.iterator();
            if (value != null) {
                while (iterator.hasNext()) {
                    if (iterator.next().equals(value)) {
                        break;
                    }
                }
            }
            if (!iterator.hasNext()) {
                if (!prevChanged()) {
                    throw new IllegalStateException("Unsolvable grid");
                }
                continue;
            }
            do {
                value = iterator.next();
                try {
                    grid.cell(index, value);
                } catch (IllegalStateException e) {
                    value = null;
                }
            } while (value == null && iterator.hasNext());
            if (value == null) {
                if (!prevChanged()) {
                    throw new IllegalStateException("Unsolvable grid");
                }
            } else if (!nextEmpty()) {
                return;
            }
        }
    }

    public BruteForceSolver<A, R, T> fork() {
        BruteForceSolver<A, R, T> fork = new BruteForceSolver<>(gridCopy());
        while (true) {
            fork.nextEmpty();
            if (fork.grid.emptyCellsCount() < 10) {
                throw new IllegalStateException("Solving is about to finish");
            }
            A v = grid.cell(fork.index);
            EnumSet<A> possibleValues = fork.grid.possibleValues(fork.index);
            Iterator<A> iterator = possibleValues.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().equals(v)) {
                    break;
                }
            }
            fork.grid.cell(fork.index, v);
            fork.fixed[fork.index] = true;
            if (iterator.hasNext()) {
                A n = iterator.next();
                index = fork.index;
                while (++fork.index < fixed.length) {
                    fork.grid.cell(fork.index, grid.cell(fork.index));
                    fork.fixed[fork.index] = fixed[fork.index];
                    if (!fixed[fork.index]) {
                        grid.cell(fork.index, null);
                    }
                }
                grid.cell(index, n);
                return fork;
            }
        }
    }

    private T gridCopy() {
        T copy = grid.emptyCopy();
        for (int i = 0; i < fixed.length; ++i) {
            if (fixed[i]) {
                copy.cell(i, grid.cell(i));
            }
        }
        return copy;
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException();
    }
}
