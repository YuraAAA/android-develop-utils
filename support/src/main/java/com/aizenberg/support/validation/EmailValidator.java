package com.aizenberg.support.validation;

import android.util.Patterns;

/**
 * Created by Yuriy Aizenberg
 */
public class EmailValidator extends PatternValidator {
    @Override
    public String getPattern() {
        return Patterns.EMAIL_ADDRESS.pattern();
    }
}
