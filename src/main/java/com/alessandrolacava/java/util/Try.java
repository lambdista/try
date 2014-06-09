package com.alessandrolacava.java.util;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by alacava on 5/29/14.
 */
public abstract class Try<T> {

    private Try() {}

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

    /**
     * Created by alacava on 5/29/14.
     */
    public static final class Success<T> extends Try<T> {

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
                    return new Failure<>(new NoSuchElementException("Predicate does not hold for " + value));
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

    public static final class Failure<T> extends Try<T> {

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
}
