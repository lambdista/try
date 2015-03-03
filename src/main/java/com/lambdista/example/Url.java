package com.lambdista.example;

import com.google.common.base.Function;
import com.lambdista.util.Consumer;
import com.lambdista.util.FailableSupplier;
import com.lambdista.util.Try;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

/**
 * @author Alessandro Lacava
 * @since 2014-07-16.
 */
public class Url {

    public static void main(String[] args) {

        String url = "http://www.google.com";
        String errorMessage = "Connection error";

        System.out.println("File read using the try-catch block");
        System.out.println(urlToStringWithoutTry(url, errorMessage));

        System.out.println("\nFile read using the Try-Success-Failure API");
        System.out.println(urlToStringWithTry(url, errorMessage));


    }

    public static String urlToStringWithoutTry(String url, String errorMessage) {
        Scanner scanner = null;
        try {
            scanner = new Scanner(new URL(url).openStream(), "UTF-8");
            String result = scanner.useDelimiter("\\A").next();
            scanner.close();
            return result;
        } catch (IOException e) {
            return errorMessage;
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    public static String urlToStringWithTry(final String url, String errorMessage) {
        Try<Scanner> scanner = Try.apply(
                new FailableSupplier<Scanner>() {
                    @Override
                    public Scanner get() throws Exception {
                        return new Scanner(new URL(url).openStream(), "UTF-8");
                    }
                }
        );

        String result = scanner.map(
                new Function<Scanner, String>() {
                    @Override
                    public String apply(Scanner scanner) {
                        return scanner.useDelimiter("\\A").next();
                    }
                }
        ).getOrElse(errorMessage);


        scanner.forEach(
                new Consumer<Scanner>() {
                    @Override
                    public void accept(Scanner scanner) {
                        scanner.close();
                    }
                }
        );
        return result;
    }
}
