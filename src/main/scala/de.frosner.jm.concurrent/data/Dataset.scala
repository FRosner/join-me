package de.frosner.jm.concurrent.data

import akka.actor.ActorRef

case class Dataset(partitions: Seq[ActorRef])
