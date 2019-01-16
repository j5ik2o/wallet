package domain.contract

import domain.ContractId

import scala.language.higherKinds

trait ContractRepository[M[_]] {

  def store(aggregate: Contract): M[Unit]

  def resolveById(id: ContractId): M[Contract]

}
