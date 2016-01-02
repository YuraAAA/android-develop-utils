package com.aizenberg.support.validation;

/**
 * Created by Yuriy Aizenberg
 */
public abstract class PatternValidator implements IValidatable<String> {

    public abstract String getPattern();

    @Override
    public boolean isValid(String candidate) {
        return candidate.matches(getPattern());
    }
}
