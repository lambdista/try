package com.alessandrolacava.java.util;

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

    public abstract <U> Try<U> map(Function<? super T, ? extends U> mapper);

    public abstract <U> Try<U> flatMap(Function<? super T, ? extends Try<U>> mapper);

    public abstract Try<T> filter(Predicate<? super T> predicate);

    public abstract <U> Try<U> recover(Function<? super Exception, ? extends U> recoverFunction);

    public abstract <U> Try<U> recoverWith(Function<? super Exception, ? extends Try<U>> recoverFunction);

    public abstract Try<Exception> failed();

    public abstract Optional<T> toOptional();

    /* TODO:
    withFilter
    flatten

     */

    public abstract T getOrElse(T defaultValue);

    public abstract Try<T> orElse(Try<T> defaultValue);

    public abstract <U> Try<U> transform(Function<? super T, ? extends Try<U>> successFunc,
                                                   Function<Exception, ? extends Try<U>> failureFunc);

    public static <T> Try<T> apply(FailableSupplier<T> supplier) {
        try {
            return new Success<>(supplier.get());
        } catch (Exception e) {
            return new Failure<>(e);
        }
    }
}
