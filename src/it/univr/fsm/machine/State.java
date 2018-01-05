package it.univr.fsm.machine;

/**
 * Automaton state class.
 * 
 * @author <a href="mailto:vincenzo.arceri@univr.it">Vincenzo Arceri</a>
 * @version 1.0
 * @since 24-10-2016
 */
public class State {

	/**
	 * Name of the state.
	 */
	private String state;

	private boolean isFinalState;
	private boolean isInitialState;

	/**
	 * Constructs a new state.
	 * 
	 * @param state the name of the state
	 * @param isInitialState 
	 * @param isFinalState
	 */
	public State(String state, boolean isInitialState, boolean isFinalState) {
		this.state = state;
		this.isFinalState = isFinalState;
		this.isInitialState = isInitialState;
	}

	/**
	 * Returns if the state is an initial state.
	 */
	public boolean isInitialState() {
		return isInitialState;
	}

	/**
	 * Sets the initial state.
	 */
	public void setInitialState(boolean isInitialState) {
		this.isInitialState = isInitialState;
	}

	/**
	 * Returns the name of the state.
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the name of the state.
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * Returns if the state is a final state.
	 */
	public boolean isFinalState() {
		return isFinalState;
	}

	/**
	 * Sets the state as a final state.
	 */
	public void setFinalState(boolean isFinalState) {
		this.isFinalState = isFinalState;
	}

	@Override
	public String toString() {
		return "[" + this.getState() + "]";
	}

	@Override
	public boolean equals(Object other) {
		if (other instanceof State) 
			return this.getState().equals(((State) other).getState());
		return false;
	}

	@Override
	public int hashCode() {
		return this.getState().hashCode();
	}

	@Override
	public State clone() {
		return new State(this.getState(), this.isInitialState, this.isFinalState);
	}
}
