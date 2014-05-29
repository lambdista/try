package com.alessandrolacava.util;

import java.io.IOException;

/**
 * Created by alacava on 5/29/14.
 */
public class Test {

    public static void main(String[] args) throws Exception {

        Try<Integer> result = Try.apply(
                () -> test()
        );
        System.out.println(result.map((Integer a) -> a.toString() + " hello world!").get());
    }

    public static Integer test() throws IOException {
        return 42;
    }
}
