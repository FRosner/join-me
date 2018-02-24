package de.frosner.jm.concurrent.messages

case class DatasetNotFound(name: String)
    extends Exception(s"Dataset $name not found")
