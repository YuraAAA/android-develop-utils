package com.aizenberg.support.validation;

import com.aizenberg.support.common.collection.ClassInstanceCache;
import com.aizenberg.support.utils.GenericUtils;
import com.aizenberg.support.utils.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yuriy Aizenberg
 */
public class Validator {


    private static ClassInstanceCache<PatternValidator> classInstanceCache;
    private static Map<String, IValidatable> validatableClassInstanceCache;

    static {
        classInstanceCache = new ClassInstanceCache<>();
        validatableClassInstanceCache = new HashMap<>();
    }

    public static boolean isEmail(String candidate) {
        return StringUtils.notEmpty(candidate) && isValidCustomPattern(EmailValidator.class, candidate);
    }

    public static <T> void registerCustomValidator(String aliasName, IValidatable<T> validatable) {
        validatableClassInstanceCache.put(aliasName, validatable);
    }

    public static <T> boolean isCustomValid(String key, T data) {
        IValidatable iValidatable = validatableClassInstanceCache.get(key);
        return iValidatable != null && iValidatable.isValid(data);
    }

    public static boolean isValidCustomPattern(Class<? extends PatternValidator> clazz, String candidate) {
        try {
            if (classInstanceCache.containsKey(clazz))
                return classInstanceCache.get(clazz).isValid(candidate);
            PatternValidator patternValidator = clazz.newInstance();
            classInstanceCache.put(clazz, patternValidator);
            return patternValidator.isValid(candidate);
        } catch (Exception e) {
            throw new ValidationException(String.format("Class %s can't instantiate. Make sure exist public empty constructor", clazz.getSimpleName()));
        }
    }

    public static <T> boolean isCustomValid(IValidatable<T> validatable, T data) {
        return validatable.isValid(data);
    }


}
