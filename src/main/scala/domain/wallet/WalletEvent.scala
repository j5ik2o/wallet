package domain.wallet

import domain._
import infrastructure.Event
import org.sisioh.baseunits.scala.money.Money

sealed trait WalletEvent extends Event {
  val id: WalletEventId
  val walletId: WalletId
  val contractId: ContractId
  val userAccountId: UserAccountId
  val createdAt: Timestamp
}

case class WalletCreated(id: WalletEventId,
                         walletId: WalletId,
                         contractId: ContractId,
                         userAccountId: UserAccountId,
                         createdAt: Timestamp)
    extends WalletEvent

// チャージした
case class MoneyDeposited(id: WalletEventId,
                          walletId: WalletId,
                          contractId: ContractId,
                          userAccountId: UserAccountId,
                          from: MoneyResource,
                          money: Money,
                          createdAt: Timestamp)
    extends WalletEvent

// 請求した
case class MoneyRequested(id: WalletEventId,
                          walletId: WalletId,
                          contractId: ContractId,
                          userAccountId: UserAccountId,
                          toId: WalletId,
                          money: Money,
                          createdAt: Timestamp)
    extends WalletEvent

// 請求された
case class MoneyRequestReceived(id: WalletEventId,
                                walletId: WalletId,
                                contractId: ContractId,
                                userAccountId: UserAccountId,
                                fromId: WalletId,
                                money: Money,
                                createdAt: Timestamp)
    extends WalletEvent

// 支払った
case class MoneyPaid(id: WalletEventId,
                     walletId: WalletId,
                     contractId: ContractId,
                     userAccountId: UserAccountId,
                     toId: WalletId,
                     money: Money,
                     requestEventId: Option[WalletEventId],
                     createdAt: Timestamp)
    extends WalletEvent

// 支払われた
case class MoneyPaymentReceived(id: WalletEventId,
                                walletId: WalletId,
                                contractId: ContractId,
                                userAccountId: UserAccountId,
                                fromId: WalletId,
                                money: Money,
                                requestEventId: Option[WalletEventId],
                                createdAt: Timestamp)
    extends WalletEvent
