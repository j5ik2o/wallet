package domain.wallet

import java.time.ZonedDateTime

import domain.{ Result, WalletEventId }
import org.sisioh.baseunits.scala.money.Money

object RequestService {

  def execute(from: Wallet,
              to: Wallet,
              money: Money,
              requestEventId: Option[WalletEventId] = None,
              createdAt: ZonedDateTime = ZonedDateTime.now()): Result[(Wallet, Wallet, String)] = {
    for {
      newFromWithRequestEventId <- from.request(to.id, money, createdAt = createdAt)
      newTo                     <- to.receiveRequest(from.id, money, createdAt = createdAt)
    } yield (newFromWithRequestEventId._1, newTo, newFromWithRequestEventId._2)
  }

}
