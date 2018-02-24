package de.frosner.jm.concurrent.actors

import akka.actor.Actor
import akka.event.Logging
import de.frosner.jm.concurrent.data.Partition
import de.frosner.jm.concurrent.messages.ReceiveInMemoryPartition

class Printer extends Actor {
  val log = Logging(context.system, this)

  def receive = {
    case x => println(x)
  }
}
