package com.aizenberg.support.collection;

/**
 * Created by Yuriy Aizenberg
 */
public class TraversPair<K, V> {
    private K key;
    private V value;

    TraversPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "TraversPair{" +
                "key=" + key +
                ", value=" + value +
                '}';
    }
}
