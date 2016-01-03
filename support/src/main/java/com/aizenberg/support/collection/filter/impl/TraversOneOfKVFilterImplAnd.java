package com.aizenberg.support.collection.filter.impl;

import com.aizenberg.support.collection.TraversFilterOperation;
import com.aizenberg.support.collection.filter.TraversOneOfKVFilter;

/**
 * Created by Yuriy Aizenberg
 */
public abstract class TraversOneOfKVFilterImplAnd<K, V> implements TraversOneOfKVFilter<K, V> {

    @Override
    public TraversFilterOperation getOp() {
        return TraversFilterOperation.AND;
    }
}
