package org.typesafely.java.util;

/**
 * This class is semantically the same as the {@link java.util.function.Supplier} class apart from the fact that
 * its {@link FailableSupplier#get()} method may throw an {@link java.lang.Exception}
 *
 * @author Alessandro Lacava
 * @since 2014-06-20
 */
@FunctionalInterface
public interface FailableSupplier<T> {

    /**
     *
     * @return a value of type {@code T}
     * @throws Exception
     */
    public T get() throws Exception;
}
