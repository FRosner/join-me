package de.frosner.jm.concurrent.messages

import de.frosner.jm.concurrent.data.InMemoryPartition

case class ReceiveInMemoryPartition(datasetName: String,
                                    partition: InMemoryPartition)
