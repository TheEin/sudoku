package ru.nsk.ein.sudoku.model;

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

    private final List<A> cells;

    private List<RegionConstraint<A>> constraints;

    private List<List<RegionConstraint<A>>> cellConstraints;

    public RegionConstraintGrid(Class<A> alphabet, Location<?> size, Collection<? extends Region> uniqueRegions) {
        this.alphabet = alphabet;
        this.size = size.toImmutable();
        universe = EnumSet.allOf(alphabet);
        area = new RectangularRegion(this.size.zero(), this.size);
        cells = new ArrayList<>(area.size());
        constraints = new ArrayList<>(uniqueRegions.size());
        for (Region region : uniqueRegions) {
            if (!area.contains(region)) {
                throw new IllegalArgumentException("Unique region is out of grid area" + region);
            }
            constraints.add(new UniqueConstraint<>(region, universe));
        }
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
        throw new UnsupportedOperationException();
    }

    @Override
    public void cell(Location<?> location, A value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public EnumSet<A> possibleValues(Location<?> location) {
        throw new UnsupportedOperationException();
    }
}
