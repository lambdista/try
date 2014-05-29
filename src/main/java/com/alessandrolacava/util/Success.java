package com.alessandrolacava.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by alacava on 5/29/14.
 */
public final class Success<T> extends Try<T> {

    private final T value;

    public Success(T value) {
        this.value = value;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }

    @Override
    public boolean isFailure() {
        return false;
    }

    @Override
    public T get() {
        return value;
    }

    @Override
    public void forEach(Consumer<? super T> action) {

    }

    @Override
    public <U> Try<? extends U> map(Function<? super T, ? extends U> mapper) {
        return null;
    }

    @Override
    public <U> Try<? extends U> flatMap(Function<? super T, ? extends Try<? extends U>> mapper) {
        return null;
    }

    @Override
    public Try<T> filter(Predicate<? super T> predicate) {
        return null;
    }

    @Override
    public <U> Try<? extends U> recover(Function<Exception, ? extends U> recoverFunction) {
        return null;
    }

    @Override
    public <U> Try<? extends U> recoverWith(Function<Exception, Try<? extends U>> recoverFunction) {
        return null;
    }

    @Override
    public Try<Exception> failed() {
        return null;
    }
}
