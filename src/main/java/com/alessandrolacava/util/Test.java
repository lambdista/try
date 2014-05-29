package com.alessandrolacava.util;

import java.io.IOException;
import java.util.stream.Stream;

/**
 * Created by alacava on 5/29/14.
 */
public class Test {

    public static void main(String[] args) throws Throwable {

        Try<Integer> result = Try.apply(
                () -> test()
        );
        System.out.println(result.getOrElse(24));
    }

    public static Integer test() throws IOException {
        return 42;
    }
}
