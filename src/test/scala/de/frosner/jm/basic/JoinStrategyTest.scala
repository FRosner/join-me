package de.frosner.jm.basic

import org.scalatest.FlatSpec
import org.scalatest.prop.TableDrivenPropertyChecks._
import org.scalatest.Matchers._

class JoinStrategyTest extends FlatSpec {

  private val strategies =
    Table[JoinStrategy](
      "strategy", // First tuple defines column names
      NestedLoopJoin
    )

  "Joining" should "combine two values with the same key" in {
    forAll(strategies) { strategy =>
      val left = Seq(
        Entry("a", 1)
      )
      val right = Seq(
        Entry("a", 2)
      )
      strategy.join(left, right) shouldBe Seq(
        Entry("a", (1, 2))
      )
    }
  }

  it should "combine multiple values with the same key (left)" in {
    forAll(strategies) { strategy =>
      val left = Seq(
        Entry("a", 1),
        Entry("a", 2)
      )
      val right = Seq(
        Entry("a", 2)
      )
      strategy.join(left, right) shouldBe Seq(
        Entry("a", (1, 2)),
        Entry("a", (2, 2))
      )
    }
  }

  it should "combine multiple values with the same key (right)" in {
    forAll(strategies) { strategy =>
      val left = Seq(
        Entry("a", 1)
      )
      val right = Seq(
        Entry("a", 2),
        Entry("a", 3)
      )
      strategy.join(left, right) shouldBe Seq(
        Entry("a", (1, 2)),
        Entry("a", (1, 3))
      )
    }
  }

  it should "not combine keys that don't match (left)" in {
    forAll(strategies) { strategy =>
      val left = Seq(
        Entry("a", 1),
        Entry("b", 2)
      )
      val right = Seq(
        Entry("a", 2)
      )
      strategy.join(left, right) shouldBe Seq(
        Entry("a", (1, 2))
      )
    }
  }

  it should "not combine keys that don't match (right)" in {
    forAll(strategies) { strategy =>
      val left = Seq(
        Entry("a", 1)
      )
      val right = Seq(
        Entry("a", 2),
        Entry("b", 3)
      )
      strategy.join(left, right) shouldBe Seq(
        Entry("a", (1, 2))
      )
    }
  }

  it should "combine multiple keys" in {
    forAll(strategies) { strategy =>
      val left = Seq(
        Entry("a", 1),
        Entry("b", 1)
      )
      val right = Seq(
        Entry("a", 2),
        Entry("b", 2)
      )
      strategy.join(left, right) shouldBe Seq(
        Entry("a", (1, 2)),
        Entry("b", (1, 2))
      )
    }
  }

}
