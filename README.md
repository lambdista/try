# Scala's Try-Success-Failure for Java 8 #

This API is a Java 8 implementation of the <a href="http://www.scala-lang.org/api/current/#scala.util.Try">Scala Try API</a>,
originally implemented by <a href="https://twitter.com/">Twitter</a> Engineers and later added to the Scala Standard Library.

The `Try` type represents a computation that may fail. If the computation is successful it returns
the value wrapped in a `Try.Success` otherwise the `java.lang.Exception` wrapped in a `Try.Failure`.

In order to use `Try` you need to call the `Try.apply(FailableSupplier)` method providing a lambda with
the same signature used for a common `java.util.function.Supplier`.
Indeed `FailableSupplier` is just a `java.util.function.Supplier` with a
`throws Exception` added to its `get` method.

Note that I'm not saying here that the `try-catch` approach must be abandoned in favour of `Try-Success-Failure`.
Indeed there are cases where you would use the traditional `try-catch` pattern but, in general, I think this API provides
a more *fluent interface* to deal with exceptions.

## Build ##
This project is managed with [Maven](http://maven.apache.org/) so it can be built using:

```
$ git clone https://github.com/lambdista/try.git
$ cd try
$ mvn package
```

You'll find the `jar` under the usual `target` directory.

## Running the examples in source code ##
Using [Maven](http://maven.apache.org/) and the [exec-maven-plugin](http://mojo.codehaus.org/exec-maven-plugin/) 
you can run the main classes representing the examples for this project. For instance, to run the `ReadFileLines` main
class you can use:

```
$ mvn exec:java -Dexec.mainClass="com.lambdista.example.ReadFileLines"
```

The changing part is the full path to the main class you intend to run.

## Examples ##
In order to get you acquainted with this API each example will be provided using both the same old `try-catch` pattern
and the new `Try` API.

### Example 1: Read a file line by line ###
As a first example consider the code you need to implement a method to read a file line by line in Java 8. 
#### Using the traditional try-catch block ####
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
#### Using the Try API ####
```java
public static List<String> readFile(String file) {

    return Try.apply(() -> Files.readAllLines(new File(file).toPath()))
            .getOrElse(Arrays.asList("Could not read the file: " + file));

}
```

In this case a lambda is passed to `Try.apply`. The `Try`'s 
`getOrElse` method returns the value obtained by the call to `Files.readAllLines(new File(file).toPath())` if 
no exception is thrown or whatever you passed to it in case of exception. Of course everything is *type safe*, 
in the sense that `getOrElse` will accept only arguments whose type is the same of `Try`'s. Thanks to the
type inferer there's no need to specify the type for `Try` in the previous code. In fact, it is equivalent to
`Try.<List<String>>apply(() -> Files.readAllLines(new File(file).toPath()))` where you explicitly say that
the `Try`'s type is `List<String>`.

Which version do you like more? The `try-catch` approach or the `Try` one? 
It may be a matter of taste or just because I'm used to it but I prefer the latter--also
because otherwise I wouldn't have written this API! :-)

### Example 2: Read the content of a URL into a String ###
#### Using the traditional try-catch-finally block ####
```java
public static String urlToString(String url, String errorMessage) {
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
```

`urlToString` reads the content of a URL into a `String`. The method takes two parameters: `url` which is the 
`String` representing the URL and `errorMessage` which is the `String` to return if the URL content retrieving fails. 
Notice the boilerplate code. You need to initialize the `scanner` reference
to `null`. You also have to use a finally block and close the `Scanner` object after checking if it is not `null`.
Wouldn't it be great if you could avoid such a boilerplate code and let an API do it for you? Well, take a look
at the, semantically, same code in the following example.

#### Using the Try API ####
```java
public static String urlToString(String url, String errorMessage) {
    Try<Scanner> scanner = Try.apply(() -> new Scanner(new URL(url).openStream(), "UTF-8"));
    String result = scanner.map(s -> s.useDelimiter("\\A").next()).getOrElse(errorMessage);
    scanner.forEach(s -> s.close());
    return result;
}
```

Look ma, no `null` initialization, no `try-catch-finally` block and no `null` check before closing `scanner`!
The first line of the method creates a `Try<Scanner>` object which can be, as usual, a `Success<Scanner>` or a 
`Failure<Scanner>` depending on the result of the lambda. The `map` method is then used to transform it 
into a `Try<String>`, taking care of the fact that if the result
of `Try.apply` is a `Failure<Scanner>` now it just becomes a `Failure<String>` otherwise it gets mapped into a 
`Success<String>`. `getOrElse` then extracts its content (a `String`) if it's a `Success` or returns `errorMessage` if
it's a `Failure`. Afterward the `forEach` method takes care of closing the `Scanner` object if it is of type
`Success<Scanner>` otherwise it does nothing. Finally the result is returned. 

Typically you use `map` to transform something into something else, 
while you employ `forEach` to *consume* something, that is to use it someway. As a matter of fact 
`forEach` has a `void` return type.

The `Try` version is declarative whilst the `try-catch-finally` one is imperative. Expressing the `Try` version in 
words you have: "*Try* to create a `Scanner` object for the given URL. Afterward *map* this object into a `String` *or else*
use this other `String` if it's a failure. In the end close the `Scanner` object." 

### Example 3: Integer division ###
This is an interesting one because it shows another peculiarity of the `Try` API. You may already know that Java
has both checked and unchecked exceptions. For checked exceptions the compiler won't accept your code
if you forget to handle them. However unchecked exceptions such as `NullPointerException`, `IllegalArgumentException`,
`RuntimeException` and so on are not notified by the compiler if you don't handle them. Consider the following code
snippet:
```java
System.out.println("Enter the dividend press Return and then enter the divisor: ");
Scanner dividend = new Scanner(System.in);
Scanner divisor = new Scanner(System.in);

int num = dividend.nextInt();
int denum = divisor.nextInt();
String res = "The result of division is: " + (num / denum);
System.out.println(res);
```

The previous code asks the user to enter two integers and then performs their division. The problem is that it could
throw two types of unchecked exceptions and the compiler of course wouldn't tell you. You are required to know it 
yourself. The two unchecked exceptions I'm talking about are `java.util.InputMismatchException` and 
`java.lang.ArithmeticException` if the user enter a non-integer or zero as the divisor, respectively. Now,
if you have a decent mathematical background you know you can't divide by zero. Furthermore you can also imagine that
`Scanner`'s `nextInt` method may throw some type of exception if you enter a non-integer. However, in both cases
you have to look up the type of exception. Yes, you can use a generic `catch(Exception e)` and capture them all if you're
not interested in the specific type or you could avoid using `try-catch` in the first place thanks to `Try`. Here are both 
implementations.
#### Using the traditional try-catch block ####
```java
public static void divideWithoutTry() {

    System.out.println("Enter the dividend press Return and then enter the divisor: ");
    Scanner dividend = new Scanner(System.in);
    Scanner divisor = new Scanner(System.in);

    String res;
    try {
        res = "The result of division is: " + (dividend.nextInt() / divisor.nextInt());
    } catch(InputMismatchException|ArithmeticException e) {
        res = "The integers you entered are not valid or the divisor is zero.";
    }

    System.out.println(res);
}
```

#### Using the Try API ####
```java
public static void divideWithTry() {

    System.out.println("Enter the dividend press Return and then enter the divisor: ");
    Scanner dividend = new Scanner(System.in);
    Scanner divisor = new Scanner(System.in);

    String res = Try.apply(() -> dividend.nextInt() / divisor.nextInt())
            .map(quotient -> "The result of division is: " + quotient)
            .getOrElse("The integers you entered are not valid or the divisor is zero.");

    System.out.println(res);
}
```

In the previous code `map` maps the `Integer` result into a `String`. After the `map` call you have something
that was a `Try<Integer>` transformed into a `Try<String>`. This is another important aspect of `Try`. Its type can be
mapped into another type without the need to do explicit exception-handling in all of the places that an 
exception might occur. I mean if `dividend.nextInt() / divisor.nextInt()` caused an exception the result of `Try.apply`
would be a `Try.Failure` instance. Nevertheless it's type would be `Try<String>`. This lets you call `getOrElse`
passing a `String` to it.



### Integer sum ###
An important property of `Try` is its ability
to *pipeline* (*chain* if you prefer)  operations, catching exceptions along the way thanks to its `flatMap` method.
If you are not a functional programmer concepts such as `flatMap/map` might not be easy to grasp
at first. However you'll get used to them when you become one and, in the end, you'll love them. 
Moreover you're going to encounter
these methods more and more often since some important Java 8 classes already implement them
(e.g. `java.util.Optional` and `java.util.stream.Stream`). Anyway for the moment just take for
granted that to pipeline more than two operations, say N, you just need to chain them by using N - 1
`flatMap` calls and a last call to `map`. For example, suppose you have 3 variables--x, y and z--being
of type `Try<Integer>` and you just want to sum them up. Here is the code you need:

```java
x.flatMap(a -> y.flatMap(b -> z.map(c -> a + b + c)))
```

Apart from the methods seen in these examples, such as `map`, `flatMap` and `getOrElse`, `Try` 
has many other useful methods. See the `TryTest` class for a thorough coverage of all its methods.

## Javadoc ##
<a href="http://lambdista.github.io/try/apidocs/">API documentation</a> for this project.

## Bugs and Feedback ##
For bugs, questions and discussions please use the [Github Issues](https://github.com/lambdista/try/issues).

## Conclusion ##
What else to say? Give Try a try (pun intended :-)) you won't regret once you get used to it.

