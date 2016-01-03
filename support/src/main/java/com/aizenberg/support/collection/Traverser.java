package com.aizenberg.support.collection;

import com.aizenberg.support.collection.filter.TraversKVFilter;
import com.aizenberg.support.collection.filter.TraversOneOfKVFilter;
import com.aizenberg.support.validation.IValidatable;
import com.aizenberg.support.validation.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yuriy Aizenberg
 */
public class Traverser<K, V> implements ITraversable<K, V> {

    private Map<K, V> map = new HashMap<>();
    private List<Map.Entry<K, V>> entries;
    private int index;

    private Traverser(Map<K, V> map) {
        if (map == null) throw new NullPointerException("Map can't be null");
        this.map = map;
        entries = new ArrayList<>(map.entrySet());
    }

    public static <K, V> ITraversable<K, V> of(Map<K, V> pipeline) {
        return new Traverser<>(pipeline);
    }

    @Override
    public boolean hasNext() {
        return index < entries.size();
    }

    @Override
    public TraversPair<K, V> next() {
        Map.Entry<K, V> kvEntry = entries.get(index);
        index++;
        return new TraversPair<>(kvEntry.getKey(), kvEntry.getValue());
    }

    @Override
    public int size() {
        return entries.size();
    }

    @Override
    public K nextKey() {
        return next().getKey();
    }

    @Override
    public V nextValue() {
        return next().getValue();
    }

    @Override
    public List<K> filterToKeys(TraversFilter<K, V> filter) {
        return new ArrayList<>(filter(filter).keySet());
    }

    @Override
    public List<V> filterToValues(TraversFilter<K, V> filter) {
        return new ArrayList<>(filter(filter).values());
    }

    @Override
    public Map<K, V> filter(TraversFilter<K, V> filter) {
        Map<K, V> toReturn = new HashMap<>();
        IValidatable<K> keyValidatable = filter.getKeyValidatable();
        IValidatable<V> valueValidatable = filter.getValueValidatable();

        switch (filter.getOp()) {
            case AND:
                if (keyValidatable == null || valueValidatable == null)
                    throw new IllegalFilterConfigureException("Setup AND but one of filter null");
                break;
            case OR:
                if (keyValidatable == null && valueValidatable == null) {
                    throw new IllegalFilterConfigureException("Setup OR but no filter configured");
                }
                break;
        }

        while (hasNext()) {
            TraversPair<K, V> next = next();
            K key = next.getKey();
            V value = next.getValue();
            if (filter.getOp() == TraversFilterOperation.NONE) {
                if (keyValidatable != null) {
                    boolean keyValid = Validator.isCustomValid(keyValidatable, key);
                    if (keyValid) toReturn.put(key, value);
                } else {
                    boolean valueValid = Validator.isCustomValid(valueValidatable, value);
                    if (valueValid) toReturn.put(key, value);
                }
            } else if (filter.getOp() == TraversFilterOperation.AND) {
                boolean keyValid = Validator.isCustomValid(keyValidatable, key);
                boolean valueValid = Validator.isCustomValid(valueValidatable, value);
                if (keyValid && valueValid) {
                    toReturn.put(key, value);
                }
            } else {
                boolean keyValid = false;
                if (keyValidatable != null) {
                    keyValid = Validator.isCustomValid(keyValidatable, key);
                }
                boolean valueValid = false;
                if (valueValidatable != null) {
                    valueValid = Validator.isCustomValid(valueValidatable, value);
                }
                if (keyValid || valueValid) {
                    toReturn.put(key, value);
                }

            }
        }
        return toReturn;
    }

    @Override
    public Map<K, V> filter(TraversKVFilter<K, V> filter) {
        if (filter == null) return new HashMap<>(map);
        Map<K, V> toReturn = new HashMap<>();
        while (hasNext()) {
            TraversPair<K, V> next = next();
            K key = next.getKey();
            V value = next.getValue();
            if (filter.accept(key, value)) toReturn.put(key, value);

        }
        return toReturn;
    }

    @Override
    public List<K> filterToKeys(TraversKVFilter<K, V> filter) {
        return new ArrayList<>(filter(filter).keySet());
    }

    @Override
    public List<V> filterToValues(TraversKVFilter<K, V> filter) {
        return new ArrayList<>(filter(filter).values());
    }

    @Override
    public List<K> filterToKeys(TraversOneOfKVFilter<K, V> filter) {
        return new ArrayList<>(filter(filter).keySet());
    }

    @Override
    public List<V> filterToValues(TraversOneOfKVFilter<K, V> filter) {
        return new ArrayList<>(filter(filter).values());
    }

    @Override
    public Map<K, V> filter(TraversOneOfKVFilter<K, V> filter) {
        if (filter == null || filter.getOp() == null || filter.getOp() == TraversFilterOperation.NONE)
            return new HashMap<>(map);

        Map<K, V> toReturn = new HashMap<>();
        while (hasNext()) {
            TraversPair<K, V> next = next();
            K key = next.getKey();
            V value = next.getValue();
            boolean keyAccepted = filter.acceptKey(key);
            boolean valueAccepted = filter.acceptValue(value);
            switch (filter.getOp()) {
                case AND:
                    if (keyAccepted && valueAccepted) toReturn.put(key, value);
                    break;
                case OR:
                    if (keyAccepted || valueAccepted) toReturn.put(key, value);
                    break;
            }

        }
        return toReturn;
    }


}
