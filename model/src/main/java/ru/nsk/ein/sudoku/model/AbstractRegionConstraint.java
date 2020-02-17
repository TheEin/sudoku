package ru.nsk.ein.sudoku.model;

public abstract class AbstractRegionConstraint<A extends Enum<A>, R extends Region> implements RegionConstraint<A, R> {

    private final R region;

    public AbstractRegionConstraint(R region) {
        this.region = region;
    }

    @Override
    public R region() {
        return region;
    }
}
