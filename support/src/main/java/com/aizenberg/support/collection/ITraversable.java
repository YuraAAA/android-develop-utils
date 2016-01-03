package com.aizenberg.support.collection;

import com.aizenberg.support.collection.filter.TraversKVFilter;
import com.aizenberg.support.collection.filter.TraversOneOfKVFilter;

import java.util.List;
import java.util.Map;

/**
 * Created by Yuriy Aizenberg
 */
public interface ITraversable<K, V> {


    boolean hasNext();

    TraversPair<K, V> next();

    int size();

    K nextKey();

    V nextValue();

    Map<K, V> filter(TraversFilter<K, V> filter);

    List<K> filterToKeys(TraversFilter<K, V> filter);

    List<V> filterToValues(TraversFilter<K, V> filter);


    Map<K, V> filter(TraversKVFilter<K, V> filter);

    List<K> filterToKeys(TraversKVFilter<K, V> filter);

    List<V> filterToValues(TraversKVFilter<K, V> filter);


    Map<K, V> filter(TraversOneOfKVFilter<K, V> filter);

    List<K> filterToKeys(TraversOneOfKVFilter<K, V> filter);

    List<V> filterToValues(TraversOneOfKVFilter<K, V> filter);


}
