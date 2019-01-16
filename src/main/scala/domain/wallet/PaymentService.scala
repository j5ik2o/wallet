package domain.wallet

import java.time.ZonedDateTime

import domain.{ Result, WalletEventId }
import org.sisioh.baseunits.scala.money.Money

object PaymentService {

  case class FromTo(from: Wallet, to: Wallet)

  def execute(from: Wallet,
              to: Wallet,
              money: Money,
              requestEventId: Option[WalletEventId] = None,
              createdAt: ZonedDateTime = ZonedDateTime.now()): Result[FromTo] = {
    for {
      newFrom <- from.pay(to.id, money, requestEventId, createdAt)
      newTo   <- to.wasPaid(from.id, money, requestEventId, createdAt)
    } yield FromTo(newFrom, newTo)
  }

}
