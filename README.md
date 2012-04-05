Code Example
============

_messages.properties_

    test.key = {0} and {1}

_Java_

    import org.ybiquitous.messages.*;

    new MessageKey("test.key1", "messages").get(1, "abc");  //=> 1 and abc


Dependency
==========

No Dependency.
