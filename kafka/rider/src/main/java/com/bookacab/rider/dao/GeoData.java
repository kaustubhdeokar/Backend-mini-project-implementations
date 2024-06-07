package com.bookacab.rider.dao;

public class GeoData {

    private String latt;
    private String longt;

    public GeoData(String latt, String longt) {
        this.latt = latt;
        this.longt = longt;
    }

    @Override
    public String toString() {
        return "latitude:'" + latt + ", longitude:'" + longt + '\'';
    }

    public String getLatt() {
        return latt;
    }

    public String getLongt() {
        return longt;
    }
}
