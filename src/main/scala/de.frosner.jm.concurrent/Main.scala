package de.frosner.jm.concurrent

import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import akka.pattern.ask

import scala.concurrent.duration._
import de.frosner.jm.concurrent.actors.{Master, Printer, Slave}
import de.frosner.jm.concurrent.data.Entry
import de.frosner.jm.concurrent.messages.{DistributeDataset, ShowDataset}

import scala.concurrent.Await

object Main extends App {
  val system = ActorSystem("join-me")
  sys.addShutdownHook {
    system.terminate()
  }
  import system.dispatcher

  val numSlaves = 3
  val slaves = (1 to numSlaves).map { n =>
    system.actorOf(Props[Slave], s"Slave-$n")
  }
  val master = system.actorOf(Props(classOf[Master], slaves), "Master")
  val printer = system.actorOf(Props[Printer], "Printer")

  val testName = "test"
  val test = Seq(
    Entry("a", 5),
    Entry("b", 10)
  )

  val test2Name = "test2"
  val test2 = (1 to 1000).map(x => Entry(x.toString, x))

  implicit val timeout = Timeout(2.seconds)

  Await.ready(master.ask(DistributeDataset(test2Name, test2))(timeout, printer),
              timeout.duration)
  master.tell(ShowDataset(test2Name), printer)

}
