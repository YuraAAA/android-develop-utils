package com.aizenberg.support.cache;

/**
 * Created by Yuriy Aizenberg
 */
public class TimeCacheable<T> {

    private Long cache;
    private T data;

    public static final Long NO_EXPIRE = -1L;

    public TimeCacheable(Long cache, T data) {
        this(data);
        this.cache = cache;
    }

    public TimeCacheable(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public boolean isExpired() {
        return cache != null && !cache.equals(NO_EXPIRE) && System.currentTimeMillis() > cache;
    }
}
