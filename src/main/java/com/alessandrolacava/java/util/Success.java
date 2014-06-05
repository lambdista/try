package com.alessandrolacava.java.util;

import java.util.NoSuchElementException;
import java.util.Optional;
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
        action.accept(value);
    }

    @Override
    public <U> Try<U> map(Function<? super T, ? extends U> mapper) {
        return Try.apply(() -> mapper.apply(value));
    }

    @Override
    public <U> Try<U> flatMap(Function<? super T, ? extends Try<U>> mapper) {
        try {
            return mapper.apply(value);
        } catch (Exception e) {
            return new Failure<>(e);
        }
    }

    @Override
    public Try<T> filter(Predicate<? super T> predicate) {
        try {
            if(predicate.test(value)) {
                return this;
            } else {
                return new Failure<T>(new NoSuchElementException("Predicate does not hold for " + value));
            }
        } catch (Exception e) {
            return new Failure<>(e);
        }
    }

    @Override
    public <U> Try<U> recover(Function<? super Exception, ? extends U> recoverFunction) {
        return (Try<U>) this;
    }

    @Override
    public <U> Try<U> recoverWith(Function<? super Exception, ? extends Try<U>> recoverFunction) {
        return (Try<U>) this;
    }

    @Override
    public Try<Exception> failed() {
        return new Failure<>(new UnsupportedOperationException("Success.failed"));
    }

    @Override
    public Optional<T> toOptional() {
        return Optional.of(value);
    }

    @Override
    public T getOrElse(T defaultValue) {
        return value;
    }

    @Override
    public Try<T> orElse(Try<T> defaultValue) {
        return this;
    }

    @Override
    public <U> Try<U> transform(Function<? super T, ? extends Try<U>> successFunc,
                                Function<Exception, ? extends Try<U>> failureFunc) {
        return successFunc.apply(value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Success success = (Success) o;

        return value.equals(success.value);

    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public String toString() {
        return "Success{" +
                "value=" + value +
                '}';
    }
}
