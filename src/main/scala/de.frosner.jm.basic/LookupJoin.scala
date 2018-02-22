package de.frosner.jm.basic

import de.frosner.jm.basic.JoinStrategy.DataSet

object LookupJoin extends JoinStrategy {

  override def join[K, V1, V2](as: DataSet[K, V1], bs: DataSet[K, V2])(
      implicit ordering: Ordering[K]): DataSet[K, (V1, V2)] = {
    if (as.length < bs.length)
      lookupJoin(as, bs)((a, b) => (a, b))
    else
      lookupJoin(bs, as)((b, a) => (a, b))
  }

  private def lookupJoin[K, V1, V2, R1, R2](smalls: DataSet[K, V1],
                                            bigs: DataSet[K, V2])(
      combiner: (V1, V2) => (R1, R2)): DataSet[K, (R1, R2)] = {
    val lookupTable = smalls.groupBy(_.key)
    for {
      b <- bigs
      ss <- lookupTable.get(b.key).toSeq
      s <- ss
    } yield Entry(b.key, combiner(s.value, b.value))
  }

}
