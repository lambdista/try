# Scala's Try-Success-Failure for Java 8 #

This API is a Java 8 implementation of <a href="http://www.scala-lang.org/api/current/#scala.util.Try">Scala Try API</a>,
originally implemented by the guys at <a href="https://twitter.com/">Twitter</a> and later added to the Scala Standard Library.

The `Try` type represents a computation that may fail. If the computation is successful returns
the value wrapped in a `Try.Success` otherwise returns the
`java.lang.Exception` wrapped in a `Try.Failure`.

To use `Try` you need to call the `Try.apply(FailableSupplier)` method passing in a lambda with
the same signature used for a common `java.util.function.Supplier`.
Indeed `FailableSupplier` is just a `java.util.function.Supplier` with a
`throws Exception` added to its `get` method.

## Examples ##
### Read a file line by line ###
As a first example consider the code you need to implement a method to read a file line by line in Java 8. 
Using the traditional `try-catch` block you would implement as follows:

```java
public static List<String> readFile(String file) {

    List<String> lines;
    try {
        lines = Files.readAllLines(new File(file).toPath());
    } catch (IOException e) {
        lines = Arrays.asList("Could not read the file: " + file);
    }

    return lines;
}
```

`readFile` reads the content of a file, line by line, into a `List<String>`. In case of exception the method
returns a `List<String>` with just one line: *"Could not read..."*. 

However, using the `Try` API the previous method becomes:

```java
public static List<String> readFile(String file) {

    return Try.apply(() -> Files.readAllLines(new File(file).toPath()))
            .getOrElse(Arrays.asList("Could not read the file: " + file));

}
```

Which version do you like more? :-)

### Integer division ###
For example, `Try` can be used to perform division on a user-defined input, without the need to do explicit
exception-handling in all of the places that an exception might occur:

```java
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
```
In the previous example if you enter two valid integers with the second one--the divisor--being different from zero
then the code prints out `The result of division is: $RESULT`, where `$RESULT` is the division between the first
and the second number. On the other hand, if you either enter non valid integers--such as a string--or the second
number is zero then you'll get the message `The integers you entered are not valid or the divisor is zero.`
printed out.

An important property of `Try` shown in the previous code snippet is its ability
to *pipeline* (*chain* if you prefer)  operations, catching exceptions along the way thanks to its `flatMap` method.
If you are not a seasoned functional programming geek concepts such as `flatMap/map` might not be easy to grasp
at first. However you'll get used to them and, in the end, you'll love them. Moreover you're going to encounter
these methods more and more often since some important Java 8 classes already implement them
(e.g. `java.util.Optional` and `java.util.stream.Stream`. Anyway for the moment just take for
granted that to pipeline more than two operations, say N, you just need to chain them by using N - 1
`flatMap` calls and a last call to `map`. 

### Integer sum ###
Suppose you have 3 variables (x, y and z) being
of type `Try<Integer>` and you just want to sum them up. The code you need for doing that is the
following:

```java
x.flatMap(a -> y.flatMap(b -> z.map(c -> a + b + c)))
```

Apart from `map` and `flatMap`, `Try` has many other useful methods. See the `TryTest`
class for a thorough coverage of all methods.

## Javadoc ##
<a href="http://typesafely.github.io/try/apidocs/">API documentation</a> for this project.

## Build ##

To build:

```
$ git clone https://github.com/typesafely/try.git
$ cd try
$ mvn package
```

You'll find the `jar` under the usual `target` directory.

## Bugs and Feedback ##

For bugs, questions and discussions please use the [Github Issues](https://github.com/typesafely/try/issues).

