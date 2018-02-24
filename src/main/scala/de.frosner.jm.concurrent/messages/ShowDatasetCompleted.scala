package de.frosner.jm.concurrent.messages

import de.frosner.jm.concurrent.data.Entry

import scala.util.Try

case class ShowDatasetCompleted(name: Try[String])
