package de.frosner.jm.concurrent.messages

import de.frosner.jm.concurrent.data.Entry

case class DistributeDataset(name: String, entries: Seq[Entry])
