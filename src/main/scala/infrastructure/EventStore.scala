package infrastructure
import monix.eval.Task

import scala.collection.mutable

trait EventStore[E <: Event] {
  def add(aggregateId: String, event: E): Task[Unit]
  def add(aggregateId: String, events: Seq[E]): Task[Unit]
  def fetch(aggregateId: String): Task[E]
  def iterator(aggregateId: String): Task[Iterator[E]]
}

class EventStoreOnMemory[E <: Event] extends EventStore[E] {

  private val partitions: mutable.Map[String, mutable.Queue[E]] = mutable.Map.empty

  override def add(aggregateId: String, event: E): Task[Unit] = Task.pure {
    val queue = partitions.getOrElseUpdate(aggregateId, mutable.Queue.empty)
    queue.enqueue(event)
  }

  override def add(aggregateId: String, events: Seq[E]): Task[Unit] =
    Task.sequence(events.map(event => add(aggregateId, event))).map(_ => ())

  override def fetch(aggregateId: String): Task[E] = Task.pure {
    val queue = partitions.getOrElseUpdate(aggregateId, mutable.Queue.empty)
    queue.dequeue()
  }

  override def iterator(aggregateId: String): Task[Iterator[E]] = Task.pure {
    val queue = partitions.getOrElseUpdate(aggregateId, mutable.Queue.empty)
    queue.iterator
  }
}
