package ru.nsk.ein.sudoku.model;

/**
 * Represents decimal digits
 */
public enum DecimalDigits {

    ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE;

    @Override
    public String toString() {
        return Integer.toString(ordinal() + 1);
    }
}
