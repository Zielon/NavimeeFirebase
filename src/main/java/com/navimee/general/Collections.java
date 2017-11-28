package com.navimee.general;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class Collections {

    public static <T> List<List<T>> spliter(Collection<T> collection, int splitSize) {

        List<List<T>> split = new ArrayList<>();
        List<T> subList = new ArrayList<>();

        if (collection.size() < splitSize || splitSize < 0)
            return split;

        Iterator<T> iterator = collection.iterator();

        for (int i = 0, j = 1; i < collection.size(); i++, j++) {
            if (iterator.hasNext())
                subList.add(iterator.next());

            if (j == splitSize) {
                split.add(subList);
                subList = new ArrayList<>();
                j = 0;
            }
        }

        if (subList.size() > 0)
            split.add(subList);

        return split;
    }
}
