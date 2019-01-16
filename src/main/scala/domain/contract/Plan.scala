package domain.contract

import enumeratum._

import scala.collection.immutable

sealed trait Plan extends EnumEntry {
  val maxWallets: Int
  val minWallets: Int
}

object Plan extends Enum[Plan] {

  override def values: immutable.IndexedSeq[Plan] = findValues

  case object Personal extends Plan {
    override val minWallets: Int = 1
    override val maxWallets: Int = 1
  }
  case object Family extends Plan {
    override val minWallets: Int = 1
    override val maxWallets: Int = 10
  }

}
