package com.alessandrolacava.java.util;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
/**
 * <p>The {@code Try} type represents a computation that may fail. If the computation is successful returns
 * the value wrapped in a {@link com.alessandrolacava.java.util.Try.Success} otherwise returns the
 * {@link java.lang.Exception} wrapped in a {@link com.alessandrolacava.java.util.Try.Failure}.</p>
 *
 * <p>To use {@code Try} you need to call the {@link Try#apply(FailableSupplier)} method passing in a lambda with
 * the same signature used for a common {@link java.util.function.Supplier}.
 * Indeed {@link com.alessandrolacava.java.util.FailableSupplier} is just a {@link java.util.function.Supplier} with a
 * {@code 'throws Exception'} added to its {@code 'get'} method.</p>
 *
 * <p>For example, {@code Try} can be used to perform division on a user-defined input, without the need to do explicit
 * exception-handling in all of the places that an exception might occur.</p>
 *
 * <p>An important property of {@code Try} shown in the {@link com.alessandrolacava.java.test.MainExample#divide()} method is its ability
 * to <i>pipeline (chain if you prefer)</i>  operations,
 * catching exceptions along the way thanks to its {@link Try#flatMap(java.util.function.Function)} method. If you
 * are not a seasoned functional programming geek concepts such as {@code flatMap/map} might not be easy to grasp
 * at first. However you'll get used to them and, in the end, you'll love them. Moreover you're going to encounter
 * these methods more and more often since some important Java 8 classes already implement them
 * (e.g. {@link java.util.Optional} and {@link java.util.stream.Stream}. Anyway for the moment just take for
 * granted that to pipeline more than two operations, say N, you just need to chain them by using N - 1
 * {@code flatMap} calls and a last call to {@code map}. E.g.: Suppose you have 3 variables (x, y and z) being
 * of type {@code Try<Integer>} and you just wanto to sum them up. The code you need for doing that is the
 * following:</p>
 *
 * <pre>
 * x.flatMap(a -> y.flatMap(b -> z.map(c -> a + b + c)))
 * </pre>
 *
 * Apart from {@code map} and {@code flatMap}, {@code Try} has many other useful methods. See the {@code TryTest}
 * test class for a thorough coverage of all {@code Try}'s methods.
 *
 * @author Alessandro Lacava
 * @since 2014-06-20
 * @param <T> the type returned by the computation
 */
public abstract class Try<T> {

    /**
     * Ensures that the only possible instances of this class
     * are either {@link Try.Success} or {@link Try.Failure}
     */
    private Try() {
    }

    /**
     * @return {@code true} if the {@code Try} is a {@code Success}, {@code false} if it's a {@code Failure}
     */
    public abstract boolean isSuccess();

    /**
     * @return {@code true} if the {@code Try} is a {@code Failure}, {@code false} if it's a {@code Success}
     */
    public abstract boolean isFailure();

    /**
     * @return the value from this {@code Success} or throws the exception if this is a {@code Failure}
     * @throws Exception
     */
    public abstract T get() throws Exception;

    /**
     * Calls {@link Consumer#accept(T)} passing the value to it if this is a {@link Try.Success} otherwise it takes
     * no action if it is a {@link Try.Failure}
     *
     * @param action the {@link Consumer} to use
     */
    public abstract void forEach(Consumer<? super T> action);

    /**
     * Maps the value of type {@code T} to the value of type {@code U}
     * by applying the {@code mapper} function to it if this is a {@link Try.Success} otherwise it takes no action
     * if this is a {@link Try.Failure}
     *
     * @param mapper a function to apply to the value of type {@code T}
     * @param <U>    The type of the result
     * @return the result of applying {@code mapper} wrapped in a {@code Try} if it's a {@link Try.Success} or
     * {@code this} if it's a  {@link Try.Failure}
     */
    public abstract <U> Try<U> map(Function<? super T, ? extends U> mapper);

    /**
     * Maps the value of type {@code T} to the value of type {@code Try<U>}
     * by applying the {@code mapper} function to it if this is a {@link Try.Success} otherwise it takes no action
     * if this is a {@link Try.Failure}
     *
     * @param mapper a function to apply to the value which produces a {@code Try}
     *               of a new value
     * @param <U>    The type of the result
     * @return the result of applying {@code mapper} if it's a {@link Try.Success} or
     * {@code this} if it's a  {@link Try.Failure}
     */
    public abstract <U> Try<U> flatMap(Function<? super T, ? extends Try<U>> mapper);

    /**
     * Converts this to a {@link Failure} if the predicate is not satisfied.
     *
     * @param predicate the {@link Predicate} to use
     * @return
     */
    public abstract Try<T> filter(Predicate<? super T> predicate);

    /**
     * Applies the given function {@code recoverFunc} if this is a {@link Failure}, otherwise returns this if this is a {@link Success}.
     * This is like {@link #map(Function)} for the exception.
     *
     * @param recoverFunc
     * @param <U>
     * @return
     */
    public abstract <U> Try<U> recover(Function<? super Exception, ? extends U> recoverFunc);

    /**
     * Applies the given function {@code recoverFunc} if this is a {@link Failure}, otherwise returns this if this is a {@link Success}.
     * This is like {@link #flatMap(Function)} for the exception.
     *
     * @param recoverFunc
     * @param <U>
     * @return
     */
    public abstract <U> Try<U> recoverWith(Function<? super Exception, ? extends Try<U>> recoverFunc);

    /**
     * Completes this {@code Try} with an exception wrapped in a {@link Success}. The exception is either the exception that the
     * {@code Try} failed with (if a {@link Failure}) or an {@link java.lang.UnsupportedOperationException}.
     *
     * @return
     */
    public abstract Try<Exception> failed();

    /**
     * Returns {@link Optional#empty()} if this is a {@link Failure} or a {@link Optional#of(T)} )}
     * containing the value if this is a {@link Success}.
     *
     * @return
     */
    public abstract Optional<T> toOptional();

    /* TODO:
    withFilter
    flatten

     */

    /**
     * Returns the value from this {@link Success} or the given {@code defaultValue} argument if this is a {@link Failure}.
     *
     * @param defaultValue
     * @return
     */
    public abstract T getOrElse(T defaultValue);

    /**
     * Returns this {@code Try} if it's a {@link Success} or the given {@code defaultValue} argument if this is a {@link Failure}.
     *
     * @param defaultValue
     * @return
     */
    public abstract Try<T> orElse(Try<T> defaultValue);

    /**
     * Completes this {@code Try} by applying the function {@code failureFunc} to this if it is of type {@link Failure},
     * or the function {@code successFunc} if this is a {@link Success}.
     *
     * @param successFunc
     * @param failureFunc
     * @param <U>
     * @return
     */
    public abstract <U> Try<U> transform(Function<? super T, ? extends Try<U>> successFunc,
                                         Function<Exception, ? extends Try<U>> failureFunc);

    /**
     * Constructs a {@code Try} using the {@link FailableSupplier} parameter. This
     * method will ensure any non-fatal exception is caught and a {@link Failure} object is returned.
     *
     * @param supplier the {@link FailableSupplier} to use
     * @param <T> the type returned by the {@link FailableSupplier}
     * @return a {@code Try} object (an instance of either {@link Try.Success} or {@link Try.Failure}
     */
    public static <T> Try<T> apply(FailableSupplier<T> supplier) {
        try {
            return new Success<>(supplier.get());
        } catch (Exception e) {
            return new Failure<>(e);
        }
    }


    /**
     * Represents the successful result of a computation
     *
     * @author Alessandro Lacava
     * @since 2014-06-20
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
                if (predicate.test(value)) {
                    return this;
                } else {
                    return new Failure<>(new NoSuchElementException("Predicate does not hold for " + value));
                }
            } catch (Exception e) {
                return new Failure<>(e);
            }
        }

        @Override
        public <U> Try<U> recover(Function<? super Exception, ? extends U> recoverFunc) {
            return (Try<U>) this;
        }

        @Override
        public <U> Try<U> recoverWith(Function<? super Exception, ? extends Try<U>> recoverFunc) {
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

    /**
     * Represents the failed result of a computation
     *
     * @author Alessandro Lacava
     * @since 2014-06-20
     */
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
        public <U> Try<U> recover(Function<? super Exception, ? extends U> recoverFunc) {
            try {
                return Try.apply(() -> recoverFunc.apply(exception));
            } catch (Exception e) {
                return new Failure<>(e);
            }
        }

        @Override
        public <U> Try<U> recoverWith(Function<? super Exception, ? extends Try<U>> recoverFunc) {
            try {
                return recoverFunc.apply(exception);
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
