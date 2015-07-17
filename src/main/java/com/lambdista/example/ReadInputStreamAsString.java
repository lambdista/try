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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import com.lambdista.util.Try;

/**
 * Try-with-resource input stream consumption
 *
 * @author Gregor Trefs 
 */
public class ReadInputStreamAsString { 

    public static void main(String[] args) throws IOException {
    	final InputStream stream = createByteArrayInputStream(50);
    	markStartOfStream(stream);
    	
        System.out.println("Read InputStream as String using the try-catch block");
        final String result1 = consumptionWithoutTry(stream);
        System.out.println("Result: " + result1);

        resetStreamToStart(stream);
        
        System.out.println("Read InputStream as String using the Try-Success-Failure API");
        final String result2 = consumptionWithTry(stream);
        System.out.println("Result: " + result2);
    }

	private static void markStartOfStream(final InputStream stream) {
		stream.mark(100);
	}

	private static void resetStreamToStart(final InputStream stream) throws IOException {
		stream.reset();
	}
    
    private static InputStream createByteArrayInputStream(int numberOfRandomBytes){
    	final byte[] randomBytes = new byte[numberOfRandomBytes];
    	new Random().nextBytes(randomBytes);
    	return new ByteArrayInputStream(randomBytes);
    }

    private static String consumptionWithoutTry(InputStream stream) {
    	try(InputStream in = stream){
    		return convertStreamToString(in);
    	} catch (IOException e) {
			return "";
		}    
    }

    private static String consumptionWithTry(InputStream stream) {
        return Try.apply(ReadInputStreamAsString::convertStreamToString).apply(stream).get();
    }
    
	// http://stackoverflow.com/questions/309424/read-convert-an-inputstream-to-a-string
	@SuppressWarnings("resource")
	private static String convertStreamToString(java.io.InputStream is) {
		try (java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A")) {
			return s.hasNext() ? s.next() : "";
		}
	}

}
