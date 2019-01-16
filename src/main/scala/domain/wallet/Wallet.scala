package domain.wallet

import java.time.ZonedDateTime

import domain._
import domain.wallet.Wallet.{ InvalidBalanceError, InvalidCurrencyError }
import io.azam.ulidj.ULID
import org.sisioh.baseunits.scala.money.Money

object Wallet {

  class InvalidCurrencyError(val message: String) extends Error
  class InvalidBalanceError(val message: String)  extends Error
  class InvalidPaymentError(val message: String)  extends Error

  def apply(events: WalletEvents): Wallet = WalletImpl(events)

}

case class WalletEvents(breachEncapsulationOfEvents: Seq[WalletEvent]) {

  private val values = breachEncapsulationOfEvents

  def walletId: WalletEventId      = values.last.id
  def userAccountId: UserAccountId = values.last.userAccountId
  def createdAt: Timestamp         = values.last.createdAt

  def add(other: WalletEvents): WalletEvents = WalletEvents(other.values ++ values)

  def add(other: WalletEvent): WalletEvents = WalletEvents(other +: values)

}

trait MoneyResource

case class CreditCard(id: String, due: String, name: String) extends MoneyResource

trait Wallet {
  // ドメインイベントのコレクション
  def events: WalletEvents

  // ドメインの状態
  def id: WalletId
  def userAccountId: UserAccountId
  def balance: Money
  def createdAt: Timestamp

  // チャージする
  def deposit(from: MoneyResource, money: Money, createdAt: Timestamp = ZonedDateTime.now()): Result[Wallet]
  // 請求する
  def request(toId: WalletId, money: Money, createdAt: Timestamp = ZonedDateTime.now()): Result[(Wallet, WalletEventId)]
  // 請求される
  def wasRequested(fromId: WalletId, money: Money, createdAt: Timestamp = ZonedDateTime.now()): Result[Wallet]
  // 支払う
  def pay(toId: WalletId,
          money: Money,
          requestEventId: Option[WalletEventId] = None,
          createdAt: Timestamp = ZonedDateTime.now()): Result[Wallet]
  // 支払われる
  def wasPaid(fromId: WalletId,
              money: Money,
              requestEventId: Option[WalletEventId],
              createdAt: Timestamp = ZonedDateTime.now()): Result[Wallet]

}

case class WalletImpl(events: WalletEvents, snapshotBalanace: Money = Money.zero(Money.JPY)) extends Wallet {

  override lazy val id: WalletEventId            = events.walletId
  override lazy val userAccountId: UserAccountId = events.userAccountId
  override lazy val createdAt: Timestamp         = events.createdAt
  override lazy val balance: Money = {
    // FIXME: ファーストクラスコレクションのリファクタ
    events.breachEncapsulationOfEvents.reverse.foldLeft(snapshotBalanace) {
      case (r, MoneyDeposited(_, _, _, _, money, _)) =>
        r.plus(money)
      case (r, MoneyPaid(_, _, _, _, money, _, _)) =>
        r.minus(money)
      case (r, MoneyWasPaid(_, _, _, _, money, _, _)) =>
        r.plus(money)
      case (r, _) =>
        r
    }
  }

  override def deposit(from: MoneyResource, money: Money, createdAt: Timestamp): Result[Wallet] =
    money match {
      case m if m.currency != balance.currency =>
        Left(new InvalidCurrencyError("Invalid currency"))
      case m if balance.plus(money).isNegative =>
        Left(new InvalidBalanceError(s"from: $from, money: $money"))
      case _ =>
        val event = MoneyDeposited(ULID.random(), id, userAccountId, from, money, createdAt)
        Right(
          copy(
            events = events.add(event)
          )
        )
    }

  override def wasRequested(fromId: WalletId, money: Money, createdAt: Timestamp): Result[Wallet] =
    money match {
      case m if m.currency != balance.currency =>
        Left(new InvalidCurrencyError("Invalid currency"))
      case _ =>
        val event = MoneyWasRequested(ULID.random(), id, userAccountId, fromId, money, createdAt)
        Right(
          copy(events = events.add(event))
        )
    }

  override def request(toId: WalletId, money: Money, createdAt: Timestamp): Result[(Wallet, WalletEventId)] =
    money match {
      case m if m.currency != balance.currency =>
        Left(new InvalidCurrencyError("Invalid currency"))
      case _ =>
        val event = MoneyRequested(ULID.random(), id, userAccountId, toId, money, createdAt)
        Right(
          (copy(events = events.add(event)), event.id)
        )
    }

  override def pay(toId: WalletId,
                   money: Money,
                   requestEventId: Option[WalletEventId] = None,
                   createdAt: Timestamp): Result[Wallet] = money match {
    case m if m.currency != balance.currency =>
      Left(new InvalidCurrencyError("Invalid currency"))
    case m if balance.minus(money).isNegative =>
      Left(new InvalidBalanceError(s"fromId: $id, toId: $toId, money: $money"))
    case _ =>
      val event = MoneyPaid(ULID.random(), id, userAccountId, toId, money, requestEventId, createdAt)
      Right(
        copy(
          events = events.add(event)
        )
      )
  }

  override def wasPaid(fromId: WalletId,
                       money: Money,
                       requestEventId: Option[WalletEventId],
                       createdAt: Timestamp): Result[Wallet] = money match {
    case m if m.currency != balance.currency =>
      Left(new InvalidCurrencyError("Invalid currency"))
    case m if balance.plus(money).isNegative =>
      Left(new InvalidBalanceError(s"fromId: $fromId, toId: $id, money: $money"))
    case _ =>
      val event = MoneyWasPaid(ULID.random(), id, userAccountId, fromId, money, requestEventId, createdAt)
      Right(
        copy(
          events = events.add(event)
        )
      )
  }
}
