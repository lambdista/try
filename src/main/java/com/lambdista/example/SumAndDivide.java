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
        } catch (NumberFormatException e) {}
    }

    public static void sumWithTry(String first, String second, String third) {

        Try<Integer> x = Try.apply(() -> Integer.parseInt(first));
        Try<Integer> y = Try.apply(() -> Integer.parseInt(second));
        Try<Integer> z = Try.apply(() -> Integer.parseInt(third));

        Try<Integer> res = x.flatMap(a -> y.flatMap(b -> z.map(c -> a + b + c)));

        /* Note that this example is implemented this way just to show you the Try's chaining peculiarity.
           Of course if the sum was what you just needed then you could have obtained it much more easily as follows:

           Try<Integer> res = Try.apply(() ->
                Integer.parseInt(first) + Integer.parseInt(second) + Integer.parseInt(third)
           );

         */

        res.forEach(sum -> System.out.println("The sum is: " + sum));
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
        Scanner dividend = new Scanner(System.in);
        Scanner divisor = new Scanner(System.in);

        String res = Try.apply(() -> dividend.nextInt() / divisor.nextInt())
                .map(quotient -> "The quotient is: " + quotient)
                .getOrElse("The integers you entered are not valid or the divisor is zero.");

        System.out.println(res);
    }

}