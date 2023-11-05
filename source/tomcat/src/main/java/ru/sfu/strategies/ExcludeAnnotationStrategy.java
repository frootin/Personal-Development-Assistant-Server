package ru.sfu.strategies;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import ru.sfu.annotations.Exclude;

public class ExcludeAnnotationStrategy implements ExclusionStrategy {
    private String endOfName;

    public ExcludeAnnotationStrategy(String endOfName) {
        this.endOfName = endOfName;
    }

    @Override
    public boolean shouldSkipClass(Class<?> clazz) {
        return false;
    }

    @Override
    public boolean shouldSkipField(FieldAttributes field) {
        return field.getName().endsWith(endOfName) || field.getAnnotation(Exclude.class) != null;
    }
}
