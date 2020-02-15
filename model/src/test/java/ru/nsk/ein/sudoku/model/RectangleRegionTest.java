package ru.nsk.ein.sudoku.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RectangleRegionTest {

    private static final int DIM_MIN = 2;

    private static final int DIM_MAX = 5;

    private static final int POS_MIN = 1;

    private static final int POS_MAX = 10;

    private static final int LEN_MAX = 10;

    protected static Random random;

    protected static int dimensions;

    protected ImmutableLocation begin;

    protected ImmutableLocation end;

    protected RectangleRegion region;

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
            positions[i] = positions[i] + random.nextInt(LEN_MAX) + 1;
        }
        end = ImmutableLocation.of(positions);
        region = new RectangleRegion(begin, end);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvertedCorners() {
        new RectangleRegion(end, begin);
    }

    private void testRegion(int x1, int y1, int x2, int y2, int size) {
        RectangleRegion r1 = new RectangleRegion(ImmutableLocation.of(x1, y1), ImmutableLocation.of(x2, y2));
        assertEquals(size, r1.size());
        Set<ImmutableLocation> locations = new HashSet<>();
        r1.forEach(location -> assertTrue(locations.add(location)));
        assertEquals(size, locations.size());
    }

    @Test
    public void testRegion0() {
        testRegion(0, 0, 1, 1, 1);
    }

    @Test
    public void testRegion1() {
        testRegion(1, 1, 2, 2, 1);
    }


    @Test
    public void testRegion2() {
        testRegion(0, 0, 2, 2, 4);
    }
}