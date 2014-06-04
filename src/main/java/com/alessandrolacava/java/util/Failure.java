package com.alessandrolacava.java.util;

import java.util.Optional;
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
    public void forEach(Consumer<? super T> action) {
    }

    @Override
    public <U> Try<U> map(Function<? super T, ? extends U> mapper) {
        return (Try<U>) this;
    }

    @Override
    public <U> Try<U> flatMap(Function<? super T, ? extends Try<U>> mapper) {
        return (Try<U>) this;
    }

    @Override
    public Try<T> filter(Predicate<? super T> predicate) {
        return this;
    }

    @Override
    public <U> Try<U> recover(Function<? super Exception, ? extends U> recoverFunction) {
        try {
            return Try.apply(() -> recoverFunction.apply(exception));
        } catch (Exception e) {
            return new Failure<>(e);
        }
    }

    @Override
    public <U> Try<U> recoverWith(Function<? super Exception, ? extends Try<U>> recoverFunction) {
        try {
            return recoverFunction.apply(exception);
        } catch (Exception e) {
            return new Failure<>(e);
        }
    }

    @Override
    public Try<Exception> failed() {
        return new Success<>(exception);
    }

    @Override
    public Optional<T> toOptional() {
        return Optional.empty();
    }

    @Override
    public T getOrElse(T defaultValue) {
        return defaultValue;
    }

    @Override
    public Try<T> orElse(Try<T> defaultValue) {
        return defaultValue;
    }

    @Override
    public <U> Try<U> transform(Function<? super T, ? extends Try<U>> successFunc,
                                Function<Exception, ? extends Try<U>> failureFunc) {
        return failureFunc.apply(exception);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Failure failure = (Failure) o;

        if (!exception.equals(failure.exception)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return exception.hashCode();
    }

    @Override
    public String toString() {
        return "Failure{" +
                "exception=" + exception +
                '}';
    }
}
