package it.univr.fsm.machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Finite-state transducer Class.
 * 
 * @author <a href="mailto:vincenzo.arceri@univr.it">Vincenzo Arceri</a>
 * @version 1.0
 * @since 24-10-2016
 */
public class Transducer {

	/**
	 * The initial state.
	 */
	private State initialState;

	/**
	 * The set of transitions.
	 */
	private HashSet<Transition> delta;

	/**
	 * The set of states.
	 */
	private HashSet<State> states;

	/**
	 * The final state.
	 */
	private State finalState;

	/**
	 * Constructs a new transducer.
	 */
	public Transducer(State initialState, HashSet<Transition> delta, HashSet<State> states)  {
		this.initialState = initialState;
		this.delta = delta;
		this.states = states;
	}

	/**
	 * Transduction of a string.
	 * @param s the string.
	 * @return the output string.
	 */
	public String transduce(String s) {
		return transduce(s, initialState);
	}

	/**
	 * Transduce a string starting from a given state.
	 * 
	 * @param s the string
	 * @param state the starting state.
	 * @return the output string is the string is accepted, empty string otherwise.
	 */
	public String transduce(String s, State state) {
		ArrayList<String> input = (ArrayList<String>) toList(s);
		String result = "";

		HashSet<String> results = new HashSet<String>();

		State currentState = state;
		boolean found = false;

		while (!input.isEmpty()) {
			found = false;

			for (Transition t: delta) {
				if (t.isFirable(currentState, input.get(0))) {
					results.add(trySinglePath(input, t.getTo(), t.getOutput()));


				} else if (t.getInput().equals("") && t.getFrom().equals(currentState)) { // Empty transition
					currentState = t.fire("");
					result += t.getOutput();
					found = true;
					break;
				} else
					found = false;
			}


			if (found)
				continue;

			return null;
		}


		System.out.println(results);

		if (currentState.isFinalState())
			return result;
		return null;
	}

	private String trySinglePath(ArrayList<String> input, State initialState, String output) {
		String result = "";
		result += output;
		input.remove(0);
		result += transduce(input.toString());
		return result;
	}

	/**
	 * Returns the string as an array of chars.
	 */
	public static List<String> toList(String s) {
		ArrayList<String> result = new ArrayList<String>();

		for (int i = 0; i < s.length(); ++i){
			result.add(s.substring(i, i+1));
		}

		return result;
	}

	/**
	 * Returns the automaton recognized the output language of the transducer.
	 */
	public Automaton FA_O() {
		HashSet<State> newStates = (HashSet<State>) new HashSet<State>(); // this.states.clone();
		HashSet<Transition> newGamma = new HashSet<Transition>();
		Automaton automaton = null;
		State newInitialState = null, from, to;

		for (Transition t : this.delta) {
			newStates.add(from = t.getFrom().clone());
			newStates.add(to = t.getTo().clone());

			if (to.isInitialState())
				newInitialState = to;

			if (from.isInitialState())
				newInitialState = from;

			newGamma.add(new Transition(from, to, t.getOutput(), ""));
		}

		automaton =  new Automaton(newGamma, newStates);
		return automaton;

		/*
		for (State state: this.states) {
			newStates.add(new State(state.getState(), state.isInitialState() ,state.isFinalState()));
		}


		for (State state: newStates) {
			if (state.isInitialState()) {
				automaton = new Automaton(state, null, newStates);
				break;
			}
		}


		for (Transition t: this.gamma) 
			newGamma.add(new Transition(automaton.getState(t.getFrom().getState()), automaton.getState(t.getTo().getState()), t.getOutput(), ""));
		 */



		//automaton.setGamma(newGamma);

	}
	
	/**
	 * Transduction operation of two composed transducers.
	 * 
	 * @param t1 first transducer.
	 * @param t2 second transducer.
	 * @param input input string.
	 * 
	 * @return the output string if it is accepted, empty string otherwise.
	 */
	public static String transduceInComposition(Transducer t1, Transducer t2, String input) {
		return t2.transduce(t1.transduce(input));
	}

	@Override
	public String toString() {
		String result = "";

		for (State s: this.states) {

			if (!this.getOutgoingTransitionsFrom(s).isEmpty() || s.isFinalState()) {
				result += "[" +  s.getState() + "] " + (s.isFinalState() ? "[accept]" : "[reject]" + (s.isInitialState() ? "[initial]\n" : "\n"));
				for (Transition t : this.getOutgoingTransitionsFrom(s))
					result += "\t" + s + " " + t.getInput() + " " + t.getOutput() + " -> " + t.getTo() + "\n";  
			}
		}
		return result;
	}

	/**
	 * Join transducers composition.
	 * 
	 * @param t1 first transducer.
	 * @param t2 second transducer.
	 * @return a new transducer corresponding to the join composition of two transducer.
	 */
	public static Transducer compose(Transducer t1, Transducer t2) {
		HashSet<Transition> newGamma= new HashSet<Transition>();		
		HashSet<State> newStates = new HashSet<State>();
		HashMap<String, State> statesMap = new HashMap<String, State>();

		State initialState = null, finalState = null;

		for (State s1: t1.states)
			for (State s2: t2.states) {
				State curr = new State(s1.getState() + "x" + s2.getState(), s1.isInitialState() && s2.isInitialState(), s1.isFinalState() && s2.isFinalState());
				newStates.add(curr);
				statesMap.put(s1.getState() + "x" + s2.getState(), curr);

				if (s1.isInitialState() && s2.isInitialState()) 
					initialState = curr;

				if (s1.isFinalState() && s2.isFinalState()) 
					finalState = curr;		
			}

		// First condition
		for (Transition i1: t1.delta)
			for (Transition i2: t2.delta) 
				if (i1.getOutput().equals(i2.getInput()) && !i1.getOutput().equals(""))  {
					newGamma.add(new Transition(statesMap.get(i1.getFrom().getState() + "x" + i2.getFrom().getState()), statesMap.get(i1.getTo().getState() + "x" + i2.getTo().getState()) , i1.getInput(), i2.getOutput()));
				}
		// Second condition
		for (Transition i1: t1.delta)
			if (i1.getOutput().equals("")) 
				for (State s: t2.states)
					newGamma.add(new Transition(statesMap.get(i1.getFrom().getState() + "x" + s.getState()), statesMap.get(i1.getTo().getState() + "x" + s.getState()), i1.getInput(), ""));

		// Third condition
		for (Transition i2: t2.delta)
			if (i2.getInput().equals("")) 
				for (State s: t1.states) 
					newGamma.add(new Transition(statesMap.get(s.getState() + "x" + i2.getFrom().getState()), statesMap.get(s.getState() + "x" + i2.getTo().getState()), "", i2.getOutput()));

		Transducer t = new Transducer(initialState, newGamma, newStates).deMerge();
		t.setFinalState(finalState);
		return t;
	}

	public HashSet<Transition> getOutgoingTransitionsFrom(State s) {
		HashSet<Transition> result = new HashSet<Transition>();

		for (Transition t : this.delta)
			if (t.getFrom().equals(s))
				result.add(t);

		return result;
	}

	private Transducer deMerge() {
		HashSet<State> newStates = new HashSet<State>();
		HashSet<Transition> newGamma = new HashSet<Transition>();
		HashMap<String, State> map = new HashMap<String, State>();
		State newInitialState = null, newFinalState = null;

		int counter = 0;

		for (State s : this.states) {
			State newState = new State("r" + counter, s.isInitialState(), s.isFinalState());
			map.put(s.getState(), newState);
			newStates.add(newState);
			counter++;

			if (s.isInitialState())
				newInitialState = newState;

			if (s.isFinalState())
				newFinalState = newState;
		}

		for (Transition t : this.delta)
			newGamma.add(new Transition(map.get(t.getFrom().getState()), map.get(t.getTo().getState()), t.getInput(), t.getOutput()));

		Transducer t =  new Transducer(newInitialState, newGamma, newStates);
		t.setFinalState(newFinalState);
		return t;
	}

	/**
	 * Gets the initial state.
	 */
	public State getInitialState() {
		return initialState;
	}

	/**
	 * Sets the initial state.
	 */
	public void setInitialState(State initialState) {
		this.initialState = initialState;
	}

	/**
	 * Gets the states of the transducer.
	 */
	public HashSet<State> getStates() {
		return states;
	}

	/**
	 * Gets the state from a given name.
	 */
	public State getState(String name) {

		for (State s : this.states) 
			if (s.getState().equals(name))
				return s;
		return null;
	}

	/**
	 * Sets the states of the transducer.
	 */
	public void setStates(HashSet<State> states) {
		this.states = states;
	}

	/**
	 * Gets the transitions of the transducer.
	 */
	public HashSet<Transition> getDelta() {
		return delta;
	}

	/**
	 * Sets the transitions of the transducer.
	 */
	public void setDelta(HashSet<Transition> delta) {
		this.delta = delta;
	}

	/**
	 * Gets the final state of the transducer.
	 */
	public State getFinalState() {
		return finalState;
	}

	/**
	 * Sets the final state of the transducer.
	 */
	public void setFinalState(State finalState) {
		this.finalState = finalState;
	}
}
