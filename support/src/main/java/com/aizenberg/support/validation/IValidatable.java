package com.aizenberg.support.validation;

/**
 * Created by Yuriy Aizenberg
 */
public interface IValidatable<T> {

    boolean isValid(T candidate);

}
