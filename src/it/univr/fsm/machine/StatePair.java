package it.univr.fsm.machine;

/**
 * State pair Class.
 * 
 * @author <a href="mailto:vincenzo.arceri@univr.it">Vincenzo Arceri</a>
 * @version 1.0
 * @since 24-10-2016
 */
public class StatePair {
	
	/**
	 * First state.
	 */
	private State first;
	
	/**
	 * Second state.
	 */
	private State second;
	
	/**
	 * Constructs a state pair.
	 */
	public StatePair(State first, State second) {
		this.first = first;
		this.second = second;
	}

	/**
	 * Gets the first state.
	 */
	public State getFirst() {
		return first;
	}
	
	/**
	 * Sets the first state.
	 */
	public void setFirst(State first) {
		this.first = first;
	}
	
	/**
	 * Gets the second state.
	 */
	public State getSecond() {
		return second;
	}
	
	/**
	 * Sets the second state.
	 */
	public void setSecond(State second) {
		this.second = second;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof StatePair) {
			return this.getFirst().equals(((StatePair) other).getFirst()) && this.getSecond().equals(((StatePair) other).getSecond());
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return (this.getFirst().toString() + this.getSecond().toString()).hashCode();
	}
}
