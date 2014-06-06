package com.alessandrolacava.java.util;

import java.io.IOException;

/**
 * Created by alacava on 5/29/14.
 */
public class MainExample {

    public static void main(String[] args) {

        Try<String> result = Try.apply(
                () -> success1() + success2()
        );
        result.forEach(
                s -> System.out.println("result against a success: " + s)
        );

        Try<Integer> result1 = Try.apply(MainExample::failure1);
        Try<Integer> result2 = Try.apply(MainExample::failure2);
        System.out.println("flatMap against failures: " + result1.flatMap(i -> result2.map(j -> i + j)).getOrElse(42));

        Try<Integer> resultOfFailure = Try.apply(
                () -> {
                    int res0 = 1;
                    int res1 = failure1();
                    int res2 = failure2();
                    return res0 + res1 + res2;
                }
        );
        System.out.println("getOrElse against failures: " + resultOfFailure.getOrElse(42));
    }

    public static Integer failure1() throws IOException {
        throw new IOException("File not found");
    }

    public static Integer failure2() throws IllegalArgumentException {
        throw new IllegalArgumentException("param not valid");
    }

    public static String success1() {
        return "Hello ";
    }

    public static String success2() {
        return "World!";
    }

}
