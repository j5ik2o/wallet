package domain.contract

import domain.{ ContractId, UserAccountId }

import scala.language.higherKinds

trait ContractRepository[M[_]] {

  def store(aggregate: Contract): M[Unit]

  def resolveById(id: ContractId): M[Contract]

  def resolveByUserAccountId(userAccountId: UserAccountId): M[Contract]

}
