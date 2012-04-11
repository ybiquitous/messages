package org.ybiquitous.messages.scala

import java.util.Locale

import org.hamcrest.CoreMatchers._
import org.junit.Test
import org.junit.Assert.assertThat

class MessageKeyTest {

  @Test def equality() {
    val key = MessageKey("test.key")
    val key2 = MessageKey("test.key")
    assertThat(key, is(key2))
  }

  @Test def simple() {
    val key = MessageKey("test.key")
    assertThat(key.get(1, 2, "3"), is("1 足す 2 は 3"))
  }

  @Test def locale() {
    val key = MessageKey("test.key")
    assertThat(key.get(Locale.ENGLISH, 1, 2, "3"), is("1 plus 2 is 3"))
  }
}
