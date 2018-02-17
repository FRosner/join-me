package de.frosner.jm.basic

import de.frosner.jm.basic.JoinStrategy.DataSet

trait JoinStrategy {

  def join[K, V1, V2](a: DataSet[K, V1], b: DataSet[K, V2]): DataSet[K, (V1, V2)]

}

object JoinStrategy {

  type DataSet[K, V] = Seq[Entry[K, V]]

}
