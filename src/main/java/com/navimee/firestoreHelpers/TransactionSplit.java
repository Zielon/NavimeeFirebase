package com.navimee.firestoreHelpers;

import java.util.*;

public class TransactionSplit {

    public static <K, V> List<Map<K, V>> mapSplit(Map<K, V> map, int chunk) {
        int rest = map.size() % chunk;
        int count = (map.size() - rest) / chunk;
        Iterator<K> iterator = map.keySet().iterator();

        List<Map<K, V>> listOfSubMaps = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Map<K, V> subMap = new HashMap<>();
            while (subMap.size() < chunk) {
                if (iterator.hasNext()) {
                    K key = iterator.next();
                    subMap.put(key, map.get(key));
                }
            }
            listOfSubMaps.add(subMap);
        }

        Map<K, V> subMap = new HashMap<>();
        for (int i = 0; i < rest; i++) {
            if (iterator.hasNext()) {
                K key = iterator.next();
                subMap.put(key, map.get(key));
            }
        }

        listOfSubMaps.add(subMap);
        return listOfSubMaps;
    }
}
