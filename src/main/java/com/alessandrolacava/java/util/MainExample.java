package com.alessandrolacava.java.util;

import java.io.IOException;
import java.util.Scanner;

/**
 * A simple client example of the {@code Try-Success-Failure} API
 *
 * @author Alessandro Lacava
 * @since 2014-06-20
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

        divide();

        Try<Integer> x = Try.apply(() -> 3);
        Try<Integer> y = Try.apply(() -> 6);
        Try<Integer> z = Try.apply(() -> 9);

        Try<Integer> res = x.flatMap(a -> y.flatMap(b -> z.map(c -> a + b + c)));
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

    /**
     * An important property of {@code Try} shown in the {@code divide()} example is its ability
     * to <i>pipeline (chain if you prefer)</i>  operations,
     * catching exceptions along the way thanks to its {@link Try#flatMap(java.util.function.Function)} method. If you
     * are not a seasoned functional programming geek concepts such as {@code flatMap/map} might not be easy to grasp
     * at first. However you'll get used to them and, in the end, you'll love them. Moreover you're going to encounter
     * these methods more and more often since some important Java 8 classes already implement them
     * (e.g. {@link java.util.Optional} and {@link java.util.stream.Stream}. Anyway for the moment just take for
     * granted that to pipeline more than two operations, say N, you just need to chain them by using N - 1
     * {@code flatMap} calls and a last call to {@code map}. E.g.: Suppose you have 3 variables (x, y and z) being
     * of type {@code Try<Integer>} and you just wanto to sum them up. The code you need for doing that is the
     * following:
     *
     * x.flatMap(a -> y.flatMap(b -> z.map(c -> a + b + c)))
     *
     * Apart from {@code map} and {@code flatMap}, {@code Try} has many other useful methods. See the {@code TryTest}
     * test class for a thorough coverage of all {@code Try}'s methods.
     *
     */
    public static void divide() {
        System.out.println("Integer division");
        System.out.println("Enter the dividend press Return and then enter the divisor: ");
        Scanner dividend = new Scanner(System.in);
        Scanner divisor = new Scanner(System.in);

        Try<Integer> num = Try.apply(dividend::nextInt);
        Try<Integer> denom = Try.apply(divisor::nextInt);

        Try<Integer> result = num.flatMap(x -> denom.map(y -> x / y));
        Try<String> resultTryStr = result.map(i -> "The result of division is: " + i);
        String resultStr = resultTryStr.getOrElse("You must've divided by zero or entered something that's not an Int. Try again!");
        System.out.println(resultStr);
    }

}
