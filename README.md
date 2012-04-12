Example
=======

messages.properties(on classpath root)

```
test.key = {0} and {1}
```

Java

```java
import org.ybiquitous.messages.*;

MessageKey.of("test.key").get(1, "abc");  //=> 1 and abc
MessageKey.of("test.key", "messages").get(1, "abc");  //=> 1 and abc
```

Scala

```scala
import org.ybiquitous.messages.scala._

MessageKey("test.key").get(1, "abc") //=> 1 and abc
MessageKey("test.key", "messages").get(1, "abc") //=> 1 and abc
```

Homepage
========

http://ybiquitous.github.com/messages/

License
=======

[The Apache Software License, Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.txt)
