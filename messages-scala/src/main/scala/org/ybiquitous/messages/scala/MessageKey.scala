package org.ybiquitous.messages.scala

import java.util.Locale

import org.ybiquitous.messages.{ MessageKey => MsgKey }
import org.ybiquitous.messages.{ MessageBuilder => MsgBuilder }

final class MessageKey private(
  val key: String,
  private val resource: Option[String] = None,
  private val builder: Option[MessageBuilder] = None
) {

  private def unwrapArg(arg: Any): AnyRef = arg.asInstanceOf[AnyRef]

  def get(args: Any*) = delegate.get(args map unwrapArg: _*)
  def get(locale: Locale, args: Any*) = delegate.get(locale, args map unwrapArg: _*)

  def getOrElse(defaultValue: String) = delegate.getOrElse(defaultValue)
  def getOrElse(args: Seq[Any], defaultValue: String) = delegate.getOrElse(args map unwrapArg toArray, defaultValue)
  def getOrElse(locale: Locale, defaultValue: String) = delegate.getOrElse(locale, defaultValue)
  def getOrElse(locale: Locale, args: Seq[Any], defaultValue: String) = delegate.getOrElse(locale, args map unwrapArg toArray, defaultValue)

  override def toString = delegate.toString

  private val delegate = resource match{
    case Some(r) => MsgKey.of(key, r)
    case _       => builder match {
      case Some(b) => MsgKey.of(key, new MsgBuilderImpl(b))
      case _       => MsgKey.of(key)
    }
  }

  private class MsgBuilderImpl(builder: MessageBuilder) extends MsgBuilder {
    override def build(locale: Locale, key: String, args: java.lang.Object*): String = {
      builder.build(locale, key, args map wrapArg)
    }

    override def buildOrElse(locale: Locale, key: String, args: Array[java.lang.Object], defaultValue: String): String = {
      builder.buildOrElse(locale, key, args map wrapArg, defaultValue)
    }

    private def wrapArg(arg: java.lang.Object): Any = arg.asInstanceOf[Any]
  }
}

object MessageKey {
  def apply(
    key: String,
    resource: Option[String] = None,
    builder: Option[MessageBuilder] = None
  ): MessageKey = new MessageKey(key, resource, builder)
}
