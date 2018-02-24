package de.frosner.jm.concurrent.actors

import akka.actor.{Actor, ActorRef}
import akka.event.Logging
import de.frosner.jm.concurrent.data.{Dataset, Partition}
import de.frosner.jm.concurrent.messages._

import scala.util.Try

class Slave extends Actor {
  val log = Logging(context.system, this)

  private var partitions: Map[String, Partition] = Map.empty

  def receive = {
    case m @ ReceiveInMemoryPartition(name, partition) =>
      log.info(m.toString)
      partitions += name -> partition
      sender() ! ReceiveInMemoryPartitionCompleted(name)
    case m @ ShowPartition(name) =>
      log.info(m.toString)
      sender() ! ShowPartitionCompleted(
        Try(partitions(name)).map(_.read.toList))
    case m => log.warning(s"received unknown message: $m")
  }
}
