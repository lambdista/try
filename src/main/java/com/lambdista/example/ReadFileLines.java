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

import com.lambdista.util.Try;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

/**
 * File lines read example
 *
 * @author Alessandro Lacava
 * @since 2014-06-25.
 */
public class ReadFileLines {

    public static void main(String[] args) {

        List<String> lines = readFileWithoutTry("test.txt");
        System.out.println("File read using the try-catch block");
        lines.forEach(System.out::println);

        lines = readFileWithTry("test.txt");
        System.out.println("File read using the Try-Success-Failure API");
        lines.forEach(System.out::println);

    } 

    public static List<String> readFileWithoutTry(String file) {

        List<String> lines;
        try {
            lines = Files.readAllLines(new File(file).toPath());
        } catch (IOException e) {
            lines = Arrays.asList("Could not read the file: " + file);
        }

        return lines;
    }

    public static List<String> readFileWithTry(String file) {

        return Try.apply(() -> Files.readAllLines(new File(file).toPath()))
                .getOrElse(Arrays.asList("Could not read the file: " + file));

    }
}
