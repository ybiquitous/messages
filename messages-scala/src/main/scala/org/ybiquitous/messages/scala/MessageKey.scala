package org.ybiquitous.messages.scala

import java.util.Locale

import org.ybiquitous.messages.{ MessageKey => MsgKey }
import org.ybiquitous.messages.{ MessageBuilder => MsgBuilder }

final class MessageKey private(private val delegate: MsgKey) {
  def get(args: Any*) = delegate.get(args map unwrapArg: _*)
  def get(locale: Locale, args: Any*) = delegate.get(locale, args map unwrapArg: _*)

  def getOrElse(defaultValue: String) = delegate.getOrElse(defaultValue)
  def getOrElse(args: Seq[Any], defaultValue: String) = delegate.getOrElse(args map unwrapArg toArray, defaultValue)
  def getOrElse(locale: Locale, defaultValue: String) = delegate.getOrElse(locale, defaultValue)
  def getOrElse(locale: Locale, args: Seq[Any], defaultValue: String) = delegate.getOrElse(locale, args map unwrapArg toArray, defaultValue)

  override def toString = delegate.toString
  override def hashCode = delegate.hashCode
  override def equals(obj: Any) = obj match {
    case that: MessageKey => this.delegate == that.delegate
    case _ => false
  }
  private def unwrapArg(arg: Any): AnyRef = arg.asInstanceOf[AnyRef]
}

object MessageKey {
  def apply(key: String) = new MessageKey(MsgKey.of(key))

  def apply(key: String, resource: String) = new MessageKey(MsgKey.of(key, resource))

  def apply(key: String, builder: MessageBuilder) = {
    new MessageKey(MsgKey.of(key, new MsgBuilderImpl(builder)))
  }

  private class MsgBuilderImpl(builder: MessageBuilder) extends MsgBuilder {
    def build(locale: Locale, key: String, args: java.lang.Object*): String = {
      builder.build(locale, key, args map wrapArg)
    }
    def buildOrElse(locale: Locale, key: String, args: Array[java.lang.Object], defaultValue: String): String = {
      builder.buildOrElse(locale, key, args map wrapArg, defaultValue)
    }
    private def wrapArg(arg: java.lang.Object): Any = arg.asInstanceOf[Any]
  }
}
