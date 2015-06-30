/**
 * Copyright 2014 Alessandro Lacava
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.lambdista.example;

import java.util.Arrays;

import com.lambdista.util.Try;
import com.lambdista.util.TryBinaryOperator;

/**
 * Parse and sum up integers
 *
 * @author Bernhard Frauendienst
 */
public class ParseAndSum {

    public static void main(String[] args) {

        System.out.println("Sum using the try-catch block");
        int sum1 = sumWithoutTry(args);
        System.out.println("Result: " + sum1);

        System.out.println("Sum using the Try-Success-Failure API");
        int sum2 = sumWithTry(args);
        System.out.println("Result: " + sum2);
    }

    public static int sumWithoutTry(String[] args) {
        int sum = 0;
        for (String arg : args) {
            try {
                int number = Math.abs(Integer.parseInt(arg));
                sum += number;
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return sum;
    }

    public static int sumWithTry(String[] args) {
        return Arrays.stream(args).map(line -> Try.apply(() -> Math.abs(Integer.parseInt(line))))
                .reduce(Try.apply(() -> 0), TryBinaryOperator.of(Integer::sum)).getOrElse(-1);
    }

}
