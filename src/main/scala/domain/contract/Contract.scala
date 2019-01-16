package domain.contract

import domain.{ ContractId, Error, Result, Timestamp, UserAccountId, WalletId }
import domain.contract.Contract.WalletLimitOverError

case class WalletIds(values: Seq[WalletId]) {
  def add(other: WalletIds): WalletIds =
    WalletIds(values ++ other.values)
  def add(other: WalletId): WalletIds =
    add(WalletIds(Seq(other)))
  def remove(other: WalletIds): WalletIds =
    WalletIds(values.filterNot(v => other.contains(v)))
  def remove(other: WalletId): WalletIds =
    remove(WalletIds(Seq(other)))
  def contains(walletId: WalletId): Boolean = values.contains(walletId)
  def size: Int                             = values.size
}

object Contract {
  class WalletLimitOverError(val message: String) extends Error
}

case class Contract(id: ContractId, ownerId: UserAccountId, plan: Plan, walletIds: WalletIds, createdAt: Timestamp) {

  def addWalletIds(walletIds: WalletIds): Result[Contract] = {
    if (!AddWalletSpec(this, walletIds))
      Left(new WalletLimitOverError(s"walletIds = $walletIds"))
    else
      Right(
        copy(
          walletIds = this.walletIds.add(walletIds)
        )
      )
  }

  def removeWalletIds(walletIds: WalletIds): Result[Contract] = {
    if (!RemoveWalletSpec(this, walletIds))
      Left(new WalletLimitOverError(s"walletIds = $walletIds"))
    else
      Right(
        copy(
          walletIds = this.walletIds.remove(walletIds)
        )
      )
  }

}
