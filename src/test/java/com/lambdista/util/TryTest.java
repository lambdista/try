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
package com.lambdista.util;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for the {@code Try-Success-Failure} API. It covers each and every method of
 * {@link com.lambdista.util.Try} in case of both success and failure
 *
 * @author Alessandro Lacava
 * @since 2014-06-20
 */
public class TryTest {
	
	private Closeable closeableMock;
	private InputStream inputStreamMock;

	@Before
	public void setup(){
		this.closeableMock = mock(Closeable.class);
		this.inputStreamMock = mock(InputStream.class);
	}
	
    @Test
    public void testIsSuccess() {
        Try<Integer> result = Try.apply(
                this::success
        );
        assertTrue("result must be a success", result.isSuccess());
    }

    @Test
    public void testIsFailure() {
        Try<Integer> result = Try.apply(
                this::failure
        );
        assertTrue("result must be a failure", result.isFailure());
    }

    @Test
    public void testGetAgainstASuccess() throws Exception {
        Try<Integer> result = Try.apply(
                this::success
        );
        int intResult = result.get();
        assertEquals("intResult must be 42", intResult, 42);
    }

    @Test(expected = GetOfFailureException.class)
    public void testGetAgainstAFailure()  {
        Try<Integer> result = Try.apply(
                this::failure
        );
        result.get();
    }

    @Test
    public void testUncheckedGetAgainstASuccess() throws Throwable {
        Try<Integer> result = Try.apply(
                this::success
        );
        int intResult = result.checkedGet();
        assertEquals("intResult must be 42", intResult, 42);
    }

    @Test(expected = NumberFormatException.class)
    public void testCheckedGetAgainstAFailure() throws Throwable {
        Try<Integer> result = Try.apply(
                this::failure
        );
        result.checkedGet();
    }

    @Test
    public void testForEachAgainstASuccess() {
        Try<Integer> result = Try.apply(
                this::success
        );
        result.forEach(
                i -> assertEquals("i must be 42", (int) i, 42)
        );
    }

    @Test(expected = GetOfFailureException.class)
    public void testForEachAgainstAFailure() throws Throwable {
        Try<Integer> result = Try.apply(
                this::failure
        );
        result.forEach(
                i -> System.out.println("Since it's a failure it does not even get here. As a matter of fact this won't be printed")
        );

        // Conversely, calling get must throw the GetOfFailureException captured in result
        result.get();
    }

    @Test
    public void testMapAgainstASuccess() {
        Try<Integer> result = Try.apply(
                this::success
        );
        Try<String> mappedResult = result.map(
                i -> i.toString() + ", Hello World!"
        );
        assertEquals("mappedResult must be Success(\"42, Hello World!\")", mappedResult, new Try.Success<>("42, Hello World!"));

    }

    @Test(expected = GetOfFailureException.class)
    public void testMapAgainstAFailure() throws Exception {
        Try<Integer> result = Try.apply(
                this::failure
        );
        result.map(
                i -> {
                    String out = i.toString() + ", Hello World!";
                    System.out.println("Since it's a failure it does not even get here. As a matter of fact this won't be printed");
                    return out;
                }
        );

        // Conversely, calling get must throw the GetOfFailureException captured in result
        result.get();
    }

    @Test
    public void testFlatMapAgainstASuccess() {
        Try<Integer> result = Try.apply(
                this::success
        );

        Try<String> flatMappedResult = result.flatMap(
                i -> Try.apply(
                        () -> i + ", " + anotherSuccess()
                )
        );

        assertEquals("flatMappedResult must be Success(\"42, Hello World!\")", flatMappedResult, new Try.Success<>
                ("42, Hello World!"));
    }

    @Test(expected = GetOfFailureException.class)
    public void testFlatMapAgainstAFailure() throws Exception {
        Try<Integer> result = Try.apply(
                this::failure
        );

        Try<String> chainedResult = result.flatMap(
                i -> Try.apply(
                        () -> {
                            String out = i + ", " + anotherSuccess();
                            System.out.println("Since it's a failure it does not even get here. As a matter of fact this won't be printed");
                            return out;
                        }
                )
        );

        // Conversely, calling get must throw the GetOfFailureException captured in result
        chainedResult.get();
    }

    @Test
    public void testFilterAgainstASuccess() {
        Try<Integer> result = Try.apply(
                this::success
        );

        Try<Integer> filteredResult = result.filter(i -> i == 42);
        assertEquals("filteredResult must be Success(42)", filteredResult, new Try.Success<>(42));
    }

    @Test(expected = GetOfFailureException.class)
    public void testFilterAgainstAFailure() throws Exception {
        Try<Integer> result = Try.apply(
                this::failure
        );

        Try<Integer> filteredResult = result.filter(
                i -> {
                    System.out.println("Since it's a failure it does not even get here. As a matter of fact this won't be printed");
                    return i == 42;
                }
        );

        // Conversely, calling get must throw the GetOfFailureException captured in result
        filteredResult.get();
    }

    @Test(expected = GetOfFailureException.class)
    public void testNonMatchingFilter() throws Exception {
        Try<Integer> result = Try.apply(
                this::success
        );

        Try<Integer> filteredResult = result.filter(
                i -> i != 42
        );

        // In this case calling get must throw a GetOfFailureException since the Predicate in filter does not hold
        filteredResult.get();
    }

    @Test
    public void testRecoverAgainstASuccess() {
        Try<Integer> result = Try.apply(
                this::success
        );
        Try<Integer> recoveredResult = result.recover(
                exception -> {
                    if (exception instanceof NumberFormatException) {
                        return 84;
                    } else {
                        return 0;
                    }
                }
        );
        assertEquals("recoveredResult must be Success(42)", recoveredResult, new Try.Success<>(42));
    }

    @Test
    public void testRecoverAgainstAFailure() {
        Try<Integer> result = Try.apply(
                this::failure
        );
        Try<Integer> recoveredResult = result.recover(
                exception -> {
                    if (exception instanceof NumberFormatException) {
                        return 84;
                    } else {
                        return 0;
                    }
                }
        );
        assertEquals("recoveredResult must be Success(84)", recoveredResult, new Try.Success<>(84));
    }

    @Test
    public void testRecoverWithAgainstASuccess() {
        Try<Integer> result = Try.apply(
                this::success
        );
        Try<Integer> recoveredResult = result.recoverWith(
                exception -> {
                    if (exception instanceof NumberFormatException) {
                        return new Try.Success<>(84);
                    } else {
                        return new Try.Success<>(0);
                    }
                }
        );
        assertEquals("recoveredResult must be Success(42)", recoveredResult, new Try.Success<>(42));
    }

    @Test
    public void testRecoverWithAgainstAFailure() {
        Try<Integer> result = Try.apply(
                this::failure
        );
        Try<Integer> recoveredResult = result.recoverWith(
                exception -> {
                    if (exception instanceof NumberFormatException) {
                        return new Try.Success<>(84);
                    } else {
                        return new Try.Success<>(0);
                    }
                }
        );
        assertEquals("recoveredResult must be Success(84)", recoveredResult, new Try.Success<>(84));
    }

    @Test
    public void testFailedAgainstASuccess() {
        Try<Integer> result = Try.apply(
                this::success
        );
        Try<Throwable> failedOnASuccessProducesFailure = result.failed();
        assertEquals("failedOnASuccessProducesFailure is a Failure", failedOnASuccessProducesFailure.isFailure(), true);
    }

    @Test
    public void testFailedAgainstAFailure() {
        Try<Integer> result = Try.apply(
                this::failure
        );
        Try<Throwable> failedOnAFailureProducesSuccess = result.failed();
        assertEquals("failedOnAFailureProducesSuccess is a Success", failedOnAFailureProducesSuccess.isSuccess(), true);
    }

    @Test
    public void testToOptionalAgainstASuccess() {
        Try<Integer> result = Try.apply(
                this::success
        );
        assertEquals("successful result.toOptional() must be Optional.of(42)", result.toOptional(), Optional.of(42));
    }

    @Test
    public void testToOptionalAgainstAFailure() {
        Try<Integer> result = Try.apply(
                this::failure
        );
        assertEquals("failed result.toOptional() must be Optional.empty()", result.toOptional(), Optional.<Integer>empty());
    }

    @Test
    public void testGetOrElseAgainstASuccess() {
        Try<Integer> result = Try.apply(
                this::success
        );
        int out = result.getOrElse(84);
        assertEquals("out must be 42", out, 42);
    }

    @Test
    public void testGetOrElseAgainstAFailure() {
        Try<Integer> result = Try.apply(
                this::failure
        );
        int out = result.getOrElse(84);
        assertEquals("out must be 84", out, 84);
    }

    @Test
    public void testOrElseAgainstASuccess() {
        Try<Integer> result = Try.apply(
                this::success
        );
        Try<Integer> out = result.orElse(new Try.Success<>(84));
        assertEquals("out must be Success(42)", out, new Try.Success<>(42));
    }
    @Test
    public void testOrElseAgainstAFailure() {
        Try<Integer> result = Try.apply(
                this::failure
        );
        Try<Integer> out = result.orElse(new Try.Success<>(84));
        assertEquals("out must be Success(84)", out, new Try.Success<>(84));
    }


    @Test
    public void testTransformAgainstASuccess() {
        Try<Integer> result = Try.apply(
                this::success
        );
        Try<Integer> out = result.transform(
                i -> new Try.Success<>(i + 42),
                exception -> new Try.Success<>(0)
        );
        assertEquals("out must be Success(84) (42 + 42)", out, new Try.Success<>(84));
    }

    @Test
    public void testTransformAgainstAFailure() {
        Try<Integer> result = Try.apply(
                this::failure
        );
        Try<Integer> out = result.transform(
                i -> new Try.Success<>(i + 42),
                exception -> new Try.Success<>(0)
        );
        assertEquals("out must be Success(0)", out, new Try.Success<>(0));
    }

    @Test(expected = OutOfMemoryError.class)
    public void testErrorMustNotBeCaught() throws Throwable {
        Try.apply(
                () -> {throw new OutOfMemoryError();}
        );
    }

    @Test(expected = NullPointerException.class)
    public void testSuppliedFailableSupplierIsNullShouldThrow() {
        FailableSupplier<Integer> s = null;

        Try.apply(s);
    }
    
    @Test
    public void testCloseableShouldBeClosedAfterConsumption() throws IOException{
    	verify(closeableMock, never()).close();
    	final int result = Try.apply(closeable -> success()).apply(closeableMock).get();
    	verify(closeableMock).close();
        assertThat(result, is(equalTo(42)));
    }
    
    @Test
    public void testInputStreamShouldBeClosedAfterConsumption() throws IOException{
    	verify(inputStreamMock, never()).close();
    	final String result = Try.apply(inputStream -> anotherSuccess()).apply(inputStreamMock).get();
    	verify(inputStreamMock).close();
        assertThat(result, is(equalTo("Hello World!")));
    }
    
    @Test(expected = NullPointerException.class)
    public void testSuppliedFunctionIsNullShouldThrow() throws IOException{
    	verify(inputStreamMock, never()).close();
    	Try.apply((Function<InputStream, String>) null).apply(inputStreamMock);
    	verify(inputStreamMock).close();
    }
    
    @Test
    public void testInputStreamShouldBeClosedAfterException() throws IOException{
    	verify(inputStreamMock, never()).close();
    	Try.apply(inputStream -> failure()).apply(inputStreamMock);
    	verify(inputStreamMock).close();
    }

    private int success() {
        return 42;
    }

    private String anotherSuccess() {
        return "Hello World!";
    }

    private int failure() throws NumberFormatException {
        throw new NumberFormatException("Number not valid");
    }
}
