package domain.wallet

import domain.WalletId
import infrastructure.EventStoreOnMemory
import monix.eval.Task

import scala.collection.mutable
import scala.language.higherKinds

trait WalletRepository[M[_]] {
  def addListeners(listeners: Seq[WalletEvent => Unit]): Unit
  def store(aggregate: Wallet): M[Unit]
  def resolveById(id: WalletId): M[Wallet]
}

class WalletRepositoryOnMemory extends WalletRepository[Task] {
  private val listeners: mutable.Seq[WalletEvent => Unit] = mutable.Seq.empty

  private val eventStore = new EventStoreOnMemory[WalletEvent]()

  override def addListeners(listeners: Seq[WalletEvent => Unit]): Unit = this.listeners ++ listeners

  override def store(aggregate: Wallet): Task[Unit] =
    eventStore.add(aggregate.id, aggregate.events.breachEncapsulationOfEvents).doOnFinish { _ =>
      fireEvents(aggregate)
    }

  override def resolveById(id: WalletId): Task[Wallet] = eventStore.iterator(id).map { events =>
    Wallet(WalletEvents(events.toSeq))
  }

  private def fireEvents(aggregate: Wallet) = {
    Task {
      aggregate.events.breachEncapsulationOfEvents.reverse.foreach { event =>
        listeners.foreach(_(event))
      }
    }
  }

}
