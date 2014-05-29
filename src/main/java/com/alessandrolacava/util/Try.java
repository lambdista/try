package com.alessandrolacava.util;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by alacava on 5/29/14.
 */
public abstract class Try<T> {

    public abstract boolean isSuccess();

    public abstract boolean isFailure();

    public abstract T get() throws Exception;

    public abstract void forEach(Consumer<? super T> action);

    public abstract <U> Try<? extends U> map(Function<? super T, ? extends U> mapper);

    public abstract <U> Try<? extends U> flatMap(Function<? super T, ? extends Try<? extends U>> mapper);

    public abstract Try<T> filter(Predicate<? super T> predicate);

    public abstract <U> Try<? extends U> recover(Function<Exception, ? extends U> recoverFunction);

    public abstract <U> Try<? extends U> recoverWith(Function<Exception, Try<? extends U>> recoverFunction);

    public abstract Try<Exception> failed();

    public Optional<T> toOptional() {
        Optional<T> result = Optional.empty();
        // TODO: Avoid try catch if possible
        try {
            if (isSuccess()) {
                result = Optional.of(get());
            }
        } catch (Exception exception) {
        }
        return result;
    }

    /* TODO:
    withFilter
    flatten

     */

    public T getOrElse(T defaultValue) {
        T result = defaultValue;
        // TODO: Avoid try catch if possible
        try {
            if (isSuccess())
                result = get();
        } catch (Exception exception) {
        }
        return result;
    }

    public Try<? super T> orElse(Try<? super T> defaultValue) {
        if (isSuccess())
            return this;
        else
            return defaultValue;
    }

    public <U> Try<? extends U> transform(Function<? super T, Try<? extends U>> g,
                                Function<Exception, Try<? extends  U>> f) {
        Try<? extends U> result = null;
        if (this instanceof Success) {
            Success<T> success = (Success<T>) this;
            result = g.apply(success.get());
        } else if(this instanceof Failure) {
            Failure<T> failure = (Failure<T>) this;
            try {
                failure.get();
            } catch(Exception t) {
                result = f.apply(t);
            }
        } else {
            throw new RuntimeException("Algebraic data type (Try-Success-Failure) violated");
        }
        return result;
    }

    public static <T> Try<T> apply(FailableSupplier<T> supplier) {
        try {
            return new Success<T>(supplier.get());
        } catch (Exception e) {
            return new Failure<T>(e);
        }
    }
}
