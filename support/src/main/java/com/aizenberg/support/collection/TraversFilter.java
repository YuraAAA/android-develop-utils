package com.aizenberg.support.collection;

import com.aizenberg.support.validation.IValidatable;

/**
 * Created by Yuriy Aizenberg
 */
public class TraversFilter<K, V> {

    private TraversFilterOperation op = TraversFilterOperation.NONE;
    private IValidatable<K> keyValidatable;
    private IValidatable<V> valueValidatable;


    public TraversFilterOperation getOp() {
        return op;
    }

    public IValidatable<K> getKeyValidatable() {
        return keyValidatable;
    }

    public IValidatable<V> getValueValidatable() {
        return valueValidatable;
    }

    public static <K, V> TraversFilter<K, V> newFilter() {
        return new TraversFilter<>();
    }


    private TraversFilter() {
    }

    public TraversFilter<K, V> key(IValidatable<K> keyValidatable) {
        if (keyValidatable == null) throw new NullPointerException("Key filter can't be null");
        this.keyValidatable = keyValidatable;
        return this;
    }

    public TraversFilter<K, V> and() {
        this.op = TraversFilterOperation.AND;
        return this;
    }

    public TraversFilter<K, V> or() {
        this.op = TraversFilterOperation.OR;
        return this;
    }

    public TraversFilter<K, V> value(IValidatable<V> valueValidatable) {
        if (valueValidatable == null)
            throw new NullPointerException("Value filter can't be null");
        this.valueValidatable = valueValidatable;
        return this;
    }


}
