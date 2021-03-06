Example
=======

messages.properties (on classpath root, UTF-8 encoded)

```
test.key = {0} & {1}
```

messages_en.properties (on classpath root, UTF-8 encoded)

```
test.key = {0} and {1}
```

Java

```java
import java.util.Locale;
import org.ybiquitous.messages.MessageKey;
import org.ybiquitous.messages.MessageLocaleHolder;

Locale.setDefault(Locale.FRENCH);

// default locale
MessageKey.of("test.key").get(1, "abc"); //=> 1 & abc
MessageKey.of("test.key", "messages").get(1, "abc"); //=> 1 & abc

// specify locale
MessageKey.of("test.key").get(Locale.ENGLISH, 1, "abc"); //=> 1 and abc

// use locale saved on thread-local
ThreadLocalLocaleHolder.set(Locale.ENGLISH);
MessageKey.of("test.key").get(1, "abc"); //=> 1 and abc
```

Scala

```scala
import org.ybiquitous.messages.scala.MessageKey

MessageKey("test.key").get(1, "abc") //=> 1 and abc
MessageKey("test.key", "messages").get(1, "abc") //=> 1 and abc
```

See [Sample Project](https://github.com/ybiquitous/messages/tree/master/messages-sample).

Required
========

- Java 1.6+
- Scala 2.9+

Homepage
========

http://ybiquitous.github.com/messages/

API
===

- [Java](http://ybiquitous.github.com/messages/apidocs/)
- [Scala](http://ybiquitous.github.com/messages/messages-scala/scaladocs/)

License
=======

[The Apache Software License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)
