package com.alessandrolacava.java.util;

import java.io.IOException;

/**
 * Created by alacava on 5/29/14.
 */
public class Test {

    public static void main(String[] args) throws Exception {

        Try<Integer> result = Try.apply(
                () -> {
                    int res0 = 42;
                    int res1 = test1();
                    int res2 = test2();
                    return res0 + res1 + res2;
                }
        );
        // fail reporting the first Exception it encounters
        //result.get();
        System.out.println(result.getOrElse(42));

        Try<Integer> res1 = Try.apply(Test::test1);
        Try<Integer> res2 = Try.apply(Test::test2);
        System.out.println("flatMap: " + res1.flatMap(i -> res2.map(j -> i + j)).getOrElse(42));
    }

    public static Integer test1() throws IOException {
        throw new IOException("File not found");
    }

    public static Integer test2() throws IllegalArgumentException {
        throw new IllegalArgumentException("param not valid");
    }
}
