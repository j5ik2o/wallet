package useCase

import cats.MonadError
import cats.implicits._
import domain.contract.{ ContractRepository, WalletIds }
import domain.wallet.{ Wallet, WalletRepository }
import domain.{ Error, UserAccountId }
import io.azam.ulidj.ULID

case class AddWalletDto(userAccountId: UserAccountId)

class AddWalletUseCase[M[_]](contractRepository: ContractRepository[M], walletRepository: WalletRepository[M])(
    implicit ME: MonadError[M, Error]
) {

  def execute(dto: AddWalletDto): M[Unit] = {
    for {
      contract <- contractRepository.resolveByUserAccountId(dto.userAccountId)
      wallet   <- ME.pure(Wallet(ULID.random(), contract.id, contract.ownerId))
      newContract <- contract.addWalletIds(WalletIds(wallet.id)) match {
        case Right(v) => ME.pure(v)
        case Left(e)  => ME.raiseError(e)
      }
      _ <- contractRepository.store(newContract)
      _ <- walletRepository.store(wallet)
    } yield ()
  }

}
