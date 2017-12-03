package it.univr.fsm.machine;

import java.util.HashSet;

/**
 * Transition Class.
 * 
 * @author <a href="mailto:vincenzo.arceri@univr.it">Vincenzo Arceri</a>
 * @version 1.0
 * @since 24-10-2016
 */
public class Transition {
	/**
	 * Exiting state.
	 */
	private State from;
	
	/**
	 * Entering state.
	 */
	private State to;
	
	/**
	 * Input string.
	 */
	private String input;
	
	/**
	 * Output string.
	 */
	private String output;
	
	public HashSet<State> pfrom;
	public HashSet<State> pto;
	
	/**
	 * Constructs a new transition.
	 */
	public Transition(State from, State to, String input, String output) {
		this.from = from;
		this.to = to;
		this.input = input;
		this.output = output;
	}
	
	/**
	 * Returns the exiting state.
	 */
	public State getFrom() {
		return from;
	}
	
	/**
	 * Sets the exiting state.
	 */
	public void setFrom(State from) {
		this.from = from;
	}
	
	/**
	 * Returns the entering state.
	 */
	public State getTo() {
		return to;
	}
	
	/**
	 * Sets the entering state.
	 */
	public void setTo(State to) {
		this.to = to;
	}
	
	/**
	 * Gets the input string.
	 */
	public String getInput() {
		return input;
	}
	
	/**
	 * Sets the input string.
	 */
	public void setInput(String input) {
		this.input = input;
	}
	
	/**
	 * Gets the output string.
	 */
	public String getOutput() {
		return output;
	}
	
	/**
	 * Sets the output string.
	 */
	public void setOutput(String output) {
		this.output = output;
	}
	
	/**
	 * Fires the transition with a given input.
	 * 
	 * @param s the input.
	 * @return the reached state if the transition is firable from the state, null otherwise.
	 */
	public State fire(String s) {
		if (this.getInput().equals(s))
			return this.getTo();
		return null;
	}
	
	/**
	 * Returns if the transition is firable from a given state with a given input.
	 * @param from the state.
	 * @param input the input.
	 */
	public boolean isFirable(State from, String input) {
		if (this.getFrom().equals(from) && (this.getInput().equals(input) || this.getInput().equals("")))
			return true;
		return false;
	}
		
	/**
	 * Returns if the transition is an epsilon trnasition.
	 */
	public boolean isEpsilonTransition() {
		return this.getInput().equals("");
	}
	
	@Override
	public String toString() {
		return from.getState().toString() + " " + input + " -> " + output + " " + to.getState().toString()+ "\n";
	}
	
	@Override
	public Transition clone() {
		return new Transition((State) this.getFrom().clone(), (State) this.getTo().clone(), this.getInput(), this.getOutput());
	}
	
	@Override
	public int hashCode() {
		return (this.getFrom() + "" + this.getTo() + "" +  this.getInput()).hashCode(); 
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Transition) {
					return this.getFrom().equals(((Transition) other).getFrom()) &&
					this.getTo().equals(((Transition) other).getTo()) &&
					this.getInput().equals(((Transition) other).getInput()) &&
					this.getOutput().equals(((Transition) other).getOutput());
		}
		
		return false;
	}
}
