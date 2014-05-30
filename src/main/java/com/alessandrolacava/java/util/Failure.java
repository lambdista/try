package com.alessandrolacava.java.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by alacava on 5/29/14.
 */
public final class Failure<T> extends Try<T> {

    private final Exception exception;

    public Failure(Exception exception) {
        this.exception = exception;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public boolean isFailure() {
        return true;
    }

    @Override
    public T get() throws Exception {
        throw exception;
    }

    @Override
    public void forEach(Consumer<? super T> action) {}

    @Override
    public <U> Try<? extends U> map(Function<? super T, ? extends U> mapper) {
        return (Try<U>) this;
    }

    @Override
    public <U> Try<? extends U> flatMap(Function<? super T, ? extends Try<? extends U>> mapper) {
        return (Try<U>) this;
    }

    @Override
    public Try<T> filter(Predicate<? super T> predicate) {
        return this;
    }

    @Override
    public <U> Try<? extends U> recover(Function<Exception, ? extends U> recoverFunction) {
        try {
            return Try.apply(() -> recoverFunction.apply(exception));
        } catch (Exception e) {
            return new Failure<>(e);
        }
    }

    @Override
    public <U> Try<? extends U> recoverWith(Function<Exception, Try<? extends U>> recoverFunction) {
        return null;
    }

    @Override
    public Try<Exception> failed() {
        return new Success<>(exception);
    }
}
