package ru.nsk.ein.sudoku.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RegionConstraintGridTest {

    private static final int DIM_MIN = 2;

    private static final int DIM_MAX = 5;

    private static final int POS_MIN = 5;

    private static final int POS_MAX = 10;

    private static final int GRID_SIZE = 4;

    private static final int BLOCK_SIZE = 2;

    protected static Random random;

    protected static int dimensions;

    protected static int[] testPositions;

    protected ImmutableLocation testSize;

    private Grid<DecimalDigit> grid;

    @BeforeClass
    public static void beforeClass() throws Exception {
        random = new Random();
        dimensions = random.nextInt(DIM_MAX - DIM_MIN) + DIM_MIN;
        testPositions = random.ints(dimensions, POS_MIN, POS_MAX).toArray();
    }

    @Before
    public void setUp() throws Exception {
        testSize = ImmutableLocation.of(testPositions);
        grid = SudokuGrids.square(GRID_SIZE, BLOCK_SIZE);
    }

    @Test
    public void testSolved() {
        RegionConstraintGrid<DecimalDigit> testGrid = new RegionConstraintGrid<>(
                DecimalDigit.class, testSize, Collections.emptyList());
        RectangularRegion area = RectangularRegion.of(testSize);
        for (ImmutableLocation location : area) {
            testGrid.cell(location, DecimalDigit.NINE);
        }
        assertTrue(testGrid.isSolved());
    }

    private void testGrid(int[][] counts) {
        for (int x = 0; x < GRID_SIZE; ++x) {
            for (int y = 0; y < GRID_SIZE; ++y) {
                ImmutableLocation location = ImmutableLocation.of(x, y);
                EnumSet<DecimalDigit> possibleValues = grid.possibleValues(location);
                assertEquals("Validation failed at " + location,
                        counts[y][x], possibleValues.size());
            }
        }
    }

    @Test
    public void testUniverse() {
        int[][] counts = {
                {9, 9, 9, 9},
                {9, 9, 9, 9},
                {9, 9, 9, 9},
                {9, 9, 9, 9}
        };
        testGrid(counts);
    }

    @Test
    public void testBegin() {
        grid.cell(ImmutableLocation.of(0, 0), DecimalDigit.THREE);
        int[][] counts = {
                {8, 8, 8, 8},
                {8, 8, 9, 9},
                {8, 9, 9, 9},
                {8, 9, 9, 9}
        };
        testGrid(counts);
    }

    @Test
    public void testEnd() {
        grid.cell(ImmutableLocation.of(3, 3), DecimalDigit.SIX);
        int[][] counts = {
                {9, 9, 9, 8},
                {9, 9, 9, 8},
                {9, 9, 8, 8},
                {8, 8, 8, 8}
        };
        testGrid(counts);
    }
}
