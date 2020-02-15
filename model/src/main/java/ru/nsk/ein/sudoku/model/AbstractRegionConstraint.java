package ru.nsk.ein.sudoku.model;

public abstract class AbstractRegionConstraint<A extends Enum<A>> implements RegionConstraint<A> {

    private final Region region;

    public AbstractRegionConstraint(Region region) {
        this.region = region;
    }

    @Override
    public Region region() {
        return region;
    }
}
