package com.lambdista.util;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.BinaryOperator;

import com.lambdista.util.Try.Failure;
import com.lambdista.util.Try.Success;

/**
 * A {@link BinaryOperator} which simplifies working with {@link Try} values, by
 * returning a {@link Failure} if one of the values is a failure, and passing
 * the wrapped values to another {@link BinaryOperator} otherwise.
 * 
 */
@FunctionalInterface
public interface TryBinaryOperator<T> extends BinaryOperator<Try<T>> {

  /**
   * Returns a {@link TryBinaryOperator} which returns either a {@link Failure},
   * if either of the both arguments is non-successful, or passes the wrapped
   * values to the specified {@link BinaryOperator}
   * 
   * @param <T> the type of the input arguments of the binaryOperator
   * @param binaryOperator a {@link BinaryOperator} for operating on the wrapped
   *          values if both represent a {@link Success}
   * @return a {@link TryBinaryOperator} which returns a {@link Failure} if
   *         either of its arguments is a failure, or the result of the binary
   *         operation done by the passed {@link BinaryOperator}
   * @throws NullPointerException if the argument is null
   */
  public static <T> TryBinaryOperator<T> of(BinaryOperator<T> binaryOperator) {
    Objects.requireNonNull(binaryOperator);
    return (a, b) -> a.flatMap(x -> b.map(y -> binaryOperator.apply(x, y)));
  }

  /**
   * Returns a {@link TryBinaryOperator} which returns the lesser of two
   * elements according to the specified {@code Comparator}, if both values are
   * a {@link Success}.
   *
   * @param <T> the type of the input arguments of the comparator
   * @param comparator a {@code Comparator} for comparing the two values
   * @return a {@code TryBinaryOperator} which returns the lesser of its
   *         operands, according to the supplied {@code Comparator}, or the
   *         first {@link Failure} if one is encountered.
   * @throws NullPointerException if the argument is null
   */
  public static <T> TryBinaryOperator<T> minBy(Comparator<? super T> comparator) {
    return TryBinaryOperator.of(BinaryOperator.minBy(comparator));
  }

  /**
   * Returns a {@link BinaryOperator} which returns the greater of two elements
   * according to the specified {@code Comparator}, if both values are a
   * {@link Success}
   *
   * @param <T> the type of the input arguments of the comparator
   * @param comparator a {@code Comparator} for comparing the two values
   * @return a {@code TryBinaryOperator} which returns the greater of its
   *         operands, according to the supplied {@code Comparator}, or the
   *         first {@link Failure} if one is encountered
   * @throws NullPointerException if the argument is null
   */
  public static <T> TryBinaryOperator<T> maxBy(Comparator<? super T> comparator) {
    return TryBinaryOperator.of(BinaryOperator.maxBy(comparator));
  }

}
