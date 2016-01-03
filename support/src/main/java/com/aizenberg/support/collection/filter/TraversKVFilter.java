package com.aizenberg.support.collection.filter;

/**
 * Created by Yuriy Aizenberg
 */
public interface TraversKVFilter<K, V> {
    boolean accept(K key, V value);

}
