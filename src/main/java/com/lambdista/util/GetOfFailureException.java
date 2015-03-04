package com.lambdista.util;

/**
 * Custom unchecked exception to signal the invocation of the get method on a {@link com.lambdista.util.Try.Failure}
 * object.
 *
 * @author Alessandro Lacava
 * @since 2015-03-04
 */
public class GetOfFailureException extends RuntimeException {

    private static final String message = "get of a Failure object";

    public GetOfFailureException(Throwable cause) {
        super(message, cause);
    }

}
