# Try-Success-Failure API: Java implementation of the famous Scala counterpart. #

This API is a Java implementation of <a href="http://www.scala-lang.org/api/current/#scala.util.Try">Scala Try API</a>,
originally implemented by the guys at <a href="https://twitter.com/">Twitter</a> and later added to the Scala Standard Library.

The `Try` type represents a computation that may fail. If the computation is successful returns
the value wrapped in a `Try.Success` otherwise returns the
`java.lang.Exception` wrapped in a `Try.Failure`.

To use `Try` you need to call the `Try.apply(FailableSupplier)` method passing in a lambda with
the same signature used for a common `java.util.function.Supplier`.
Indeed `FailableSupplier` is just a `java.util.function.Supplier` with a
`throws Exception` added to its `get` method.

For example, `Try` can be used to perform division on a user-defined input, without the need to do explicit
exception-handling in all of the places that an exception might occur.

An important property of `Try` shown in the `divide` method of the `MainExample` class is its ability
to *pipeline (chain if you prefer)*  operations, catching exceptions along the way thanks to its `flatMap` method.
If you are not a seasoned functional programming geek concepts such as {@code flatMap/map} might not be easy to grasp
at first. However you'll get used to them and, in the end, you'll love them. Moreover you're going to encounter
these methods more and more often since some important Java 8 classes already implement them
(e.g. `java.util.Optional` and `java.util.stream.Stream`. Anyway for the moment just take for
granted that to pipeline more than two operations, say N, you just need to chain them by using N - 1
`flatMap` calls and a last call to `map`. E.g.: Suppose you have 3 variables (x, y and z) being
of type `Try<Integer>` and you just wanto to sum them up. The code you need for doing that is the
following:

```java
x.flatMap(a -> y.flatMap(b -> z.map(c -> a + b + c)))
```

Apart from `map` and `flatMap`, `Try` has many other useful methods. See the `TryTest`
class for a thorough coverage of all methods.

## Usage example ##

```java
System.out.println("Integer division");
System.out.println("Enter the dividend press Return and then enter the divisor: ");
Scanner dividend = new Scanner(System.in);
Scanner divisor = new Scanner(System.in);

Try<Integer> num = Try.apply(dividend::nextInt);
Try<Integer> denom = Try.apply(divisor::nextInt);

Try<Integer> result = num.flatMap(x -> denom.map(y -> x / y));
Try<String> resultTryStr = result.map(i -> "The result of division is: " + i);
String resultStr = resultTryStr.getOrElse("You must've divided by zero or entered something that's not an Int. Try again!");
System.out.println(resultStr);
```

In the previous example if you enter two valid integers with the second one--the divisor--being different from zero
then the code prints out `The result of division is: $RESULT`, where `$RESULT` is the division between the first
and the second number. On the other hand, if you either enter non valid integers--such as a string--or the second
number is zero then you'll get the message `You must've divided by zero or entered something that's not an Int. Try again!`
printed out.

Anyway, as I already said, see the `TryTest` class for a thorough coverage of all methods.

## Final Notes ##
Any criticism/suggestion is more than welcome!