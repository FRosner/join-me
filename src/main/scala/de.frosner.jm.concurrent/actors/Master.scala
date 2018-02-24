package de.frosner.jm.concurrent.actors

import akka.actor.{Actor, ActorRef}
import akka.event.Logging
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.duration._
import de.frosner.jm.concurrent.data.InMemoryPartition
import de.frosner.jm.concurrent.messages._

import scala.concurrent.{Await, Future}
import scala.util.{Failure, Success}

case class Master(slaves: Seq[ActorRef]) extends Actor {
  import context.dispatcher

  val log = Logging(context.system, this)

  private var datasets: Set[String] = Set.empty

  def receive = {
    case m @ DistributeDataset(name, entries) =>
      log.info(m.toString)
      val partitions: Seq[InMemoryPartition] =
        entries
          .grouped((entries.size.toDouble / slaves.size).ceil.toInt)
          .toList
          .map(InMemoryPartition)
      implicit val timeout = Timeout(2.seconds)
      val results = slaves
        .zipAll(partitions, slaves.head, InMemoryPartition(Seq.empty))
        .map {
          case (slave, partition) =>
            slave ? ReceiveInMemoryPartition(name, partition)
        }
      val result = Future.sequence(results)
      Await.ready(result, (timeout.duration.toSeconds * slaves.size).seconds)
      datasets += name
      sender() ! result.map(_ => DistributeDatasetCompleted(name))
    case m @ ShowDataset(name) =>
      log.info(m.toString)
      if (datasets.contains(name)) {
        slaves.foreach(_.tell(ShowPartition(name), sender()))
        sender() ! ShowDatasetCompleted(Success(name))
      } else {
        sender() ! ShowDatasetCompleted(Failure(DatasetNotFound(name)))
      }
    case m => log.warning(s"received unknown message: $m")
  }
}
