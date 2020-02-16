package ru.nsk.ein.sudoku.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class RectangularRegionTest {

    private static final int DIM_MIN = 2;

    private static final int DIM_MAX = 5;

    private static final int POS_MIN = 2;

    private static final int POS_MAX = 10;

    private static final int LEN_MIN = 2;

    private static final int LEN_MAX = 10;

    protected static Random random;

    protected static int dimensions;

    protected ImmutableLocation begin;

    protected ImmutableLocation end;

    protected ImmutableLocation location;

    protected RectangularRegion region;

    @BeforeClass
    public static void beforeClass() throws Exception {
        random = new Random();
        dimensions = random.nextInt(DIM_MAX - DIM_MIN) + DIM_MIN;
    }

    @Before
    public void setUp() throws Exception {
        int[] positions = random.ints(dimensions, POS_MIN, POS_MAX).toArray();
        begin = ImmutableLocation.of(positions);
        for (int i = 0; i < positions.length; ++i) {
            positions[i] = positions[i] + random.nextInt(LEN_MAX - LEN_MIN + 1) + LEN_MIN;
        }
        end = ImmutableLocation.of(positions);
        MutableLocation location = end.toMutable();
        for (int i = 0; i < location.dimensions(); ++i) {
            location.position(i, location.position(i) - 1);
        }
        this.location = location.toImmutable();
        region = new RectangularRegion(begin, end);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvertedCorners() {
        new RectangularRegion(end, begin);
    }

    @Test
    public void testContainsBegin() {
        assertTrue(region.contains(begin));
    }

    @Test
    public void testNotContainsEnd() {
        assertFalse(region.contains(end));
    }

    @Test
    public void testContainsLocation() {
        assertTrue(region.contains(location));
    }

    @Test
    public void testContainsRegion() {
        assertTrue(region.contains(region));
    }

    @Test
    public void testContainsSmallerRegion() {
        assertTrue(region.contains(RectangularRegion.of(begin, location)));
    }

    @Test
    public void testNotContainsRegion() {
        assertFalse(RectangularRegion.of(begin, location).contains(region));
    }

    private void testRegion(RectangularRegion r, int size) {
        assertEquals(size, r.size());
        Set<ImmutableLocation> locations = new HashSet<>();
        r.forEach(location -> assertTrue(locations.add(location)));
        assertEquals(size, locations.size());
    }

    @Test
    public void testRegion1() {
        testRegion(RectangularRegion.of2d(0, 1, 0, 1), 1);
    }

    @Test
    public void testRegion2() {
        testRegion(RectangularRegion.of2d(1, 2, 1, 2), 1);
    }

    @Test
    public void testRegion3() {
        testRegion(RectangularRegion.of2d(0, 2, 0, 2), 4);
    }

    @Test
    public void testRegion4() {
        testRegion(RectangularRegion.of2d(0, 5, 0, 3), 15);
    }

    @Test
    public void testRegion5() {
        testRegion(RectangularRegion.of2d(1, 2, 0, 5), 5);
    }

    @Test
    public void testRegion6() {
        testRegion(RectangularRegion.of3d(0, 5, 0, 3, 0, 2), 30);
    }
}