package domain.wallet

import domain.{ Timestamp, UserAccountId, WalletEventId, WalletId }
import infrastructure.Event
import org.sisioh.baseunits.scala.money.Money

sealed trait WalletEvent extends Event {
  val id: WalletEventId
  val walletId: WalletId
  val userAccountId: UserAccountId
  val createdAt: Timestamp
}

case class MoneyDeposited(id: WalletEventId,
                          walletId: WalletId,
                          userAccountId: UserAccountId,
                          from: MoneyResource,
                          money: Money,
                          createdAt: Timestamp)
    extends WalletEvent

case class MoneyRequested(id: WalletEventId,
                          walletId: WalletId,
                          userAccountId: UserAccountId,
                          toId: WalletId,
                          money: Money,
                          createdAt: Timestamp)
    extends WalletEvent

case class MoneyWasRequested(id: WalletEventId,
                             walletId: WalletId,
                             userAccountId: UserAccountId,
                             fromId: WalletId,
                             money: Money,
                             createdAt: Timestamp)
    extends WalletEvent

case class MoneyPaid(id: WalletEventId,
                     walletId: WalletId,
                     userAccountId: UserAccountId,
                     toId: WalletId,
                     money: Money,
                     requestEventId: Option[WalletEventId],
                     createdAt: Timestamp)
    extends WalletEvent

case class MoneyWasPaid(id: WalletEventId,
                        walletId: WalletId,
                        userAccountId: UserAccountId,
                        fromId: WalletId,
                        money: Money,
                        requestEventId: Option[WalletEventId],
                        createdAt: Timestamp)
    extends WalletEvent
