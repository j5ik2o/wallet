package domain.user

import domain.{ EmailAddress, Timestamp, UserAccountId }

case class UserAccount(id: UserAccountId, emailAddress: EmailAddress, createdAt: Timestamp)
