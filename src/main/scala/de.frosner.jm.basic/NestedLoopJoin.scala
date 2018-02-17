package de.frosner.jm.basic
import de.frosner.jm.basic.JoinStrategy.DataSet

object NestedLoopJoin extends JoinStrategy {

  override def join[K, V1, V2](as: DataSet[K, V1], bs: DataSet[K, V2]): Seq[Entry[K, (V1, V2)]] =
    for {
      a <- as
      b <- bs
      if a.key == b.key
    } yield Entry(a.key, (a.value, b.value))

}
