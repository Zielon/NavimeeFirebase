package com.navimee.firestore;

import java.util.StringJoiner;

public class PathBuilder {

    private StringJoiner joiner = new StringJoiner("/");
    private int countryIndex;
    private int currentIndex;

    public PathBuilder() {
    }

    public PathBuilder(int countryIndex) {
        this.countryIndex = countryIndex;
    }

    public PathBuilder add(String segment) {
        if (countryIndex == currentIndex) {
            joiner.add(System.getenv().get("COUNTRY"));
            currentIndex++;
        }

        joiner.add(segment);
        currentIndex++;
        return this;
    }

    public String build() {
        return joiner.toString();
    }

    @Override
    public String toString() {
        return this.build();
    }
}
