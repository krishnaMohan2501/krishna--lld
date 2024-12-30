package com.mc.lld.todo.service;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

public class PhonePeCoding {

    public static int getTheFirstUniqueElement(List<Integer> streams) {
        List<Integer> list = new ArrayList<>();
        Map<Integer, Pair> valuePairMap = new HashMap<>();
        for (Integer stream : streams) {
            // if present in map then increment the frequency and delete the previous value from list.
            if (valuePairMap.containsKey(stream)) {
                Pair pair = valuePairMap.get(stream);
                int removeFromThisIndex = pair.getIndex();
                list.remove(removeFromThisIndex);
                pair.setIndex(list.size());
                valuePairMap.remove(stream);
            }
            else {
                // if element is not present in map create pair and add the pair in map
                // we have to add this stream element at last.
                Pair pair = new Pair(1, list.size());
                list.add(stream);
                valuePairMap.put(stream, pair);
            }

        }
        return list.get(0);
    }

    public static int getTheFirstUniqueElement1(List<Integer> streams) {
        Map<Integer, Integer> valueFrequencyMap = new LinkedHashMap<>();

        for (Integer stream : streams) {
            valueFrequencyMap.put(stream, valueFrequencyMap.getOrDefault(stream, 0) + 1);
        }

        for (Map.Entry<Integer, Integer> entry : valueFrequencyMap.entrySet()) {
            if (entry.getValue() == 1) {
                return entry.getKey();
            }
        }

        return -1; // In case there are no unique elements
    }


    @Getter
    @Setter
    public static class Pair {
        private int frequency;
        private int index;

        public Pair(int frequency, int index) {
            this.frequency = frequency;
            this.index = index;
        }
    }

}
