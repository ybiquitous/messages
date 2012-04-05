package org.ybiquitous.messages;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.Test;

public class MessageKeyTest {

    @Test
    public void simple() {
        MessageKey test_key = newMessageKey("test.key");
        assertThat(test_key.get(1, 2, "三"), is("1 足す 2 は 三"));

        MessageKey test_key1 = newMessageKey("test.key1");
        MessageKey test_key2 = newMessageKey("test.key2");
        assertThat(test_key2.get(test_key1), is("'aaa' from other key"));
    }

    @Test
    public void locale() {
        MessageKey test_key = newMessageKey("test.key");
        assertThat(test_key.get(1, 2, "三"), is("1 足す 2 は 三"));

        MessageLocaleHolder.set(Locale.ENGLISH);
        assertThat(test_key.get(1, 2, 3), is("1 plus 2 is 3"));

        MessageLocaleHolder.reset();
        assertThat(test_key.get(1, 2, "三"), is("1 足す 2 は 三"));
        assertThat(test_key.get(Locale.ENGLISH, 1, 2, 3), is("1 plus 2 is 3"));
    }

    private MessageKey newMessageKey(String key) {
        return new MessageKey(key, "messages");
    }
}
