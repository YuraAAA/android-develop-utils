package com.aizenberg.support.utils;

/**
 * Created by Yuriy Aizenberg
 */
public class StringUtils {

    public static boolean isEmpty(String candidate) {
        return candidate == null || candidate.isEmpty();
    }

    public static boolean notEmpty(String candidate) {
        return !isEmpty(candidate);
    }

    public static String orEmpty(String candidate) {
        return GenericUtils.orElse(candidate, "");
    }

}
