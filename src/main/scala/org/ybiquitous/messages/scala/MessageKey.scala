package org.ybiquitous.messages.scala

import java.util.Locale

import org.ybiquitous.messages.MessageBuilder
import org.ybiquitous.messages.{ MessageKey => MsgKey }

final class MessageKey private(val delegate: MsgKey) {
  def get(args: Any*) = delegate.get(args map unwrapArg: _*)
  def get(locale: Locale, args: Any*) = delegate.get(locale, args map unwrapArg: _*)
  override def toString = delegate.toString
  override def hashCode = delegate.hashCode
  override def equals(obj: Any) = obj match {
    case that: MessageKey => this.delegate == that.delegate
    case _ => false
  }
  private def unwrapArg(arg: Any): AnyRef = arg.asInstanceOf[AnyRef]
}

object MessageKey {
  def apply(
    key: String,
    resourceName: Option[String] = None,
    builder: Option[MessageBuilder] = None
  ): MessageKey = {
    if (resourceName.isEmpty && builder.isEmpty) {
      return new MessageKey(MsgKey.of(key))
    }
    if (!resourceName.isEmpty && builder.isEmpty) {
      return new MessageKey(MsgKey.of(key, resourceName.get))
    }
    if (resourceName.isEmpty && !builder.isEmpty) {
      return new MessageKey(MsgKey.of(key, builder.get))
    }
    if (!resourceName.isEmpty && !builder.isEmpty) {
      return new MessageKey(MsgKey.of(key, resourceName.get))
    }
    throw new AssertionError("never reach: " + List(key, resourceName, builder) )
  }
}
