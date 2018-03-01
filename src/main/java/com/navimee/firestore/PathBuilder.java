package com.navimee.firestore;

import java.util.StringJoiner;

public class PathBuilder {

    private StringJoiner joiner = new StringJoiner("/");

    public PathBuilder() {
    }

    public PathBuilder add(String segment) {
        joiner.add(segment);
        return this;
    }

    public PathBuilder addCountry() {
        joiner.add(System.getenv().get("COUNTRY"));
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
