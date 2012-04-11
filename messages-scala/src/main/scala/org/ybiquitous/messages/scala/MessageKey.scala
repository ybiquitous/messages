package org.ybiquitous.messages.scala

import java.util.Locale

import org.ybiquitous.messages.{ MessageKey => MsgKey }
import org.ybiquitous.messages.{ MessageBuilder => MsgBuilder }

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
  def apply(key: String) = new MessageKey(MsgKey.of(key))

  def apply(key: String, resource: String) = new MessageKey(MsgKey.of(key, resource))

  def apply(key: String, builder: (Locale, String, Seq[Any]) => String) = {
    new MessageKey(MsgKey.of(key, new MsgBuilder() {
      def build(locale: Locale, key: String, args: java.lang.Object*) = {
        builder(locale, key, args map wrapArg)
      }
      private def wrapArg(arg: java.lang.Object): Any = arg.asInstanceOf[Any]
    }))
  }
}
