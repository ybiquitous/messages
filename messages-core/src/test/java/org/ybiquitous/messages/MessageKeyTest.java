package org.ybiquitous.messages;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

public class MessageKeyTest {

    @Rule
    public TestRule watcher = new TestWatcher() {
        @Override
        protected void starting(Description description) {
            System.out.printf("%1$tF/%1$tT - starting %2$s%n", System.currentTimeMillis(), description);
        }

        @Override
        protected void finished(Description description) {
            System.out.printf("%1$tF/%1$tT - finished %2$s%n", System.currentTimeMillis(), description);
        }
    };

    @Before
    public void setUp() {
        Locale.setDefault(Locale.JAPANESE);
    }

    @After
    public void cleanUp() {
        ThreadLocalLocaleHolder.reset();
    }

    @Test
    public void simple() {
        MessageKey test_key = MessageKey.of("test.key");
        assertThat(test_key.get(1, 2, "三"), is("1 足す 2 は 三"));

        MessageKey test_key1 = MessageKey.of("test.key1");
        MessageKey test_key2 = MessageKey.of("test.key2", "messages");
        assertThat(test_key2.get(test_key1), is("'あああ' 他のキー"));
    }

    @Test
    public void locale() {
        MessageKey test_key = MessageKey.of("test.key");
        assertThat(test_key.get(1, 2, "三"), is("1 足す 2 は 三"));

        ThreadLocalLocaleHolder.set(Locale.ENGLISH);
        assertThat(test_key.get(1, 2, 3), is("1 plus 2 is 3"));

        ThreadLocalLocaleHolder.reset();
        assertThat(test_key.get(1, 2, "三"), is("1 足す 2 は 三"));
        assertThat(test_key.get(Locale.ENGLISH, 1, 2, 3), is("1 plus 2 is 3"));

        // missing locale
        ThreadLocalLocaleHolder.set(Locale.ITALIAN);
        assertThat(test_key.get(1, 2, "3"), is("1 + 2 = 3"));
    }

    @Test(expected = MessageKeyNotFoundException.class)
    public void missing() {
        MessageKey.of("missing.key").get();
    }

    @Test
    public void defaultValue() {
        assertThat(MessageKey.of("missing.key").getOrElse("aa"), is("aa"));
        assertThat(MessageKey.of("missing.key").getOrElse(new Object[] { 1, 2 }, "aa"), is("aa"));
        assertThat(MessageKey.of("missing.key").getOrElse(Locale.CANADA, "aa"), is("aa"));
    }
}
