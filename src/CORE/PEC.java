package CORE;

import java.util.PriorityQueue;

/**
 * Represents the Priority Event Calendar (PEC) for the simulation.
 * The PEC is a data structure that stores and manages events in chronological order,
 * ensuring that the event with the earliest scheduled time is always processed next.
 * It uses a {@link PriorityQueue} internally to maintain this order.
 */
public class PEC {
	/**
	 * A priority queue storing {@link Event} objects.
	 * Events are ordered based on their scheduled time, with the earliest event
	 * having the highest priority.
	 */
	private PriorityQueue<Event> eventos;

	/**
	 * Constructs a new Priority Event Calendar.
	 * Initializes an empty priority queue to store events.
	 */
	public PEC() {
		this.eventos = new PriorityQueue<>();
	}

	/**
	 * Adds a new event to the Priority Event Calendar.
	 * The event will be automatically placed in the correct chronological order
	 * within the queue.
	 *
	 * @param aEvent The {@link Event} to be added to the PEC.
	 */
	public void addEvent(Event aEvent) {
		eventos.add(aEvent);
	}

	/**
	 * Retrieves and removes the next event from the Priority Event Calendar.
	 * This is the event with the earliest scheduled time.
	 *
	 * @return The next {@link Event} to be processed, or {@code null} if the PEC is empty.
	 */
	public Event getNextEvent() {
		return eventos.poll(); // Remove e devolve o evento mais próximo
	}

	/**
	 * Checks if there are any events remaining in the Priority Event Calendar.
	 *
	 * @return {@code true} if the PEC contains one or more events, {@code false} otherwise.
	 */
	public boolean hasEvents() {
		return !eventos.isEmpty();
	}

	/**
	 * Peeks at the scheduled time of the next event in the Priority Event Calendar
	 * without removing it.
	 *
	 * @return The time of the next {@link Event}, or -1 if the PEC is empty.
	 */
	public double peekNextEvent() {
		if (eventos.isEmpty()) return -1;
		return eventos.peek().getTime();
	}
}