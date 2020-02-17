package ru.nsk.ein.sudoku.model;

import org.junit.Test;

import java.util.EnumSet;

import static org.junit.Assert.assertEquals;

public class UniqueConstraintTest {

    private static ImmutableLocation location;

    private static RectangularRegion region;

    private static Grid<DecimalDigit, RectangularRegion> grid;

    @Test(expected = IllegalStateException.class)
    public void testEmpty() {
        UniqueConstraint<DecimalDigit, RectangularRegion> constraint = new UniqueConstraint<>(region, EnumSet.noneOf(DecimalDigit.class));
        EnumSet<DecimalDigit> possibleValues = constraint.cellUpdate(grid, location, null, DecimalDigit.ONE);
        assertEquals(0, possibleValues.size());
    }

    @Test
    public void testEquals() {
        UniqueConstraint<DecimalDigit, RectangularRegion> constraint = new UniqueConstraint<>(region, EnumSet.of(DecimalDigit.FIVE));
        constraint.cellUpdate(grid, location, null, DecimalDigit.FIVE);
        EnumSet<DecimalDigit> possibleValues = constraint.cellUpdate(grid, location, DecimalDigit.FIVE, DecimalDigit.FIVE);
        assertEquals(0, possibleValues.size());
    }

    @Test
    public void testSingle() {
        UniqueConstraint<DecimalDigit, RectangularRegion> constraint = new UniqueConstraint<>(region, EnumSet.of(DecimalDigit.FIVE));
        EnumSet<DecimalDigit> possibleValues = constraint.cellUpdate(grid, location, null, DecimalDigit.FIVE);
        assertEquals(0, possibleValues.size());
    }

    @Test
    public void testDouble() {
        UniqueConstraint<DecimalDigit, RectangularRegion> constraint = new UniqueConstraint<>(region,
                EnumSet.of(DecimalDigit.THREE, DecimalDigit.SEVEN));
        constraint.cellUpdate(grid, location, null, DecimalDigit.THREE);
        EnumSet<DecimalDigit> possibleValues = constraint.cellUpdate(grid, location, DecimalDigit.THREE, DecimalDigit.SEVEN);
        assertEquals(1, possibleValues.size());
    }
}
