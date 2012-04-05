package org.ybiquitous.messages;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class MessageKeyTest {

    @Test
    public void simple() {
        MessageKey test_key = new MessageKey("test.key");
        assertThat(test_key.get(1, 2, "三"), is("1 足す 2 は 三"));

        MessageKey test_key1 = new MessageKey("test.key1");
        MessageKey test_key2 = new MessageKey("test.key2");
        assertThat(test_key2.get(test_key1), is("'aaa' from other key"));
    }

}
