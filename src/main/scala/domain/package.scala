import java.time.ZonedDateTime
package object domain {

  type WalletId      = String
  type WalletEventId = String
  type ContractId    = String
  type UserAccountId = String
  type EmailAddress  = String
  type Timestamp     = ZonedDateTime

  type Result[A] = Either[Error, A]
}
