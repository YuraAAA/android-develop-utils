package com.aizenberg.support.common.collection;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Yuriy Aizenberg
 */
public class NullExcludeSafetyList<E> extends CopyOnWriteArrayList<E> {

    @Override
    public boolean add(E object) {
        return object != null && super.add(object);
    }

    @Override
    public E get(int index) {
        if (index < 0) return null;
        if (index > size() - 1) return null;
        return super.get(index);
    }

    @Override
    public void add(int index, E object) {
        if (object == null) return;
        super.add(index, object);
    }


    @Override
    public boolean addAll(Collection<? extends E> collection) {
        removeNulls(collection);
        return super.addAll(collection);
    }

    @Override
    public boolean addAll(int index, Collection<? extends E> collection) {
        removeNulls(collection);
        return super.addAll(index, collection);
    }

    private void removeNulls(Collection<? extends E> collection) {
        Iterator<? extends E> iterator = collection.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() == null) iterator.remove();
        }
    }
}
