package domain.contract

object AddWalletSpec extends ((Contract, WalletIds) => Boolean) {
  override def apply(contact: Contract, walletIds: WalletIds): Boolean =
    contact.plan.maxWallets > (contact.walletIds.size + walletIds.size)
}

object RemoveWalletSpec extends ((Contract, WalletIds) => Boolean) {
  override def apply(contact: Contract, walletIds: WalletIds): Boolean =
    contact.plan.minWallets < (contact.walletIds.size - walletIds.size)
}
