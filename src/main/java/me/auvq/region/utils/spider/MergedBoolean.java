package me.auvq.region.utils.spider;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class MergedBoolean<T> {

    private final List<Predicate<T>> predicates = new ArrayList<>();

    @NotNull
    public MergedBoolean<T> add(@NotNull final Predicate<T> predicate) {
        this.predicates.add(predicate);

        return this;
    }

    @NotNull
    public boolean build(@NotNull final T object) {
        boolean result = true;

        for (Predicate<T> predicate : this.predicates) {
            if (!predicate.test(object)) {
                result = false;

                break;
            }
        }

        return result;
    }
}