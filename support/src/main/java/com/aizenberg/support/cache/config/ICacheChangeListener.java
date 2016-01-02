package com.aizenberg.support.cache.config;

import java.util.List;

/**
 * Created by Yuriy Aizenberg
 */
public interface ICacheChangeListener<T> {

    void onChange(T data);

    void onChange(List<T> data);

}
