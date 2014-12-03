package org.typesafely.example;

import org.typesafely.util.Try;

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

    public static String urlToStringWithTry(String url, String errorMessage) {
        Try<Scanner> scanner = Try.apply(() -> new Scanner(new URL(url).openStream(), "UTF-8"));
        String result = scanner.map(s -> s.useDelimiter("\\A").next()).getOrElse(errorMessage);
        scanner.forEach(s -> s.close());
        return result;
    }
}
