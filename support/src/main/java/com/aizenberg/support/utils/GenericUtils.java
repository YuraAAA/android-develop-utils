package com.aizenberg.support.utils;

/**
 * Created by Yuriy Aizenberg
 */
public class GenericUtils {

    public static <T> T orElse(T data, T def) {
        return data != null ? data : def;
    }

    public static <T> T orNull(T data) {
        return orElse(data, null);
    }

}
