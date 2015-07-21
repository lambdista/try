# Credits #

* Thanks to [Gregor Trefs](https://github.com/gtrefs) for the suggestion about accepting `Throwable` types instead of
only `Exception` ones and for the implementation of the part that deals with `AutoCloseable` types.

* Thanks to [Sławomir Nowak](https://github.com/slnowak) for the suggestion about having two versions of `get`--the
checked and unchecked one--and for adapting the API to Java 7 using [Google Guava](https://github.com/google/guava).
You can find this alternative version in the `jdk7-support` branch.