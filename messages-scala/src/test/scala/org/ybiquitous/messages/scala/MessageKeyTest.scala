package org.ybiquitous.messages.scala

import java.util.Locale

import org.hamcrest.CoreMatchers._
import org.junit.Test
import org.junit.Assert.assertThat

class MessageKeyTest {

  @Test def simple() {
    val key = MessageKey("test.key")
    assertThat(key.get(1, 2, "3"), is("1 足す 2 は 3"))
    assertThat(key.key, is("test.key"))
  }

  @Test def locale() {
    val key = MessageKey("test.key")
    assertThat(key.get(Locale.ENGLISH, 1, 2, "3"), is("1 plus 2 is 3"))
  }

  @Test def resource() {
    val key = MessageKey("test.key", resource = Some("messages"))
    assertThat(key.get(1, 2, "3"), is("1 足す 2 は 3"))
  }

  @Test def builder() {
    val builder = new MessageBuilder {
      def build(locale: Locale, key: String, args: Seq[Any]): String = {
        List(locale, key) ::: args.toList mkString ", "
      }
    }
    val key = MessageKey("test.key", builder = Some(builder))
    assertThat(key.get(Locale.ENGLISH, 1, 2), is("en, test.key, 1, 2"))
  }

  @Test def missing() {
    val key = MessageKey("a")
    assertThat(key.getOrElse("def"), is("def"))
    assertThat(key.getOrElse(Seq(1), "def"), is("def"))
    assertThat(key.getOrElse(Locale.ENGLISH, "def"), is("def"))
    assertThat(key.getOrElse(Locale.ENGLISH, Seq(1), "def"), is("def"))
  }
}
