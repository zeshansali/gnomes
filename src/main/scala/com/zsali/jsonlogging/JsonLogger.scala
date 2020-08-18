package com.zsali.jsonlogging

import net.logstash.logback.argument.StructuredArguments.entries
import org.slf4j.LoggerFactory

import scala.jdk.CollectionConverters._

case class JsonLogger(name: String = "main") {
  val logger = LoggerFactory.getLogger(name)

  def trace(
      message: String,
      context: Map[String, String] = Map(),
      maybeException: Option[Exception] = None
  ): Unit = {
    if (logger.isTraceEnabled) {
      logger.trace(message, entries(context.asJava), maybeException.getOrElse(None))
    }
  }

  def debug(
      message: String,
      context: Map[String, String] = Map(),
      maybeException: Option[Exception] = None
  ): Unit = {
    if (logger.isDebugEnabled) {
      logger.debug(message, entries(context.asJava), maybeException.getOrElse(None))
    }
  }

  def info(
      message: String,
      context: Map[String, String] = Map(),
      maybeException: Option[Exception] = None
  ): Unit = {
    if (logger.isInfoEnabled) {
      logger.info(message, entries(context.asJava), maybeException.getOrElse(None))
    }
  }

  def warn(
      message: String,
      context: Map[String, String] = Map(),
      maybeException: Option[Exception] = None
  ): Unit = {
    if (logger.isWarnEnabled) {
      logger.warn(message, entries(context.asJava), maybeException.getOrElse(None))
    }
  }

  def error(
      message: String,
      context: Map[String, String] = Map(),
      maybeException: Option[Exception] = None
  ): Unit = {
    if (logger.isErrorEnabled) {
      logger.error(message, entries(context.asJava), maybeException.getOrElse(None))
    }
  }
}
