Example
=======

messages.properties(on classpath root)

```
test.key = {0} and {1}
```

Java

```java
import org.ybiquitous.messages.MessageKey;

MessageKey.of("test.key").get(1, "abc");  //=> 1 and abc
MessageKey.of("test.key", "messages").get(1, "abc");  //=> 1 and abc
```

Scala

```scala
import org.ybiquitous.messages.scala.MessageKey

MessageKey("test.key").get(1, "abc") //=> 1 and abc
MessageKey("test.key", "messages").get(1, "abc") //=> 1 and abc
```

Homepage
========

http://ybiquitous.github.com/messages/

API
===

[Core](http://ybiquitous.github.com/messages/messages-core/apidocs/)
[Generator](http://ybiquitous.github.com/messages/messages-generator/apidocs/)
[Scala](http://ybiquitous.github.com/messages/messages-scala/scaladocs/)

License
=======

[The Apache Software License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)
