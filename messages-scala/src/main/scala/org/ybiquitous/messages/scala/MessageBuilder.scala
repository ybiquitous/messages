package org.ybiquitous.messages.scala

import java.util.Locale
import org.ybiquitous.messages.MessageKeyNotFoundException

trait MessageBuilder {

  @throws(classOf[MessageKeyNotFoundException])
  def build(locale: Locale, key: String, args: Seq[Any]): String

  def buildOrElse(locale: Locale, key: String, args: Seq[Any], defaultValue: String): String = {
    try {
      build(locale, key, args)
    } catch {
      case e: MessageKeyNotFoundException => defaultValue
    }
  }
}
