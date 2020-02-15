package ru.nsk.ein.sudoku.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MutableLocationTest extends LocationTest<MutableLocation> {

    @Override
    protected MutableLocation createLocation(int[] positions) {
        return MutableLocation.of(positions);
    }

    @Test
    public void testSetPosition() {
        ImmutableLocation before = testLocation.toImmutable();
        testLocation.position(0, testLocation.position(0) + 1);
        ImmutableLocation after = testLocation.toImmutable();
        assertNotEquals(before, after);
    }

    @Test
    public void testIncrementPosition() {
        int before = testLocation.position(0);
        int after = testLocation.incrementAndGet(0);
        assertEquals(before + 1, after);
    }
}
