package ru.nsk.ein.sudoku.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

/**
 * A multi-dimension Sudoku grid comprises regions of constrained cells
 *
 * @param <A>
 */
public class RegionConstraintGrid<A extends Enum<A>> implements Grid<A> {

    private final Class<A> alphabet;

    private final ImmutableLocation size;

    private final EnumSet<A> universe;

    private final RectangularRegion area;

    private final A[] cells;

    private List<RegionConstraint<A>>[] cellConstraints;

    private List<RegionConstraint<A>> constraints;

    public RegionConstraintGrid(Class<A> alphabet, Location<?> size, Collection<? extends Region> uniqueRegions) {
        this.alphabet = alphabet;
        this.size = size.toImmutable();
        universe = EnumSet.allOf(alphabet);
        area = new RectangularRegion(this.size.zero(), this.size);
        cells = (A[]) Array.newInstance(alphabet, area.size());
        cellConstraints = (List<RegionConstraint<A>>[]) Array.newInstance(List.class, cells.length);
        constraints = new ArrayList<>(uniqueRegions.size());
        for (Region region : uniqueRegions) {
            if (!area.contains(region)) {
                throw new IllegalArgumentException("Unique region is out of grid area" + region);
            }
            UniqueConstraint<A> constraint = new UniqueConstraint<>(region, universe);
            constraints.add(constraint);
            for (ImmutableLocation location : region) {
                cellConstraints(location).add(constraint);
            }
        }
    }

    private int locationIndex(Location<?> location) {
        int index = 0;
        for (int i = 0; i < size.dimensions(); ++i) {
            index = index * size.position(i) + location.position(i);
        }
        return index;
    }

    private List<RegionConstraint<A>> cellConstraints(int index) {
        List<RegionConstraint<A>> c = cellConstraints[index];
        if (c == null) {
            c = new ArrayList<>();
            cellConstraints[index] = c;
        }
        return c;
    }

    private List<RegionConstraint<A>> cellConstraints(Location<?> location) {
        return cellConstraints(locationIndex(location));
    }

    @Override
    public Class<A> alphabet() {
        return alphabet;
    }

    @Override
    public ImmutableLocation size() {
        return size;
    }

    @Override
    public A cell(Location<?> location) {
        return cells[locationIndex(location)];
    }

    @Override
    public void cell(Location<?> location, A value) {
        int index = locationIndex(location);
        A from = cells[index];
        List<RegionConstraint<A>> constraints = cellConstraints(index);
        cells[index] = value;
        int i = 0;
        try {
            for (; i < constraints.size(); ++i) {
                RegionConstraint<A> constraint = constraints.get(i);
                constraint.cellUpdate(this, location, from, value);
            }
        } catch (IllegalStateException e) {
            // rollback changed cells and constraints
            cells[index] = from;
            try {
                for (--i; i >= 0; --i) {
                    RegionConstraint<A> constraint = constraints.get(i);
                    constraint.cellUpdate(this, location, value, from);
                }
            } catch (IllegalStateException error) {
                // broken constraint reversibility
                throw new Error("Failed to rollback changes", error);
            }
            throw e;
        }
    }

    @Override
    public EnumSet<A> possibleValues(Location<?> location) {
        EnumSet<A> possibleValues = universe.clone();
        for (RegionConstraint<A> constraint : cellConstraints(location)) {
            possibleValues.retainAll(constraint.possibleValues());
        }
        return possibleValues;
    }
}
