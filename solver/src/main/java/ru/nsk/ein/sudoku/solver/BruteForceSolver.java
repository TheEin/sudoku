package ru.nsk.ein.sudoku.solver;

import ru.nsk.ein.sudoku.model.Grid;
import ru.nsk.ein.sudoku.model.Region;

import java.lang.reflect.Array;
import java.util.Arrays;
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

    private A[] changes;

    private int index = -1;

    private A value;

    public BruteForceSolver(T grid) {
        super(grid);
        changes = (A[]) Array.newInstance(grid.alphabet(), grid.cellsCount());
    }

    public A[] changes() {
        if (index <= 0) {
            return null;
        }
        return Arrays.copyOf(changes, index);
    }

    private boolean nextEmpty() {
        do {
            ++index;
            if (index == changes.length) {
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
            value = changes[index];
        } while (value == null);
        return true;
    }

    @Override
    public void solve() {
        if (index == changes.length) {
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
            } else {
                changes[index] = value;
                if (!nextEmpty()) {
                    return;
                }
            }
        }
    }

    private BruteForceSolver<A,R,T> fork() {
        T g = grid.emptyCopy();
        throw new UnsupportedOperationException();
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException();
    }
}
