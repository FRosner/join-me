package de.frosner.jm.basic

import de.frosner.jm.basic.JoinStrategy.DataSet

import scala.collection.mutable

object SortMergeJoin extends JoinStrategy {

  override def join[K, V1, V2](as: DataSet[K, V1], bs: DataSet[K, V2])(
      implicit ordering: Ordering[K]): DataSet[K, (V1, V2)] = {
    /*
      I'm cheating here as I'm too lazy to implement it efficiently. As I don't
      want to assume that the keys are unique in either of the two relations,
      I have to aggregate values of the same key when sorting. Usually you
      would do that by sorting and building up the value list in the same time.
     */
    val asSorted: Iterator[(K, Seq[Entry[K, V1]])] =
      as.groupBy(_.key).toSeq.sortBy(_._1).iterator
    val bsSorted: Iterator[(K, Seq[Entry[K, V2]])] =
      bs.groupBy(_.key).toSeq.sortBy(_._1).iterator
    val result = mutable.ListBuffer.empty[Entry[K, (V1, V2)]]
    /*
    This whole method here is suuuuuper unreadable. It should be refactored and expressed as unfold.
    Unluckily, Scala doesn't have unfold so I'll have to check if cats has it or write it myself.
     */
    if (asSorted.hasNext && bsSorted.hasNext) {
      val (firstAKey, firstAEntries) = asSorted.next()
      val (firstBKey, firstBEntries) = bsSorted.next()
      var nextAKey = firstAKey
      var nextAEntries = firstAEntries
      var nextBKey = firstBKey
      var nextBEntries = firstBEntries
      var aHadNext = true
      var bHadNext = true
      while (aHadNext && bHadNext) {
        ordering.compare(nextAKey, nextBKey) match {
          case i if i < 0 =>
            if (asSorted.hasNext) {
              val nextA = asSorted.next()
              nextAKey = nextA._1
              nextAEntries = nextA._2
              aHadNext = true
            } else {
              bHadNext = false
            }
          case i if i > 0 =>
            if (bsSorted.hasNext) {
              val nextB = bsSorted.next()
              nextBKey = nextB._1
              nextBEntries = nextB._2
              bHadNext = true
            } else {
              bHadNext = false
            }
          case 0 =>
            for {
              a <- nextAEntries
              b <- nextBEntries
            } {
              result += Entry(nextAKey, (a.value, b.value))
            }
            if (asSorted.hasNext) {
              val nextA = asSorted.next()
              nextAKey = nextA._1
              nextAEntries = nextA._2
              aHadNext = true
            } else {
              bHadNext = false
            }
            if (bsSorted.hasNext) {
              val nextB = bsSorted.next()
              nextBKey = nextB._1
              nextBEntries = nextB._2
              bHadNext = true
            } else {
              bHadNext = false
            }
        }
      }
      result.toList
    } else {
      Seq.empty
    }
  }

}
