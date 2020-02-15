package ru.nsk.ein.sudoku.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotSame;

public abstract class LocationTest<T extends Location> {

    private static final int DIM_MIN = 2;

    private static final int DIM_MAX = 5;

    private static final int POS_MIN = 1;

    private static final int POS_MAX = 10;

    protected static Random random;

    protected static int dimensions;

    protected static int[] testPositions;

    protected T testLocation;

    @BeforeClass
    public static void beforeClass() throws Exception {
        random = new Random();
        dimensions = random.nextInt(DIM_MAX - DIM_MIN) + DIM_MIN;
        testPositions = random.ints(dimensions, POS_MIN, POS_MAX).toArray();
    }

    @Before
    public void setUp() throws Exception {
        testLocation = createLocation(testPositions);
    }

    protected abstract T createLocation(int[] positions);

    protected int randomNegative() {
        return -1 - random.nextInt(Integer.MAX_VALUE);
    }

    protected int[] negativePositions() {
        int[] negativePositions = testPositions.clone();
        negativePositions[random.nextInt(negativePositions.length)] = randomNegative();
        return negativePositions;
    }

    protected int[] alternativePositions() {
        int[] alternativePositions = testPositions.clone();
        int i = random.nextInt(alternativePositions.length);
        alternativePositions[i] = alternativePositions[i] + 1;
        return alternativePositions;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEmptyPositions() {
        createLocation(new int[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativePosition() {
        createLocation(negativePositions());
    }

    @Test
    public void testDimensions() {
        assertEquals(testPositions.length, testLocation.dimensions());
    }

    @Test
    public void testPosition() {
        int[] positions = new int[testPositions.length];
        for (int i = 0; i < testPositions.length; ++i) {
            positions[i] = testLocation.position(i);
        }
        assertArrayEquals(testPositions, positions);
    }

    @Test
    public void testEquals() {
        T sameLocation = createLocation(testPositions);
        assertEquals(testLocation, sameLocation);
    }

    @Test
    public void testNotEquals() {
        T alternativeLocation = createLocation(alternativePositions());
        assertNotEquals(testLocation, alternativeLocation);
    }

    @Test
    public void testHashCode() {
        T sameLocation = createLocation(testPositions);
        assertEquals(testLocation.hashCode(), sameLocation.hashCode());
    }

    @Test
    public void testCompareEquals() {
        T sameLocation = createLocation(testPositions);
        assertEquals(0, testLocation.compareTo(sameLocation));
    }

    @Test
    public void testCompareDiffers() {
        int[] positions = new int[testPositions.length];
        for (int i = 0; i < positions.length; ++i) {
            positions[i] = testPositions[i] + 1;
        }
        T biggerLocation = createLocation(positions);
        assertEquals(-1, testLocation.compareTo(biggerLocation));
        assertEquals(1, biggerLocation.compareTo(testLocation));
    }

    @Test
    public void testClone() {
        Location sameLocation = testLocation.clone();
        assertEquals(testLocation, sameLocation);
        assertNotSame(testLocation, sameLocation);
    }

    @Test
    public void toImmutable() {
        ImmutableLocation sameLocation = testLocation.toImmutable();
        assertEquals(0, testLocation.compareTo(sameLocation));
    }

    @Test
    public void toMutable() {
        MutableLocation sameLocation = testLocation.toMutable();
        assertEquals(0, testLocation.compareTo(sameLocation));
        assertNotSame(testLocation, sameLocation);
    }
}