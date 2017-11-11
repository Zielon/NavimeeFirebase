package com.navimee.firestoreHelpers;

import java.util.*;

public class TransactionSplit {

    public static <K, V> List<Map<K, V>> mapSplit(Map<K, V> map, int chunk) {

        if (chunk > map.size())
            return null;

        Iterator<K> iterator = map.keySet().iterator();
        List<Map<K, V>> listOfSubMaps = new ArrayList<>();
        Map<K, V> subMap = new HashMap<>();

        for (int i = 0, j = 1; i < map.size(); i++, j++) {
            if (iterator.hasNext()) {
                K key = iterator.next();
                subMap.put(key, map.get(key));
            }
            if (j == chunk) {
                listOfSubMaps.add(subMap);
                subMap = new HashMap<>();
                j = 0;
            }
        }

        if (subMap.size() > 0)
            listOfSubMaps.add(subMap);

        return listOfSubMaps;
    }
}
