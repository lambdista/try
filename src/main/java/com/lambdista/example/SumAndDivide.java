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
package com.lambdista.example;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.google.common.base.Function;
import com.lambdista.util.Consumer;
import com.lambdista.util.FailableSupplier;
import com.lambdista.util.Try;

/**
 * Sum and divide example
 *
 * @author Alessandro Lacava
 * @since 2014-06-20
 */
public class SumAndDivide {

    public static void main(String[] args) {

        System.out.println("Sum using the try-catch block");
        sumWithoutTry("1", "2", "3");

        System.out.println("Sum using the Try-Success-Failure API");
        sumWithTry("1", "2", "3");

        System.out.println("Integer division using the try-catch block");
        divideWithoutTry();

        System.out.println("Integer division using the Try-Success-Failure API");
        divideWithTry();
    }

    public static void sumWithoutTry(String first, String second, String third) {

        int x, y, z;
        try {
            x = Integer.parseInt(first);
            y = Integer.parseInt(second);
            z = Integer.parseInt(third);
            System.out.println("The sum is: " + (x + y + z));
        } catch (NumberFormatException e) {
        }
    }

    public static void sumWithTry(final String first, final String second, final String third) {

        Try<Integer> x = Try.apply(
                new FailableSupplier<Integer>() {
                    @Override
                    public Integer get() throws Exception {
                        return Integer.parseInt(first);
                    }
                }
        );
        final Try<Integer> y = Try.apply(
                new FailableSupplier<Integer>() {
                    @Override
                    public Integer get() throws Exception {
                        return Integer.parseInt(second);
                    }
                }

        );
        final Try<Integer> z = Try.apply(
                new FailableSupplier<Integer>() {
                    @Override
                    public Integer get() throws Exception {
                        return Integer.parseInt(third);
                    }
                }
        );

        Try<Integer> res = x.flatMap(
                new Function<Integer, Try<Integer>>() {
                    @Override
                    public Try<Integer> apply(final Integer a) {
                        return y.flatMap(
                                new Function<Integer, Try<Integer>>() {
                                    @Override
                                    public Try<Integer> apply(final Integer b) {
                                        return z.map(
                                                new Function<Integer, Integer>() {
                                                    @Override
                                                    public Integer apply(Integer c) {
                                                        return a + b + c;
                                                    }
                                                }
                                        );
                                    }
                                }
                        );
                    }
                }
        );

        res.forEach(
                new Consumer<Integer>() {
                    @Override
                    public void accept(Integer sum) {
                        System.out.println("The sum is: " + sum);
                    }
                }
        );
    }


    public static void divideWithoutTry() {

        System.out.println("Enter the dividend press Return and then enter the divisor: ");
        Scanner dividend = new Scanner(System.in);
        Scanner divisor = new Scanner(System.in);

        String res;
        try {
            res = "The quotient is: " + (dividend.nextInt() / divisor.nextInt());
        } catch (InputMismatchException | ArithmeticException e) {
            res = "The integers you entered are not valid or the divisor is zero.";
        }

        System.out.println(res);
    }

    public static void divideWithTry() {

        System.out.println("Enter the dividend press Return and then enter the divisor: ");
        final Scanner dividend = new Scanner(System.in);
        final Scanner divisor = new Scanner(System.in);

        String res = Try.apply(
                new FailableSupplier<Integer>() {
                    @Override
                    public Integer get() throws Exception {
                        return dividend.nextInt() / divisor.nextInt();
                    }
                }
        ).map(
                new Function<Integer, String>() {
                    @Override
                    public String apply(Integer quotient) {
                        return "The quotient is: " + quotient;
                    }
                }
        ).getOrElse("The integers you entered are not valid or the divisor is zero.");

        System.out.println(res);
    }

}