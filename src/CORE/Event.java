package CORE;

import MODE.Individual;
/**
 * Represents a scheduled event within the simulation.
 * Each event has a specific execution time, an associated individual (which may be null),
 * and an {@link Event_Strategy} that defines the behavior to be performed when the event occurs.
 * <p>
 * This class implements {@link Comparable} to allow events to be ordered, typically
 * by their execution time, which is crucial for event-driven simulation.
 */
public class Event implements Comparable<Event> {
    /**
     * The scheduled time at which this event is to occur.
     * Events with lower time values are processed earlier.
     */
	private int time;
    /**
     * The individual (e.g., an agent, a moving entity) associated with this event.
     * This can be {@code null} if the event is not specific to a particular individual.
     */
	private Individual individual;
    /**
     * The strategy that defines the action to be performed when this event is executed.
     * This encapsulates the specific behavior of the event.
     */
	private Event_Strategy strategy;
    /**
     * Constructs a new Event with the specified time, individual, and event strategy.
     *
     * @param time The scheduled time for this event to occur.
     * @param individual The individual associated with this event (can be {@code null}).
     * @param strategy The {@link Event_Strategy} defining the action for this event.
     */
	public Event(int time, Individual individual, Event_Strategy strategy) {
		this.time = time;
		this.individual = individual;
		this.strategy = strategy;
	}
    /**
     * Returns the scheduled time of this event.
     * @return The time of the event.
     */
	public int getTime() {
		return time;
	}
    /**
     * Returns the individual associated with this event.
     * @return The {@link Individual} associated with the event, or {@code null} if none.
     */
	public Individual getIndividual() {
		return individual;
	}
    /**
     * Executes the action defined by this event's strategy.
     * This method delegates the actual behavior to the {@link Event_Strategy}
     * associated with this event, passing the simulation context and the individual.
     *
     * @param simContext The {@link Simulation_Context} providing access to shared simulation state.
     */
	public void execute(Simulation_Context simContext) {
		strategy.execute(simContext, individual);
	}
    /**
     * Compares this event with the specified event for order.
     * Events are ordered primarily by their time. If times are equal,
     * a secondary comparison criteria (e.g., individual hash code or strategy hash code)
     * could be added for a stable sort, though for simplicity, only time is used here.
     *
     * @param other The event to be compared.
     * @return A negative integer, zero, or a positive integer as this event's time
     * is less than, equal to, or greater than the specified event's time.
     */
	@Override
	public int compareTo(Event other) {
		return Integer.compare(this.time, other.time);
	}

	@Override
	public String toString() {
		return "Event at t=" + time + " for individual=" + individual;
	}
}
