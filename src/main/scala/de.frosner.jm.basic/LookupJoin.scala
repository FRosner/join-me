package de.frosner.jm.basic

import de.frosner.jm.basic.JoinStrategy.DataSet

object LookupJoin extends JoinStrategy {

  override def join[K, V1, V2](as: DataSet[K, V1],
                               bs: DataSet[K, V2]): DataSet[K, (V1, V2)] = {
    if (as.length < bs.length)
      lookupJoin(as, bs)((a, b) => (a, b))
    else
      lookupJoin(bs, as)((b, a) => (a, b))
  }

  private def lookupJoin[K, V1, V2, R1, R2](smalls: DataSet[K, V1],
                                            bigs: DataSet[K, V2])(
      combiner: (V1, V2) => (R1, R2)): DataSet[K, (R1, R2)] = {
    val lookupTable = smalls.map(e => (e.key, e.value)).toMap
    for {
      b <- bigs
      s <- lookupTable.get(b.key)
    } yield Entry(b.key, combiner(s, b.value))
  }

}
