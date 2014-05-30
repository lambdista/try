package com.alessandrolacava.java.util;

import java.io.IOException;

/**
 * Created by alacava on 5/29/14.
 */
public class Test {

    public static void main(String[] args) throws Exception {

        Try<Integer> result = Try.apply(
                () -> test()
        );
//        System.out.println(result.map((Integer a) -> a.toString() + " hello world!").get());
        System.out.println(result.getOrElse(69));
    }

    public static Integer test() throws IOException {
        throw new IOException("antani");
    }
}
