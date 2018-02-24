package de.frosner.jm.concurrent.data

trait Partition {
  def size: Long
  def read: Stream[Entry]
}
