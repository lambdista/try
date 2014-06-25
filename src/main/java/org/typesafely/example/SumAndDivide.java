/**
 * Copyright 2014 Alessandro Lacava
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.typesafely.example;

import org.typesafely.util.Try;

import java.util.Scanner;

/**
 * Sum and divide example
 *
 * @author Alessandro Lacava
 * @since 2014-06-20
 */
public class SumAndDivide {

    public static void main(String[] args) {

        sum();

        divide();
    }

    public static void sum() {

        Try<Integer> x = Try.apply(() -> 3);
        Try<Integer> y = Try.apply(() -> 6);
        Try<Integer> z = Try.apply(() -> 9);

        Try<Integer> res = x.flatMap(a -> y.flatMap(b -> z.map(c -> a + b + c)));

        res.forEach(sum -> System.out.println("The sum is: " + sum));
    }

    /**
     * An important property of {@code Try} shown in the {@code divide()} example is its ability
     * to <i>pipeline</i> (<i>chain</i> if you prefer)  operations,
     * catching exceptions along the way thanks to its {@code flatMap} method. If you
     * are not a seasoned functional programming geek concepts such as {@code flatMap/map} might not be easy to grasp
     * at first. However you'll get used to them and, in the end, you'll love them. Moreover you're going to encounter
     * these methods more and more often since some important Java 8 classes already implement them
     * (e.g. {@link java.util.Optional} and {@link java.util.stream.Stream}. Anyway for the moment just take for
     * granted that to pipeline more than two operations, say N, you just need to chain them by using N - 1
     * {@code flatMap} calls and a last call to {@code map}. E.g.: Suppose you have 3 variables (x, y and z) being
     * of type {@code Try<Integer>} and you just wanto to sum them up. The code you need for doing that is the
     * following:
     * <p/>
     * x.flatMap(a -> y.flatMap(b -> z.map(c -> a + b + c)))
     * <p/>
     * Apart from {@code map} and {@code flatMap}, {@code Try} has many other useful methods. See the {@code TryTest}
     * test class for a thorough coverage of all {@code Try}'s methods.
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
        String resultStr = resultTryStr.getOrElse("The integers you entered are not valid or the divisor is zero.");
        System.out.println(resultStr);
    }

}