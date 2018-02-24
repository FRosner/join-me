package de.frosner.jm.concurrent.data

case class InMemoryPartition(entries: Seq[Entry]) extends Partition {
  override def size: Long = entries.size.toLong

  override def read: Stream[Entry] = entries.toStream
}
