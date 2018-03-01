package com.navimee.general;

import java.text.Normalizer;

public class StringNormalizer {
    public static String stripAccents(String input) {
        return input == null ? null :
                Normalizer.normalize(input, Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
