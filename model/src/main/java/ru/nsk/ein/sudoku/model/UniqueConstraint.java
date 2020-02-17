package ru.nsk.ein.sudoku.model;

import lombok.ToString;
import org.springframework.lang.Nullable;

import java.util.EnumSet;
import java.util.Objects;

/**
 * Constraint of unique cell values on a region
 *
 * @param <A> the grid alphabet
 */
@ToString(onlyExplicitlyIncluded = true)
public class UniqueConstraint<A extends Enum<A>, R extends Region> extends AbstractRegionConstraint<A, R> {

    @ToString.Include
    private final EnumSet<A> possibleValues;

    /**
     * Constructor
     *
     * @param region         a region of the constraint
     * @param possibleValues an initial set of possible values
     */
    public UniqueConstraint(R region, EnumSet<A> possibleValues) {
        super(region);
        this.possibleValues = possibleValues.clone();
    }

    @Override
    public EnumSet<A> cellUpdate(Grid<A, R> grid, Location<?> location, @Nullable A from, @Nullable A to) {
        if (!Objects.equals(from, to)) {
            if (to != null) {
                if (!possibleValues.remove(to)) {
                    throw new IllegalStateException("Constraint violation: " + to + " is not a possible value");
                }
            }
            if (from != null) {
                possibleValues.add(from);
            }
        }
        return possibleValues;
    }

    @ToString.Include
    @Override
    public R region() {
        return super.region();
    }

    @Override
    public EnumSet<A> possibleValues() {
        return possibleValues;
    }
}
