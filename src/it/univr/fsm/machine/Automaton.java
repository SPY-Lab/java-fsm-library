package it.univr.fsm.machine;

import org.apache.commons.collections4.MultiMap;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.commons.lang3.StringUtils;

import it.univr.exception.*;
import it.univr.fsm.config.Config;

import it.univr.fsm.equations.Comp;
import it.univr.fsm.equations.Equation;
import it.univr.fsm.equations.GroundCoeff;
import it.univr.fsm.equations.Or;
import it.univr.fsm.equations.RegularExpression;
import it.univr.fsm.equations.Var;



import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Finite-state automaton class.
 * 
 * @author <a href="mailto:vincenzo.arceri@univr.it">Vincenzo Arceri</a>
 * @version 1.0
 * @since 24-10-2016
 */
public class Automaton {

	/**
	 * Starting symbol to name the states.
	 */
	public static char initChar = 'a';

	/**
	 * Initial state.
	 */
	private State initialState;

	private HashSet<State> initialStates;

	/**
	 * Set of transitions between states.
	 */
	private HashSet<Transition> delta;

	/**
	 * Set of states.
	 */
	private HashSet<State> states;
	
	/**
	 * Adjacency list
	 */
	private HashMap<State, HashSet<Transition>> adjacencyList;

	/**
	 * Constructs a new automaton.
	 * 
	 * @param initialState the initial state
	 * @param delta the set of transitions
	 * @param states the set of states
	 */
	@Deprecated
	public Automaton(State initialState, HashSet<Transition> delta, HashSet<State> states)  {
		this.initialState = initialState;
		this.delta = delta;
		this.states = states;
		this.adjacencyList = this.computeAdjacencyList();
	}
	
	private HashMap<State, HashSet<Transition>> computeAdjacencyList() {	
		HashMap<State, HashSet<Transition>> result = new HashMap<State, HashSet<Transition>>();

		for (State s : this.getStates()) 
			result.put(s, this.recomputeOutgoingTransitionsFrom(s));

		return result;
	}
	
	public void updateAdjacencyList(State s) {
		this.adjacencyList.put(s, this.recomputeOutgoingTransitionsFrom(s));
	}

	
	
	/**
	 * Constructs a new automaton.
	 * 
	 * @param initialStates the set of initial states
	 * @param delta the set of transitions
	 * @param states the set of states
	 */
	public Automaton(HashSet<State> initialStates, HashSet<Transition> delta, HashSet<State> states)  {
		this.initialStates = initialStates;
		this.delta = delta;
		this.states = states;
	}

	
	
	/**
	 * Check whether an automaton is deterministic
	 * 
	 * @param a the automaton
	 * @return a boolean
	 */
	public static boolean isDeterministic(Automaton a){
		for(State s: a.states){
			HashSet<Transition> outgoingTranisitions = a.getOutgoingTransitionsFrom(s);
			for(Transition t: outgoingTranisitions){
				for(Transition t2: outgoingTranisitions)
					if(!t.getTo().equals(t2.getTo()) && t.getInput().equals(t2.getInput())) return false; 
			}
		}
		return true;
	}
	
	

	/**
	 * Check whether an automaton is contained in another
	 * 
	 * @param first the first automaton
	 * @param second the second automaton
	 * @return a boolean
	 * 
	 */
	public static boolean isContained(Automaton first, Automaton second){
		// first is contained in second if (first intersect !second) accepts empty language
		
		return Automaton.isEmptyLanguageAccepted(Automaton.intersection(first, Automaton.complement(second)));
	}
	

	
	/**
	 * Check whether a state is reachable from initial state
	 * 
	 * @param f the state
	 * @param a the automaton
	 * @return a boolean
	 */
	public static boolean isReachable(State f, Automaton a){
		HashSet<Transition> transitionSet;

		if(f.isInitialState()){
			return true;
		}else{
			for(State s: a.states){
				transitionSet = a.getTransitionFrom(s, f);
				
				if( transitionSet != null)
					for(Transition t: transitionSet)
						if(isReachable(t.getFrom(),a)) return true;
			}
			
			return false;
		}
	}
	
	
	
	/**
	 * Check whether an automaton accepts the empty language or not
	 * 
	 * @param automaton the automaton
	 * @return a boolean
	 */
	public static boolean isEmptyLanguageAccepted(Automaton automaton){
		return !automaton.getFinalStates().isEmpty() && !automaton.states.isEmpty();
	}
	
	
	/**
	 * Performs an intersection between multiple automatons
	 * 
	 * @param collection a collection of automatons
	 * @return the intersection
	 */
	public static Automaton intersection(Collection<Automaton> collection){
		Automaton a=null;

		for(Automaton aut: collection){
			a = (a == null) ? aut : Automaton.intersection(a, aut);

		}

		if(a!=null)
			a.minimize();
		return a;
	}
	
	
	
	/**
	 * Performs a concatenation between multiple automatons
	 * 
	 * @param collection a collection of automatons
	 * @return the concatenation
	 * 
	 * Warning: the collection should consider the order between the automatons since the concatenation is not commutative
	 * 
	 */
	public static Automaton concat(Collection <Automaton> collection){
		Automaton result = null;

		for (Automaton aut: collection) 
			result = (result == null) ? aut : Automaton.concat(result, aut);
		
		return result;
	}

	
	
	/**
	 * Performs a subtraction between multiple automatons
	 * 
	 * @param collection a collection of automatons
	 * @return the subtraction
	 * 
	 */
	public static Automaton minus(Collection<Automaton> collection){
		Automaton a=null;

		for(Automaton aut: collection){
			a = (a == null) ? aut : Automaton.minus(a, aut);
		}

		if(a!=null)
			a.minimize();
		return a;
	}



	/**
	 * Performs the difference between two automatons
	 * 
	 * @param first the first automaton
	 * @param second the second automaton
	 * @return the difference
	 */

	public static Automaton minus(Automaton first, Automaton second){
		// first \ second = first intersect !second

		Automaton a = Automaton.intersection(first, Automaton.complement(second));
		a.minimize();

		return a;
	}

	
	
	/**
	 * Performs an automaton creation from a file
	 * 
	 * @param path the path of the file
	 * @return a new automaton
	 */
	public static Automaton loadAutomaton(String path){

		BufferedReader br = null;

		HashMap<String, State> mapStates = new HashMap<String, State>();
		HashSet<Transition> delta = new HashSet<Transition>();
		HashSet<State> states = new HashSet<State>();
		HashSet<State> initialStates = new HashSet<State>();
		State initialState = null;

		State currentState;
		int lineNum;


		try{
			String currentLine;			
			br = new BufferedReader(new FileReader(path) );


			for(lineNum = 0; (currentLine = br.readLine()) != null ; lineNum++){
				String[] pieces;

				pieces=currentLine.split(" ");

				switch(lineNum){
				// here i will find all the states
				case 0:
					for(String s: pieces){
						mapStates.put(s, currentState = new State(s,false,false));
						states.add(mapStates.get(s));
					}
					break;

					// initial states
				case 1:
					for(String s: pieces){
						currentState = mapStates.get(s);

						if(currentState==null) throw new MalformedInputException();

						currentState.setInitialState(true);
						initialStates.add(currentState);
						initialState = currentState;
						
					}
					break;

					// final states
				case 2:
					for(String s: pieces){
						currentState=mapStates.get(s);

						if(currentState==null) throw new MalformedInputException();

						currentState.setFinalState(true);
					}
					break;

					// transitions
				default:
					if(pieces.length!=3 || mapStates.get(pieces[0]) == null || mapStates.get(pieces[1])==null) throw new MalformedInputException();

					delta.add(new Transition(mapStates.get(pieces[0]),mapStates.get(pieces[1]),pieces[2],""));

					break;

				}

			}


		}catch(IOException e){
			e.printStackTrace();
			return null;
		}catch(MalformedInputException m){
			m.printStackTrace();
			return null;
		}finally{
			try{
				if(br != null)
					br.close();
			}catch(Exception c){
				c.printStackTrace();
			}
		}

		Automaton a= new Automaton(initialState,delta,states);
	
		return a;
	}

	
	
	/**
	 * Concats two Automatons
	 * 
	 * @param first the first automaton to merge
	 * @param second the second automaton to merge
	 * @return a new automaton, in which first is chained to second
	 * 
	 */

	
	
	public static Automaton concat(Automaton first, Automaton second){
		
		HashMap<State, State> mappingFirst = new HashMap<State,State>();
		HashMap<State, State> mappingSecond = new HashMap<State, State>();
		HashSet<Transition> newDelta = new HashSet<Transition>();
		HashSet<State> newStates = new HashSet<State>();

		State firstInitialStates = first.getInitialState();

		State partial;

		HashSet<State> firstFinalStates = first.getFinalStates();
		State secondInitialStates = second.getInitialState();

		int c = 0;


		mappingFirst.put(firstInitialStates, new State("q" + c++, true, false));
		newStates.add(mappingFirst.get(firstInitialStates));


		// Add all the first automaton states
		for (State s: first.states) {

			// The first automaton states are not final
			if (!s.isInitialState()) {
				mappingFirst.put(s, partial = new State("q" + c++, false, false));
				newStates.add(partial);
			}
		}

		mappingSecond.put(secondInitialStates, new State("q" + c++,false,false));
		newStates.add(mappingSecond.get(secondInitialStates));


		// Add all the second automaton states
		for (State s: second.states) {
			if (!s.isInitialState()) {
				mappingSecond.put(s, partial = new State("q" + c++, false, s.isFinalState()));
				newStates.add(partial);
			}
		}

		// Add all the first automaton transitions
		for (Transition t: first.delta)
			newDelta.add(new Transition(mappingFirst.get(t.getFrom()), mappingFirst.get(t.getTo()), t.getInput(), ""));

		// Add all the second automaton transitions
		for (Transition t: second.delta)
			newDelta.add(new Transition(mappingSecond.get(t.getFrom()), mappingSecond.get(t.getTo()), t.getInput(), ""));

		// Add the links between the first automaton final states and the second automaton initial state

		for (State f: firstFinalStates)
			newDelta.add(new Transition(mappingFirst.get(f), mappingSecond.get(secondInitialStates), "", ""));

		Automaton a= new Automaton(firstInitialStates, newDelta, newStates);
		//a.minimize();
		return a;

	}


	/**
	 * Performs the automata complement operation
	 * 
	 * @param  automaton the automata input
	 * @return the complement of the automata
	 */
	public static Automaton complement(Automaton automaton) {
		HashMap<State, State> mapping = new HashMap<State,State>();
		HashSet<Transition> newDelta = new HashSet<Transition>();
		HashSet<State> newStates = new HashSet<State>();

		State autNewInitialState = new State(automaton.getInitialState().getState(), automaton.getInitialState().isInitialState(), !automaton.getInitialState().isFinalState());
		mapping.put(automaton.getInitialState(), autNewInitialState);
		newStates.add(autNewInitialState);

		State partial;

		// Add states to the mapping, replacing accept states to reject
		for(State s: automaton.states) {
			mapping.put(s, partial = new State(s.getState(), s.isInitialState(), !s.isFinalState()));
			newStates.add(partial);
		}

		// Copying delta set
		for (Transition t:  automaton.delta)
			newDelta.add(new Transition(mapping.get(t.getFrom()), mapping.get(t.getTo()), t.getInput(), ""));

		return new Automaton(autNewInitialState,newDelta,newStates);
	}


	/**
	 * Does the automata intersection
	 * 
	 * @param first the first automata
	 * @param second the first automata
	 * @return a new automata, the intersection of the first and the second
	 */

	public static Automaton intersection(Automaton first, Automaton second) {

		// !(!(first) u !(second))
		Automaton notFirst = Automaton.complement(first);
		notFirst.minimize();
		Automaton notSecond = Automaton.complement(second);
		notSecond.minimize();

		Automaton union = Automaton.union(notFirst, notSecond);
		union.minimize();

		Automaton result = Automaton.complement(union);
		result.minimize();
		return result;
	}


	/**
	 * 
	 * @param path the path containing the automaton
	 * @return the automaton descripted in file
	 * @throws MalformedInputException whenever the file doesn't complain with the default pattern
	 * 
	 * Read and returns an Automaton from file. It must be formatted in the following way:
	 * 1st line: all the states
	 * 2nd line: initial states
	 * 3rd line: final states
	 * Transitions: From To Sym
	 *
	 * 
	 * 
	 */

	public static Automaton loadAutomata(String path){
		/*	
		 * This method follows this pattern in the file
		 * 	q0 q1 a
		 * 	q1 q2 b
		 * 	q2 q3 c 
		 */

		BufferedReader br = null;

		HashMap<String, State> mapStates = new HashMap<String, State>();
		HashSet<Transition> delta = new HashSet<Transition>();
		HashSet<State> states = new HashSet<State>();
		HashSet<State> initialStates = new HashSet<State>();

		State currentState;
		int lineNum;


		try{
			String currentLine;			
			br = new BufferedReader(new FileReader(path) );


			for(lineNum = 0; (currentLine = br.readLine()) != null ; lineNum++){
				String[] pieces;

				pieces=currentLine.split(" ");

				switch(lineNum){
				// here i will find all the states
				case 0:
					for(String s: pieces){
						mapStates.put(s, currentState = new State(s,false,false));
						states.add(mapStates.get(s));
					}
					break;

					// initial states
				case 1:
					for(String s: pieces){
						currentState = mapStates.get(s);

						if(currentState==null) throw new MalformedInputException();

						currentState.setInitialState(true);
						initialStates.add(currentState);
					}
					break;

					// final states
				case 2:
					for(String s: pieces){
						currentState=mapStates.get(s);

						if(currentState==null) throw new MalformedInputException();

						currentState.setFinalState(true);
					}
					break;

					// transitions
				default:
					if(pieces.length!=3) throw new MalformedInputException();

					if(mapStates.get(pieces[0])==null || mapStates.get(pieces[1])==null) throw new MalformedInputException();

					delta.add(new Transition(mapStates.get(pieces[0]),mapStates.get(pieces[1]),pieces[2],""));

					break;

				}

			}


		}catch(IOException e){
			e.printStackTrace();
			return null;
		}catch(MalformedInputException m){
			m.printStackTrace();
			return null;
		}finally{
			try{
				br.close();
			}catch(IOException c){
				System.err.println("Failed to close BufferedReader stream in readAutomataFromFile: " + c.getMessage() );
			}
		}

		Automaton a= new Automaton(initialStates,delta,states);
		a.minimize();
		return a;
	}


	/**
	 * Runs a string on the automaton.
	 * 
	 * @param s the string
	 * @return true if the string is accepted by the automaton, false otherwise
	 */
	public boolean run(String s) {
		return run(s, initialState);
	}


	/**
	 * Runs a string on the automaton starting from a given state.
	 * 
	 * @param s the string
	 * @param state the starting state
	 * @return true if the string is accepted by the automaton, false otherwise
	 */
	public boolean run(String s, State state) {
		ArrayList<String> input = (ArrayList<String>) toList(s);

		State currentState = state;
		boolean found = false;

		while (!input.isEmpty() || found) {
			found = false;

			for (Transition t: delta) {
				if (!input.isEmpty() && t.isFirable(currentState, input.get(0))) {
					currentState = t.fire(input.get(0));
					input.remove(0);
					found = true;
				} else if (t.getInput().equals("") && t.getFrom().equals(currentState)) { // Empty transition
					currentState = t.fire("");
					found = true;
					break;
				}
			}

			if (found)
				continue;
		}

		if (currentState.isFinalState())
			return true;
		return false;
	}

	/**
	 * Returns the set of transitions.
	 */
	public HashSet<Transition> getDelta() {
		return delta;
	}

	/**
	 * Sets the set of transition
	 */
	public void setDelta(HashSet<Transition> delta) {
		this.delta = delta;
	}

	/**
	 * Returns the string as an array of chars.
	 */
	public static List<String> toList(String s) {
		ArrayList<String> result = new ArrayList<String>();

		for (int i = 0; i < s.length(); ++i)
			result.add(s.substring(i, i+1));

		return result;
	}

	public void removeTransition(Transition t) {
		this.delta.remove(t);
	}

	/**
	 * Returns the transducer associated to this automaton.
	 * T(A)
	 */
	public Transducer toTransducer() {
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> gamma = new HashSet<Transition>();
		Transducer transducer = null;
		State newFinalState = null;

		for (State state: this.states) 
			states.add(new State(state.getState(), state.isInitialState(), state.isFinalState()));

		for (State state: states) {
			if (state.isInitialState())
				transducer = new Transducer(state, null, states);

			if (state.isFinalState())
				newFinalState = state;
		}


		for (Transition t: this.delta)
			gamma.add(new Transition(transducer.getState(t.getFrom().getState()), transducer.getState(t.getTo().getState()), t.getInput(), t.getInput()));

		transducer.setDelta(gamma);
		transducer.setFinalState(newFinalState);
		return transducer;

	}

	/**
	 * Renames the states of the automaton.
	 * @return the automaton with renamed states.
	 */
	private Automaton deMerge(char initChar) {
		HashSet<State> newStates = new HashSet<State>();
		HashSet<Transition> newGamma = new HashSet<Transition>();
		HashMap<String, State> mapping = new HashMap<String, State>();
		State newInitialState = null;

		int counter = 0;

		for (State s : this.states) {
			State newState = new State(String.valueOf(initChar) + counter, s.isInitialState(), s.isFinalState());
			mapping.put(s.getState(), newState);
			newStates.add(newState);
			counter++;

			if (s.isInitialState()) {
				newInitialState = newState;
			}
		}


		for (Transition t : this.delta)
			newGamma.add(new Transition(mapping.get(t.getFrom().getState()), mapping.get(t.getTo().getState()), t.getInput(), t.getOutput()));

		return new Automaton(newInitialState, newGamma, newStates);
	}

	/**
	 * Returns the state with the given name.
	 * 
	 * @param name the name of the state.
	 * @return the state with the given name.
	 */
	public State getState(String name) {

		for (State s : this.states) 
			if (s.getState().equals(name))
				return s;
		return null;
	}

	/**
	 * Builds an automaton from a given string.
	 * 
	 * @param s the string.
	 * @return a new automaton recognize the given string.
	 */
	public static Automaton makeAutomaton(String s) {


		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State initialState = new State("q0", true, false);
		states.add(initialState);

		if (s.isEmpty()) {
			initialState.setFinalState(true);
			return new Automaton(initialState, delta, states);
		}

		State state = initialState;


		ArrayList<String> input = (ArrayList<String>) toList(s);

		for (int i = 0; i < input.size(); ++i) {
			State next = new State("q" + (i+1), false, i == input.size() -1 ? true : false );
			states.add(next);		

			/*if (input.get(i).equals(" "))
				gamma.add(new Transition(state, next, "", ""));
			else	*/
			delta.add(new Transition(state, next, input.get(i), input.get(i)));

			state = next;
		}

		return new Automaton(initialState, delta, states);
	}

	/**
	 * Returns the transitions outgoing from a given state.
	 * 
	 * @param s the state
	 * @return an HashSet of transitions outgoing from the given state.
	 */
	public HashSet<Transition> recomputeOutgoingTransitionsFrom(State s) {
		HashSet<Transition> result = new HashSet<Transition>();

		for (Transition t : this.delta)
			if (t.getFrom().equals(s))
				result.add(t);

		return result;
	}
	
	public HashSet<Transition> getOutgoingTransitionsFrom(State s) {
		return this.adjacencyList.get(s);
	}

	/**
	 * Union operation between two automata.
	 * 
	 * @param a1 first automata.
	 * @param a2 second automata.
	 * @return the union of the two automata.
	 */
	public static Automaton union(Automaton a1, Automaton a2) {
		State newInitialState = new State("q", true, false);
		HashSet<Transition> newGamma = new HashSet<Transition>();
		HashSet<State> newStates = new HashSet<State>();

		int c = 1;
		HashMap<State, State> mappingA1 = new HashMap<State, State>(); 
		HashMap<State, State> mappingA2 = new HashMap<State, State>(); 

		newStates.add(newInitialState);

		State initialA1 = null; 
		State initialA2 = null;

		for (State s : a1.states) {
			State partial;
			newStates.add(partial = new State("q" + c++, false, s.isFinalState()));

			mappingA1.put(s, partial);

			if (s.isInitialState())
				initialA1 = partial;
		}

		for (State s : a2.states) {
			State partial;
			newStates.add(partial = new State("q" + c++, false, s.isFinalState()));	

			mappingA2.put(s, partial);

			if (s.isInitialState())
				initialA2 = partial;
		}

		for (Transition t : a1.delta)
			newGamma.add(new Transition(mappingA1.get(t.getFrom()), mappingA1.get(t.getTo()), t.getInput(), t.getOutput()));

		for (Transition t : a2.delta)
			newGamma.add(new Transition(mappingA2.get(t.getFrom()), mappingA2.get(t.getTo()), t.getInput(), t.getOutput()));

		newGamma.add(new Transition(newInitialState, initialA1, "", ""));
		newGamma.add(new Transition(newInitialState, initialA2, "", ""));

		Automaton a =  new Automaton(newInitialState, newGamma, newStates);
		a.minimize();
		return a;
	}

	/**
	 * Returns an automaton recognize any string.
	 */
	public static Automaton makeTopLanauge() {
		HashSet<State> newStates = new HashSet<State>();
		HashSet<Transition> newGamma = new HashSet<Transition>();
		State initialState = new State("q0", true, true);

		newStates.add(initialState);

		for (char alphabet = '!'; alphabet <= '~'; ++alphabet) 
			newGamma.add(new Transition(initialState, initialState, String.valueOf(alphabet), String.valueOf(alphabet)));

		return new Automaton(initialState, newGamma, newStates);
	}

	/**
	 * Returns an automaton recognize the empty language.
	 */
	public static Automaton makeEmptyLanguage() {

		HashSet<State> newStates = new HashSet<State>();
		HashSet<Transition> newGamma = new HashSet<Transition>();
		State initialState = new State("q0", true, false);

		newStates.add(initialState);

		for (char alphabet = '!'; alphabet <= '~'; ++alphabet) 
			newGamma.add(new Transition(initialState, initialState, String.valueOf(alphabet), String.valueOf(alphabet)));

		return new Automaton(initialState, newGamma, newStates);
	}

	/**
	 * Returns an automaton recognize the empty string.
	 */
	public static Automaton makeEmptyString() {

		HashSet<State> newStates = new HashSet<State>();
		HashSet<Transition> newDelta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, true);

		newStates.add(q0);
		newStates.add(q1);
		newStates.add(q2);

		newDelta.add(new Transition(q0, q1, "'", ""));
		newDelta.add(new Transition(q1, q2, "'", ""));


		return new Automaton(q0, newDelta, newStates);
	}

	/**
	 * Epsilon closure operation of a state.
	 * 
	 * @param s the state
	 * @return an HashSet of states reachable from the states by using only epsilon transition.
	 */
	public HashSet<State> epsilonClosure(State s) {
		HashSet<State> paths = new HashSet<State>();
		HashSet<State> previous = new HashSet<State>();
		HashSet<State> partial;
		paths.add(s);

		while (!paths.equals(previous)) {
			previous = (HashSet<State>) paths.clone();
			partial = new HashSet<State>();

			for (State reached : paths) 
				for (Transition t : this.getOutgoingTransitionsFrom(reached)) 
					if (t.isEpsilonTransition()) 
						partial.add(t.getTo());

			paths.addAll(partial);
		}

		return paths;
	}

	/**
	 * Determinization automata operation.
	 *  
	 * @return a new determinized automaton. 
	 */
	public Automaton determinize() {
		HashMap<HashSet<State>, Boolean> dStates = new HashMap<HashSet<State>, Boolean>();
		HashSet<Transition> dGamma = new HashSet<Transition>();
		HashSet<State> newStates = new HashSet<State>();

		dStates.put(epsilonClosure(initialState), false);
		HashSet<State> T;

		State newInitialState = new State(createName(epsilonClosure(initialState)), true, isPartitionFinalState(epsilonClosure(initialState)));

		newStates.add(newInitialState);

		while ((T = notMarked(dStates)) != null) {
			dStates.put(T, true);


			for (String alphabet: readableCharFromState(T)) {

				HashSet<State> newStateWithEpsilonTransition = new HashSet<State>();


				for (State s : T) 
					for (Transition t : this.getOutgoingTransitionsFrom(s)) 
						if (t.getInput().equals(String.valueOf(alphabet)))
							newStateWithEpsilonTransition.add(t.getTo());

				HashSet<HashSet<State>> newStateWithNoEpsilon = new HashSet<HashSet<State>>();

				for (State s : newStateWithEpsilonTransition)
					newStateWithNoEpsilon.add(this.epsilonClosure(s));


				HashSet<State> U = new HashSet<State>();

				for (HashSet<State> ps : newStateWithNoEpsilon) // Flatting hashsets
					for (State s : ps)
						U.add(s);

				if (!dStates.containsKey(U))
					dStates.put(U, false);
				else {
					for (HashSet<State> s : dStates.keySet())
						if (s.equals(U))
							U = s;
				}


				State from = new State(createName(T), false, isPartitionFinalState(T));
				State to = new State(createName(U), false, isPartitionFinalState(U));

				newStates.add(from);
				newStates.add(to);

				dGamma.add(new Transition(from, to, String.valueOf(alphabet), ""));
			}

		}

		Automaton a = (new Automaton(newInitialState, dGamma, newStates)).deMerge(++initChar);
		return a;
	}

	/**
	 * Returns the first set of states not marked in the set of sets of states powerset.
	 * 
	 * @param powerset the set of sets of states.
	 */
	private HashSet<State> notMarked(HashMap<HashSet<State>, Boolean> powerset) {

		for (HashSet<State> s : powerset.keySet())
			if (!powerset.get(s))
				return s;

		return null;
	}

	/**
	 * Returns true if at least one state of the partition states is a final state, false otherwise.
	 * 
	 * @param states state partition.
	 */
	private boolean isPartitionFinalState(HashSet<State> states) {

		for (State s : states)
			if (s.isFinalState())
				return true;
		return false;

	}

	/**
	 * Returns true if at least one state of the partition states is an initial state, false otherwise.
	 * 
	 * @param states state partition.
	 */
	private boolean isPartitionInitialState(HashSet<State> states) {

		for (State s : states)
			if (s.isInitialState())
				return true;
		return false;

	}

	/**
	 * RegEx printing.
	 */
	public String prettyPrint() {
		return this.toRegex().toString();
	}

	private String createName(HashSet<State> states) {
		String result = "";

		if (!states.isEmpty()) {

			for (State s : states) 				
				result += s.getState() + "x";

			result = result.substring(0, result.length() -1);
		}
		return result;
	}

	/**
	 * Returns the set of strings readable from the states state partition.
	 * 
	 * @param states state partition.
	 */
	private HashSet<String> readableCharFromState(HashSet<State> states) {

		HashSet<String> result = new HashSet<String>();

		for (State s : states)
			for (Transition t : this.getOutgoingTransitionsFrom(s)) 
				if (!t.getInput().equals(""))
					result.add(t.getInput());


		return result;

	}

	/**
	 * Returns the set of strings readable from the state s.
	 * 
	 * @param s state of this automaton.
	 */
	private HashSet<String> readableCharFromState(State s) {

		HashSet<String> result = new HashSet<String>();

		for (Transition t : this.getOutgoingTransitionsFrom(s)) 
			if (!t.getInput().equals(""))
				result.add(t.getInput());

		return result;

	}

	/**
	 * Removes the unreachable states of an automaton.
	 * 
	 * @return a new automaton without unreachable states.
	 */
	public Automaton removeUnreachableStates() {
		HashSet<State> reachableStates = new HashSet<State>();
		reachableStates.add(this.initialState);

		HashSet<State> newStates = new HashSet<State>();
		newStates.add(this.initialState);		

		do {
			HashSet<State> temp = new HashSet<State>();
			for (State s : newStates) {
				for (String alphabet : this.readableCharFromState(s))
					for (Transition t : this.getOutgoingTransitionsFrom(s))
						if (t.getFrom().equals(s) && t.getInput().equals(alphabet))
							temp.add(t.getTo());
			}

			temp.removeAll(reachableStates);
			newStates = temp;

			reachableStates.addAll(newStates);

		} while (!newStates.isEmpty());


		HashSet<State> statesToRemove = new HashSet<State>();
		for (State s : this.states)
			if (!reachableStates.contains(s))
				statesToRemove.add(s);

		HashSet<Transition> transitionsToRemove = new HashSet<Transition>();
		for (Transition t : this.delta)
			if (statesToRemove.contains(t.getFrom()) || statesToRemove.contains(t.getTo()))
				transitionsToRemove.add(t);

		HashSet<State> states = (HashSet<State>) this.states.clone();
		HashSet<Transition> gamma = (HashSet<Transition>) this.delta.clone();

		states.removeAll(statesToRemove);
		gamma.removeAll(transitionsToRemove);

		return new Automaton(this.initialState, gamma, states);

		/*
		 * 
		HashSet<State> statesToRemove = new HashSet<State>();
		HashSet<Transition> transitionsToRemove = new HashSet<Transition>();
		 for (State s : this.states)
			if (!reachableStates.contains(s))
				statesToRemove.add(s);

		for (Transition t : this.gamma)
			if (statesToRemove.contains(t.getFrom()) || statesToRemove.contains(t.getTo()))
				transitionsToRemove.add(t);

		this.states.removeAll(statesToRemove);
		this.gamma.removeAll(transitionsToRemove);*/
	}

	/**
	 * Brzozowski's minimization algorithm.
	 */
	public void minimize() {

		this.reverse();
		Automaton a = this.determinize();
		a = a.removeUnreachableStates();
		a.reverse();
		a = a.determinize();
		a = a.removeUnreachableStates();

		this.initialState = a.initialState;
		this.delta = a.delta;
		this.states = a.states;
		this.adjacencyList = this.computeAdjacencyList();
	}
	
	public static HashSet<String> getAlphabet(Automaton a){
		HashSet<String> alphabet = new HashSet<String>();
		
		for(Transition t : a.delta){
			if(!alphabet.contains(t.getInput()))
				alphabet.add(t.getInput());
		}
		
		return alphabet;
	}
	
	private State getOutgoingStatefromTransitionSymbol(HashSet<Transition> transitions, String symbol){
		for(Transition t : transitions){
			if(t.getInput().equals(symbol)){
				return t.getTo();
			}
		}
		return null;
	}
	
	public void hopcroftremoveUnreachableStates(){
		HashSet<State> unreachableStates = new HashSet<>();
		HashSet<State> reachableStates = (HashSet<State>) this.getInitialStates().clone();
		HashSet<State> newStates = (HashSet<State>) this.getInitialStates().clone();
		HashSet<State> temp;
		
		
		do{
			temp = new HashSet<>(Collections.<State>emptySet());
			for(State s : newStates){
				for(String a : getAlphabet(this)){
					HashSet<Transition> transition =  this.getOutgoingTransitionsFrom(s);
					State to = getOutgoingStatefromTransitionSymbol(transition, a);
					if(to != null) temp.add(to);
				}
			}
			
			newStates = new HashSet<>();
			newStates.addAll(temp);
			newStates.removeAll(reachableStates);
			reachableStates.addAll(newStates);
			
		}while(!newStates.equals(Collections.<State>emptySet()));
		
		unreachableStates.addAll((HashSet<State>) states.clone());
		unreachableStates.removeAll(reachableStates);
		
		states.removeAll(unreachableStates);
		
	}
	
	private HashSet<State> getXSet(HashSet<State> A, String symbol){
		HashSet<State> s = new HashSet<State>();
		for(Transition t: delta){
			if(A.contains(t.getTo()) && t.getInput().equals(symbol)){
				s.add(t.getTo());
			}
		}
		return s;
	}
	
	
	private Set<State> setIntersection(HashSet<State> first, HashSet<State> second){
		HashSet<State> intersection = new HashSet<State>();
		for(State s1: first)
			for(State s2: second){
				if(first.contains(s2) && second.contains(s1)){
					intersection.add(s1);
					intersection.add(s2);
				}
			}
		return intersection;
	}
	
	private Set<State> setSubtraction(HashSet<State> first, HashSet<State> second){
		HashSet<State> firstCopy = (HashSet<State>) first.clone();
		
		for(State s: second){
			firstCopy.remove(s);
		}
		return firstCopy;
		
	}
	
	public void hopcroftMinimize(){
		this.hopcroftremoveUnreachableStates();
		HashSet<State> P = (HashSet<State>) states.clone();
		HashSet<State> W = this.getFinalStates();
		HashSet<State> A = new HashSet<>();
		HashSet<State> X;
		HashSet<State> Y = new HashSet<>();
		Random r=new Random();
		
		while(!W.isEmpty()){
			//choose and remove a random set A from W
			for(State s : W){
				int insert=r.nextInt(2);
				if(insert > 0){
					A.add(s);
					W.remove(s);
				}
			}
			
			for(String c : getAlphabet(this)){
				X = getXSet(A,c);
				
				while(!setIntersection(X,Y).isEmpty() && !setSubtraction(Y,X).isEmpty()){
					for(State s : X ){
						int insert=r.nextInt(2);
						if(insert > 0 && !X.equals(Y)){
							Y.add(s);
						}
					}
					
					if(!setIntersection(X,Y).isEmpty() && !setSubtraction(Y,X).isEmpty()){
						HashSet<State> Z = new HashSet<>();
						Z.addAll(setIntersection(X,Y));
						Z.addAll(setSubtraction(Y,X));
						
						for(State s: P){
							if(Y.contains(s)){
								P.remove(s);
								P.addAll(Z);
							}
						}
						
						if(W.contains(Y)){
							for(State s: W){
								if(Y.contains(s)){
									W.remove(s);
									W.addAll(Z);
								}
							}
							
						}else{
							if(setIntersection(X,Y).size() <= setSubtraction(Y,X).size()){
								W.addAll(setIntersection(X,Y));
							}else
								W.addAll(setSubtraction(Y,X));
						}
					}

					
				}
				
			}
		}
		
		
	}
	
	/**
	 * Gets the adjacency list of the automaton.
	 */
	public HashMap<State, HashSet<Transition>> getAdjacencyList() {
		return adjacencyList;
	}

	/**
	 * Sets the adjacency list of the automaton.
	 */
	public void setAdjacencyList(HashMap<State, HashSet<Transition>> adjacencyList) {
		this.adjacencyList = adjacencyList;
	}
	

	/**
	 * Reverse automata operation.
	 */
	public void reverse() {

		HashSet<State> newStates = new HashSet<State>();
		HashSet<Transition> newDelta = new HashSet<Transition>();
		HashMap<State, State> mapping = new HashMap<State, State>();
		
		State newInitialState = new State("init", true, false);
		newStates.add(newInitialState);

		for (State s : this.states) {
			State newState = new State(s.getState(), false , false);

			if (s.isFinalState()) {
				newState.setFinalState(false);
				newState.setInitialState(false);
				newDelta.add(new Transition(newInitialState, newState, "", ""));
			}

			if (s.isInitialState()) 
				newState.setFinalState(true);

			newStates.add(newState);
			mapping.put(s, newState);
		}

		for (Transition t : this.delta) {
			newDelta.add(new Transition(mapping.get(t.getTo()) , mapping.get(t.getFrom()), t.getInput(), ""));
		}
		
		this.delta = newDelta;
		this.initialState = newInitialState;
		this.states = newStates;
		this.adjacencyList = this.computeAdjacencyList();
	}

	/**
	 * Returns the regular expressions associated to this automaton
	 * using the Brzozowski algebraic method.
	 */
	public RegularExpression toRegex() {

		Vector<Equation> equations = new Vector<Equation>();

		for (State s : this.getStates()) {
			RegularExpression result = null;

			HashSet<Transition> out = this.getOutgoingTransitionsFrom(s);

			if (out.size() > 0) {
				for (Transition t : out) {

					if (result == null)
						result = new Comp(new GroundCoeff(t.getInput()), new Var(t.getTo()));
					else
						result = new Or(result, new Comp(new GroundCoeff(t.getInput()), new Var(t.getTo())));
				}

				equations.add(new Equation(s, result));
			} else
				equations.add(new Equation(s, new GroundCoeff("")));
		}

		int indexOfInitialState = 0;

		for (int i = 0; i < equations.size(); ++i) {
			if (equations.get(i).getLeftSide().isInitialState()) {
				indexOfInitialState = i;
				break;
			}

			equations.set(i, new Equation(equations.get(i).getLeftSide(), equations.get(i).getE().simplify()));

			if (!equations.get(i).isIndipendent()) {
				equations.set(i, equations.get(i).syntetize());
				equations.set(i, new Equation(equations.get(i).getLeftSide(), equations.get(i).getE().simplify()));
			}
		}

		// Fix-point
		while (!equations.get(indexOfInitialState).getE().isGround()) {
			for (int i = 0; i < equations.size(); ++i) {
				for (int j = 0; j < equations.size(); ++j) {
					if (i == j)
						continue;

					// Synthetize the indipendent equations
					if (!equations.get(j).isIndipendent()) {
						equations.set(j, equations.get(j).syntetize());
						equations.set(j, new Equation(equations.get(j).getLeftSide(), equations.get(j).getE().simplify()));
					}

					for (int k = 0; k < equations.size(); ++k) 
						equations.set(k, new Equation(equations.get(k).getLeftSide(), equations.get(k).getE().simplify()));

					if (!equations.get(j).getE().isGround()) {
						equations.set(j, new Equation(equations.get(j).getLeftSide(), equations.get(j).getE().replace(equations.get(i).getLeftSide(), equations.get(i).getE())));
						equations.set(j, equations.get(j).syntetize());
					}
				}
			}
		}

		return equations.get(indexOfInitialState).getE().simplify();

	}

	/**
	 * Returns the program corresponding to the RegEx e.
	 * 
	 * @param e regular expression.
	 */
	public static String toProgram(RegularExpression e) {		
		return e.getProgram().replaceAll("\\+", " \\+ ").replaceAll("while", "while ").replaceAll("if", "if ").replaceAll("-", " - ") + "$";
	}

	/**
	 * Checks if there exists a transition between two states.
	 * 
	 * @param s1 first state.
	 * @param s2 second state.
	 * @return the transition from s1 to s2 if exists, null otherwise.
	 */
	public Transition hasTransitionFrom(State s1, State s2) {

		for (Transition t : this.delta)
			if (t.getFrom().equals(s1) && t.getTo().equals(s2))
				return t;
		return null;
	}

	public HashSet<Transition> getTransitionFrom(State s1, State s2) {
		HashSet<Transition> result = new HashSet<Transition>();

		for (Transition t : this.delta)
			if (t.getFrom().equals(s1) && t.getTo().equals(s2))
				result.add(t);

		return result;
	}

	public MultiValueMap<String, State> build(State q) {
		MultiValueMap<String, State> Iq = new MultiValueMap<String, State>();
		build_tr(q, "", new HashSet<Transition>(), Iq);
		return Iq;
	}

	private void build_tr(State q, String stm, HashSet<Transition> mark, MultiValueMap<String, State> Iq) {

		MultiValueMap<String, State> delta_q = new MultiValueMap();

		for (Transition t : this.getOutgoingTransitionsFrom(q)) 
			delta_q.put(t.getInput(), t.getTo());

		while (!delta_q.isEmpty()) {

			String sigma = null; 
			State p = null;

			for (Object s : delta_q.keySet()) {
				sigma = (String) s;
				p = ((ArrayList<State>) delta_q.get((String) s)).get(0);
				break;
			}

			delta_q.removeMapping(sigma, p); //FIX?

			if (!mark.contains(new Transition(q, p, sigma, ""))) {
				mark.add(new Transition(q, p, sigma, ""));

				if (!isPuntaction(sigma) && !p.isFinalState()) {
					HashSet<Transition> markTemp = (HashSet<Transition>) mark.clone();
					markTemp.add(new Transition(q, p, sigma, ""));
					build_tr(p, stm + sigma, markTemp, Iq);
				}

				if (sigma.equals(";") || sigma.equals("{") || sigma.equals("}") || sigma.equals("$")) {
					Iq.put(stm + sigma, p); 
				} 

				if (sigma.equals("'") && p.isFinalState()) {
					Iq.put(stm, p); 
				}
			}
		}
	}

	public Automaton stmSyn() {
		HashSet<State> Q_first = new HashSet<State>();
		State q0;
		Q_first.add(q0 = this.getInitialState());

		HashSet<State> F_first = new HashSet<State>(); // TOFIX

		HashSet<State> temp = new HashSet<State>();
		temp.add(q0);
		HashSet<State> F = ((HashSet<State>) this.getFinalStates());
		F.retainAll(temp);
		F_first = F;

		HashSet<Transition> delta = new HashSet<Transition>();
		HashSet<State> visited = new HashSet<State>();
		visited.add(q0);
		stmSyn_tr(q0, Q_first, F_first, delta, visited);

		Automaton a = new Automaton(q0, delta, Q_first);

		a.minimize();
		return a; 
	}

	private void stmSyn_tr(State q, HashSet<State> q_first, HashSet<State> f_first, HashSet<Transition> delta, HashSet<State> visited) {

		State next = null;
		if (q.isInitialState()) {
			for (Transition t : this.getOutgoingTransitionsFrom(q))
				next = t.getTo();
		} else
			next = q;

		MultiValueMap<String, State> B = build(next);

		visited.add(q);
		HashSet<State> W = new HashSet<State>();

		for (Object s : B.values())
			q_first.add((State) s);

		f_first.retainAll((HashSet<State>) this.getFinalStates()); 

		for (Object sigma : B.keySet())
			for (Object to : (ArrayList<State>) B.get((String) sigma))
				delta.add(new Transition(q, (State) to, (String) sigma, ""));

		for (Object s : B.values())  
			W.add((State) s);

		W.removeAll(visited);

		for (State p : W) 
			stmSyn_tr(p, q_first, f_first, delta, visited);
	}

	public HashSet<State> dfs() {
		HashMap<State, String> color = new HashMap<State, String>();
		HashMap<State, State> pi = new HashMap<State, State>();

		HashSet<State> result = new HashSet<State>();

		for (State s : this.getStates())
			color.put(s, "w");

		for (State s : this.getStates())
			result.addAll(dfs_visit(s, color, pi));

		return result;
	}

	public HashSet<State> dfs_visit(State s, HashMap<State, String> color, HashMap<State, State> pi) {
		HashSet<State> cycles = new HashSet<State>();

		color.put(s, "g");

		for (Transition t : this.getOutgoingTransitionsFrom(s)) {
			State v = t.getTo();

			if (color.get(v).equals("w")) {
				pi.put(v, s);
				cycles.addAll(dfs_visit(v, color, pi));
			} else if (color.get(v).equals("g")) {
				cycles.add(v);
			}
		}

		color.put(s, "b");
		return cycles;
	}


	public HashMap<State, String> bfs() {
		return bfsAux(this.getInitialState());
	}

	public HashMap<State, String> bfsAux(State root) {
		HashMap<State, String> distance = new HashMap<State, String>();
		HashMap<State, State> parent = new HashMap<State, State>();

		for (State s : this.getStates()) { 
			distance.put(s, null); 
			parent.put(s, null);
		}

		Queue<State> q = new LinkedList<State>();
		distance.put(root, "");
		q.add(root);

		while (!q.isEmpty()) {
			System.out.println(q);
			State current = q.remove();

			for (Transition t : this.getOutgoingTransitionsFrom(current)) {
				if (distance.get(t.getTo()) == null) {
					distance.put(t.getTo(), distance.get(current) +  t.getInput());
					parent.put(t.getTo(), current);
					q.add(t.getTo());
				}
			}	
		}

		return distance;
	}

	/**
	 * Returns the sets of final states.
	 * 
	 * @return an HashSet of final states.
	 */
	public HashSet<State> getFinalStates() {
		HashSet<State> result = new HashSet<State>();

		for (State s : this.states) 
			if (s.isFinalState())
				result.add(s);

		return result;
	}

	private boolean isPuntaction(String s) {
		return s.equals("$") || s.equals(";") || s.equals("}") || s.equals("{");
	}

	public HashSet<String> getNumbers() {
		HashMap<State, String> bfs = new HashMap<State, String>();

		for (Transition t : this.getOutgoingTransitionsFrom(this.getInitialState()))
			bfs.putAll(this.bfsAux(t.getTo()));

		System.out.println(bfs);

		HashSet<String> result = new HashSet<String>();

		for (State s : bfs.keySet()) {
			if (s.isFinalState())
				if (StringUtils.isNumeric(bfs.get(s).substring(0, bfs.get(s).length() - 1)))
					result.add(bfs.get(s).substring(0, bfs.get(s).length() - 1));
		}

		return result;
	}


	public HashSet<String> getMaximalPrefixNumber(State s, Vector<State> visited) {
		HashSet<String> result = new HashSet<String>();


		if (visited.contains(s)) 
			return result;

		visited.add(s);

		for (Transition t : this.getOutgoingTransitionsFrom(s)) {

			if (StringUtils.isNumeric(t.getInput()) || ((t.getInput().equals("-") || t.getInput().equals("+")) && visited.size() == 1)) { 
				HashSet<String> nexts = getMaximalPrefixNumber(t.getTo(), visited);

				if (nexts.isEmpty()) 
					result.add(t.getInput());
				else				
					for (String next : nexts) 
						result.add(t.getInput() + next);
			}
		}

		return result;
	}

	/**
	 * Performs the set of strings of size at most n recognized from the state s.
	 * 
	 * @param s state
	 * @param n string size
	 * @return the language of size at most n recognized from the state s.
	 */
	public HashSet<String> getStringsAtMost(State s, int n) {
		HashSet<String> result = new HashSet<String>();

		if (n == 0)
			return result;

		for (Transition t : this.getOutgoingTransitionsFrom(s)) {
			String partial = t.getInput();

			if (getStringsAtMost(t.getTo(), n - 1).isEmpty())
				result.add(partial);
			else
				for (String next : getStringsAtMost(t.getTo(), n - 1))
					result.add(partial + next);
		}

		return result;
	}


	public Automaton widening(int n) {
		HashMap<State, HashSet<String> > languages = new HashMap<State, HashSet<String>>();
		State newInitialState = null;
		HashSet<HashSet<State>> powerStates = new HashSet<HashSet<State>>();

		for (State s : this.getStates()) 
			languages.put(s, this.getStringsAtMost(s, n));

		for (State s1 : this.getStates()) 
			for (State s2 : this.getStates())
				if (languages.get(s1).equals(languages.get(s2))) {
					boolean found = false;
					for (HashSet<State> singlePowerState : powerStates) 
						if (singlePowerState.contains(s1) || singlePowerState.contains(s2)) {
							singlePowerState.add(s1);
							singlePowerState.add(s2);
							found = true;
							break;
						}
					if (!found) {
						HashSet<State> newPowerState = new HashSet<State>();
						newPowerState.add(s1);
						newPowerState.add(s2);
						powerStates.add(newPowerState);
					}
				}

		HashSet<State> newStates = new HashSet<State>();
		HashMap<HashSet<State>, State> mapping = new HashMap<HashSet<State>, State>();

		for (HashSet<State> ps : powerStates) {
			State ns = new State(createName(ps), isPartitionInitialState(ps), isPartitionFinalState(ps));
			newStates.add(ns);

			if (ns.isInitialState())
				newInitialState = ns;

			mapping.put(ps, ns);
		}

		HashSet<Transition> newDelta = new HashSet<Transition>();

		for (Transition t : this.getDelta()) {
			HashSet<State> fromPartition = null;
			HashSet<State> toPartition = null;

			for (HashSet<State> ps : powerStates) {
				if (ps.contains(t.getFrom()))
					fromPartition = ps;
				if (ps.contains(t.getTo()))
					toPartition = ps;			
			}

			newDelta.add(new Transition(mapping.get(fromPartition), mapping.get(toPartition), t.getInput(), ""));

		}
		return new Automaton(newInitialState, newDelta, newStates);
	}

	/**
	 * Returns the initial state.
	 */
	@Deprecated
	public State getInitialState() {
		return initialState;
	}

	public HashSet<State> getInitialStates(){
		HashSet<State> initialStates=new HashSet<State>();

		for(State s: this.states){
			if(s.isInitialState()){
				initialStates.add(s);
			}
		}
		return initialStates;

	}

	/**
	 * Sets the initial state.
	 */
	public void setInitialState(State initialState) {
		this.initialState = initialState;
	}

	/**
	 * Gets the states of the automaton.
	 */
	public HashSet<State> getStates() {
		return states;
	}

	/**
	 * Sets the states of the automaton.
	 */
	public void setStates(HashSet<State> states) {
		this.states = states;
	}

	/**
	 * Returns a string representing the automaton.
	 */
	public String automatonPrint() {
		String result = "";

		for (State st: this.getStates()) {
			if (!this.getOutgoingTransitionsFrom(st).isEmpty() || st.isFinalState() || st.isInitialState()) {
				result += "[" +  st.getState() + "] " + (st.isFinalState() ? "[accept]" + (st.isInitialState() ? "[initial]\n" : "\n") : "[reject]" + (st.isInitialState() ? "[initial]\n" : "\n"));
				for (Transition t : this.getOutgoingTransitionsFrom(st))
					result += "\t" + st + " " + t.getInput() + " -> " + t.getTo() + "\n";  
			}
		}

		return result;
	}

	@Override
	public String toString() {
		return this.automatonPrint();
	}

	@Override
	public Automaton clone() {
		HashSet<State> newStates = new HashSet<State>();
		HashSet<Transition> newDelta = new HashSet<Transition>();
		State newInitialState = null;
		HashMap<String, State> nameToStates = new HashMap<String, State>();

		for (State s: this.states) {
			State newState = s.clone();
			newStates.add(newState);
			nameToStates.put(newState.getState(), newState);

			if (newState.isInitialState())
				newInitialState = newState;
		}

		for (Transition t : this.delta)
			newDelta.add(new Transition(nameToStates.get(t.getFrom().getState()), nameToStates.get(t.getTo().getState()), t.getInput(), t.getOutput()));

		return new Automaton(newInitialState, newDelta, newStates);

	}

	public boolean approxEquals(Object other) {
		if (other instanceof Automaton) 
			return (this.getDelta().size() == ((Automaton) other).getDelta().size() && this.getStates().size() == ((Automaton) other).getStates().size());

		return false;
	}

	/**
	 * Equal operator between automata.
	 */
	@Override
	public boolean equals(Object other) {
		if (other instanceof Automaton) {
			
			if (this.getStates().size() != ((Automaton) other).getStates().size() || this.getDelta().size() != ((Automaton) other).getDelta().size())
				return false;
				
			Automaton first = Automaton.intersection(this, Automaton.complement((Automaton) other));
			first.deMerge(++initChar);
			Automaton second = Automaton.intersection(Automaton.complement(this), (Automaton) other);
			second.deMerge(++initChar);
			
			first.minimize();
			second.minimize();

			Automaton c = Automaton.union(first, second);
			c.minimize();
			return c.getFinalStates().isEmpty();
		}

		return false;
	}


	public int maxLengthString() {
		int max = Integer.MIN_VALUE;

		for (Vector<State> v : this.pahtsFrom(this.getInitialState(), new Vector<State>()))
			if (v.size() > max)
				max = v.size();

		return max - 2; // removes ''
	}

	public HashSet<Vector<State>> pahtsFrom(State init, Vector<State> visited) {
		HashSet<Vector<State>> result = new HashSet<Vector<State>>();

		if (init.isFinalState() || visited.contains(init)) {
			Vector<State> v = new Vector<State>();
			result.add(v);
			return result;			
		}

		visited.add(init);

		for (Transition t : this.getOutgoingTransitionsFrom(init)) {
			Vector<State> partial = new Vector<State>();
			partial.add(init);

			for (Vector<State> v : this.pahtsFrom(t.getTo(), visited)) {
				Vector<State> p = (Vector<State>) partial.clone();
				p.addAll(v);
				result.add(p);

			}
		}

		return result;
	}


	/**
	 * Checks if this automaton contains a cycle.
	 * 
	 * @return true if this automaton contains a cycle, false otherwise.
	 */
	public boolean hasCycle() {
		return hasCycleAux(this.getInitialState(), new Vector<State>());
	}

	/**
	 * Checks if this automaton contains a cycle starting from init state.
	 * 
	 * @param init initial state.
	 * @param visited states already visited.
	 * @return true if this automaton contains a cycle starting from init state, false otherwise.
	 */
	public boolean hasCycleAux(State init, Vector<State> visited) {

		HashSet<Transition> outgoing = this.getOutgoingTransitionsFrom(init);

		for (Transition t: outgoing) {
			if (visited.contains(t.getTo()))
				return true;
			else {
				visited.add(t.getTo());
				if (hasCycleAux(t.getTo(), visited))
					return true;
			}
		}

		return false;
	}

}
