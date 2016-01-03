package com.aizenberg.support.collection.filter;

import com.aizenberg.support.collection.TraversFilterOperation;

/**
 * Created by Yuriy Aizenberg
 */
public interface TraversOneOfKVFilter<K, V> {

    boolean acceptKey(K key);

    boolean acceptValue(V value);

    TraversFilterOperation getOp();


}
