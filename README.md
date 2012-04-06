Code Example
============

_messages.properties_

    test.key = {0} and {1}

_Java_

    import org.ybiquitous.messages.*;

    MessageKey.of("test.key").get(1, "abc");  //=> 1 and abc
    MessageKey.of("test.key", "messages").get(1, "abc");  //=> 1 and abc


Dependency
==========

Required:

* None.

Optional:

* Apache Velocity 1.7+
* Apache Ant 1.8+
