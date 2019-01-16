package domain.user

import domain.UserAccountId

import scala.language.higherKinds

trait UserAccountRepository[M[_]] {

  def store(aggregate: UserAccount): M[Unit]

  def resolveById(id: UserAccountId): M[UserAccount]

}
