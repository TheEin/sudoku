package ru.nsk.ein.sudoku.model;

import org.springframework.lang.Nullable;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A multi-dimension Sudoku grid comprises regions of constrained cells
 *
 * @param <A>
 */
public class RegionConstraintGrid<A extends Enum<A>> implements RectangularGrid<A> {

    private final Class<A> alphabet;

    private final ImmutableLocation size;

    private final EnumSet<A> universe;

    private final RectangularRegion area;

    private final A[] cells;

    private int emptyCellsCount;

    private List<RegionConstraint<A, RectangularRegion>>[] cellConstraints;

    private List<RegionConstraint<A, RectangularRegion>> constraints;

    public RegionConstraintGrid(Class<A> alphabet, Location<?> size, Collection<RectangularRegion> uniqueRegions) {
        this.alphabet = alphabet;
        this.size = size.toImmutable();
        universe = EnumSet.allOf(alphabet);
        area = RectangularRegion.of(this.size);
        cells = (A[]) Array.newInstance(alphabet, area.size());
        emptyCellsCount = cells.length;
        cellConstraints = (List<RegionConstraint<A, RectangularRegion>>[]) Array.newInstance(List.class, cells.length);
        constraints = new ArrayList<>(uniqueRegions.size());
        for (RectangularRegion region : uniqueRegions) {
            if (!area.contains(region)) {
                throw new IllegalArgumentException("Unique region is out of grid area" + region);
            }
            UniqueConstraint<A, RectangularRegion> constraint = new UniqueConstraint<>(region, universe);
            constraints.add(constraint);
            for (ImmutableLocation location : region) {
                cellConstraints(location).add(constraint);
            }
        }
    }

    private List<RegionConstraint<A, RectangularRegion>> cellConstraints(int index) {
        List<RegionConstraint<A, RectangularRegion>> c = cellConstraints[index];
        if (c == null) {
            c = new ArrayList<>();
            cellConstraints[index] = c;
        }
        return c;
    }

    private List<RegionConstraint<A, RectangularRegion>> cellConstraints(Location<?> location) {
        return cellConstraints(locationIndex(location));
    }

    @Override
    public Class<A> alphabet() {
        return alphabet;
    }

    @Override
    public EnumSet<A> universe() {
        return universe.clone();
    }

    @Override
    public RectangularRegion area() {
        return area;
    }

    @Override
    public ImmutableLocation size() {
        return size;
    }

    @Override
    public int cellsCount() {
        return cells.length;
    }

    @Override
    public int emptyCellsCount() {
        return emptyCellsCount;
    }

    @Override
    public A cell(int index) {
        return cells[index];
    }

    @Override
    public void cell(int index, A value) {
        cell(null, index, value);
    }

    @Override
    public void cell(Location<?> location, A value) {
        cell(location, locationIndex(location), value);
    }

    private void cell(@Nullable Location<?> location, int index, @Nullable A value) {
        A from = cells[index];
        cells[index] = value;
        if (from != null) {
            ++emptyCellsCount;
        }
        if (value != null) {
            --emptyCellsCount;
        }
        List<RegionConstraint<A, RectangularRegion>> constraints = cellConstraints(index);
        int i = 0;
        try {
            for (; i < constraints.size(); ++i) {
                RegionConstraint<A, RectangularRegion> constraint = constraints.get(i);
                constraint.cellUpdate(this, location, index, from, value);
            }
        } catch (IllegalStateException e) {
            // rollback changed cells and constraints
            cells[index] = from;
            if (value != null) {
                ++emptyCellsCount;
            }
            if (from != null) {
                --emptyCellsCount;
            }
            try {
                for (--i; i >= 0; --i) {
                    RegionConstraint<A, RectangularRegion> constraint = constraints.get(i);
                    constraint.cellUpdate(this, location, index, value, from);
                }
            } catch (IllegalStateException error) {
                // broken constraint reversibility
                throw new Error("Failed to rollback changes", error);
            }
            throw e;
        }
    }

    @Override
    public EnumSet<A> possibleValues(int index) {
        EnumSet<A> possibleValues = universe();
        for (RegionConstraint<A, RectangularRegion> constraint : cellConstraints(index)) {
            possibleValues.retainAll(constraint.possibleValues());
        }
        return possibleValues;
    }

    @Override
    public <T extends Grid<A, RectangularRegion>> T duplicate() {
        return (T) new RegionConstraintGrid<A>(
                alphabet,
                size,
                constraints.stream()
                        .map(RegionConstraint::region)
                        .collect(Collectors.toList()));

    }
}
