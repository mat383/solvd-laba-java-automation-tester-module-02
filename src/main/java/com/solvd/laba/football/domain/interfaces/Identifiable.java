package com.solvd.laba.football.domain.interfaces;

public interface Identifiable {
    /**
     * sets object id. It can only be done once.
     * When object id is set (either in constructor or with this method)
     * this method will throw RuntimeException
     *
     * @param id
     */
    void setId(long id);

    Long getId();

    default boolean hasSetId() {
        return this.getId() != null;
    }
}
