/**
 * Copyright 2015 Bernhard Frauendienst
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
package com.lambdista.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.Test;

/**
 * Unit test for the {@link TryBinaryOperator} API.
 *
 * @author Bernhard Frauendienst
 */
public class TryBinaryOperatorTest {

    @Test
    public void testIntegerMinSuccess() {
        Optional<Try<Integer>> optResult = successNumbers().reduce(TryBinaryOperator.minBy(Comparator.naturalOrder()));
        assertTrue("result must have a value", optResult.isPresent());
        Try<Integer> result = optResult.get();
        assertTrue("result must be a success", result.isSuccess());
        assertEquals(Integer.valueOf(2), result.get());
    }

    @Test
    public void testIntegerMaxSuccess() {
        Optional<Try<Integer>> optResult = successNumbers().reduce(TryBinaryOperator.maxBy(Comparator.naturalOrder()));
        assertTrue("result must have a value", optResult.isPresent());
        Try<Integer> result = optResult.get();
        assertTrue("result must be a success", result.isSuccess());
        assertEquals(Integer.valueOf(195), result.get());
    }

    @Test
    public void testIntegerSumSuccess() {
        Optional<Try<Integer>> optResult = successNumbers().reduce(TryBinaryOperator.of(Integer::sum));
        assertTrue("result must have a value", optResult.isPresent());
        Try<Integer> result = optResult.get();
        assertTrue("result must be a success", result.isSuccess());
        assertEquals(Integer.valueOf(283), result.get());
    }

    @Test
    public void testIntegerMinFailure() {
        Optional<Try<Integer>> optResult = mixedSuccessFailure()
                .reduce(TryBinaryOperator.minBy(Comparator.naturalOrder()));
        assertTrue("result must have a value", optResult.isPresent());
        Try<Integer> result = optResult.get();
        assertTrue("result must be a failure", result.isFailure());
    }

    @Test
    public void testCustomComparatorSuccess() {
        Optional<Try<String>> optResult = strings().reduce(TryBinaryOperator.minBy(compareCharAt(4)));
        assertTrue("result must have a value", optResult.isPresent());
        Try<String> result = optResult.get();
        assertTrue("result must be a success", result.isSuccess());
        assertEquals("foobar", result.get());
    }

    @Test
    public void testCustomComparatorFailure() {
        // "dummy" is shorted than 6 characters, the comparator will throw a StringIndexOutOfBoundsException
        Optional<Try<String>> optResult = strings().reduce(TryBinaryOperator.minBy(compareCharAt(5)));
        assertTrue("result must have a value", optResult.isPresent());
        Try<String> result = optResult.get();
        assertTrue("result must be a failure", result.isFailure());
        assertTrue("exception must be a StringIndexOutOfBoundsException",
                result.failed().get() instanceof StringIndexOutOfBoundsException);
    }

    /* helper methods */

    private Stream<Try<String>> strings() {
        return Arrays.asList("abcdefgh", "foobar", "dummy", "sample").stream().map(this::success);
    }

    private Stream<Try<Integer>> successNumbers() {
        return Arrays.asList(42, 5, 23, 16, 195, 2).stream().map(this::success);
    }

    private Stream<Try<Integer>> mixedSuccessFailure() {
        return Arrays.asList(success(1), success(2), failure(), success(3)).stream();
    }

    private Comparator<String> compareCharAt(int index) {
        return (a, b) -> Character.compare(a.charAt(index), b.charAt(index));
    }

    private <T> Try<T> success(T o) {
        return Try.apply(() -> o);
    }

    private Try<Integer> failure() {
        return Try.apply(() -> {
            throw new NumberFormatException("Number not valid");
        });
    }
}
