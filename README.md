# Scala's Try-Success-Failure for Java 7 #

This API is a Java 7 port of the master branch (which uses Java 8 instead). This port has been done by
[Sławomir Nowak](https://github.com/slnowak) using [Google Guava library](https://github.com/google/guava) which provides
functional abstractions such as `Function` and `Predicate`--apart many other goodies.

Of course, since Java 7 didn't have lambdas yet the syntax here is much
more verbose, but considering Sławomir found the API useful nonetheless I decided to include it here as
an alternative branch.

For a more detailed explaination of this API usage see the
[README for the Java 8 version](https://github.com/lambdista/try). Of course you need to *adapt* the Java 8 lambda
syntax to the Java 7 boilerplate version.

## Bugs and Feedback ##
For bugs, questions and discussions please use the [Github Issues](https://github.com/lambdista/try/issues).

## License ##
Copyright 2014 Alessandro Lacava.

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance
with the License. You may obtain a copy of the License at

[http://www.apache.org/licenses/LICENSE-2.0](http://www.apache.org/licenses/LICENSE-2.0)

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and limitations under the License.

## Credits ##
[Credits](https://github.com/lambdista/try/blob/jdk7-support/CREDITS.md)

