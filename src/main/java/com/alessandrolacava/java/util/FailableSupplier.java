package com.alessandrolacava.java.util;

/**
 * Created by alacava on 5/29/14.
 */
public interface FailableSupplier<T> {

    public T get() throws Exception;
}
