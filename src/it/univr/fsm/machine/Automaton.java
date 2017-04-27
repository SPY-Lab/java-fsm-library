package it.univr.fsm.machine;

import it.univr.fsm.equations.*;
import org.apache.commons.collections4.map.MultiValueMap;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;

import it.univr.exception.*;

import it.univr.fsm.equations.Comp;
import it.univr.fsm.equations.Equation;
import it.univr.fsm.equations.GroundCoeff;
import it.univr.fsm.equations.Or;
import it.univr.fsm.equations.RegularExpression;
import it.univr.fsm.equations.Var;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.crypto.Mac;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.File;
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
	 * Adjacency list Outgoing
	 */
	private HashMap<State, HashSet<Transition>> adjacencyListOutgoing;

	/**
	 * Adjacency list Incoming
	 */
	private HashMap<State, HashSet<Transition>> adjacencyListIncoming;

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
		this.computeAdjacencyList();
	}

	private void computeAdjacencyList() {	
		HashMap<State, HashSet<Transition>> resultOutgoing = new HashMap<State, HashSet<Transition>>();
		HashMap<State, HashSet<Transition>> resultIncoming = new HashMap<State, HashSet<Transition>>();

		for (State s : this.getStates()) {
			resultIncoming.put(s, new HashSet<Transition>());
			resultOutgoing.put(s, new HashSet<Transition>());

			for (Transition t : this.getDelta()) {
				if (t.getFrom().equals(s)) {
					HashSet<Transition> temp = resultOutgoing.get(s);
					temp.add(t);
					resultOutgoing.put(s, temp);
				} 

				if (t.getTo().equals(s)) {
					HashSet<Transition> temp = resultIncoming.get(s);
					temp.add(t);
					resultIncoming.put(s, temp);
				}
			}
		}

		this.adjacencyListIncoming = resultIncoming;
		this.adjacencyListOutgoing = resultOutgoing;
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
				if (t.getInput().isEmpty()) return false;

				for(Transition t2: outgoingTranisitions) {
					if (t2.getInput().isEmpty()) return false;
					if(!t.getTo().equals(t2.getTo()) && t.getInput().equals(t2.getInput())) return false; 
				}
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
		return automaton.getFinalStates().isEmpty() && !automaton.states.isEmpty();
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

		State firstInitialState = null;

		HashSet<State> firstFinalStates = new HashSet<>();
		HashSet<State> secondInitialStates = new HashSet<>();

		int c = 0;



		// Add all the first automaton states
		for (State s: first.states) {

			// The first automaton states are not final, can be initial states
			mappingFirst.put(s, new State("q" + c++, s.isInitialState(), false));
			newStates.add(mappingFirst.get(s));

			if(s.isInitialState()){
				firstInitialState = mappingFirst.get(s);
			}

			if(s.isFinalState()){
				firstFinalStates.add(s);
			}


		}

		// Add all the second automaton states
		for (State s: second.states) {

			// the second automaton states are final, can't be initial states
			mappingSecond.put(s, new State("q" + c++, false, s.isFinalState()));
			newStates.add(mappingSecond.get(s));

			if(s.isInitialState()){
				secondInitialStates.add(s);
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
			for(State s : secondInitialStates)
				newDelta.add(new Transition(mappingFirst.get(f), mappingSecond.get(s), "", ""));

		Automaton a = new Automaton(firstInitialState, newDelta, newStates);
		a.minimize();

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

		State autNewInitialState = null;


		// Add states to the mapping, replacing accept states to reject
		for(State s: automaton.states) {
			mapping.put(s, new State(s.getState(), s.isInitialState(), !s.isFinalState()));
			newStates.add(mapping.get(s));

			if(s.isInitialState())
				autNewInitialState = mapping.get(s);
		}

		// Copying delta set
		for (Transition t:  automaton.delta)
			newDelta.add(new Transition(mapping.get(t.getFrom()), mapping.get(t.getTo()), t.getInput(), ""));

		Automaton a = new Automaton(autNewInitialState,newDelta,newStates);
		a.minimize();
		return a;
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
		Automaton notSecond = Automaton.complement(second);

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
	 *[state_name][reject] or [initial]
	 * <tab>[state_from] Sym -> [state_to]</tab>
	 *
	 *
	 *
	 *
	 */
	public static Automaton loadAutomataWithAlternatePattern(String path){


		BufferedReader br = null;


		HashMap<String, State> mapStates = new HashMap<>();
		HashSet<Transition> delta = new HashSet<Transition>();
		HashSet<State> states = new HashSet<State>();
		State initialstate = null;



		try{
			String currentLine;
			br = new BufferedReader(new FileReader(path) );


			while((currentLine = br.readLine()) != null ){
				State current = null;
				State next = null;
				String sym = "";
				String stateName = "";

				// state
				if(currentLine.charAt(0) != '\t'){
					String[] pieces = currentLine.trim().split(" ");

					// sanity check
					if(pieces.length < 2) throw new MalformedInputException();

					for(int i = 0; i < pieces.length; i++){

						if(pieces[i].startsWith("[") && pieces[i].endsWith("]") && i == 0){
							stateName = pieces[i].substring(1, pieces[i].length()-1);
							// found state
							if(mapStates.containsKey(stateName)){
								current = mapStates.get(stateName);
							}else{
								mapStates.put(stateName, (current=new State(stateName,false,false)));
							}

						}else if(pieces[i].startsWith("[") && pieces[i].endsWith("]")
								&& pieces[i].contains("accept")){
							if(mapStates.containsKey(stateName)){
								current = mapStates.get(stateName);
								current.setFinalState(true);
							}else{
								throw new MalformedInputException();
							}

						}else if(pieces[i].startsWith("[") && pieces[i].endsWith("]")
								&& pieces[i].contains("initial")){
							if(mapStates.containsKey(stateName)){
								current = mapStates.get(stateName);
								initialstate = current;
								current.setInitialState(true);
							}else{
								throw new MalformedInputException();
							}
						}
					}

					if(current == null) throw new MalformedInputException();

					if(!states.contains(current))
						states.add(current);
					else {
						states.remove(current);
						states.add(current);
					}


				}
				// transition
				else{
					String line = currentLine.substring(1, currentLine.length()).trim();
					String[] pieces = line.split(" ");

					// sanity check
					if (pieces.length > 4 || pieces.length < 3) throw new MalformedInputException();


					for(int i = 0; i < pieces.length; i++){
						if(pieces[i].startsWith("[") && pieces[i].endsWith("]") && i == 0){
							// state from
							stateName = pieces[i].substring(1, pieces[i].length()-1);

							if(mapStates.containsKey(stateName)){
								current = mapStates.get(stateName);
							}else{
								throw new MalformedInputException();
							}

						}else if(pieces[i].startsWith("[") && pieces[i].endsWith("]")){
							// next state
							stateName = pieces[i].substring(1, pieces[i].length()-1);
							if(mapStates.containsKey(stateName)){
								next = mapStates.get(stateName);
								states.add(next);
							}else{
								mapStates.put(stateName, (next = new State(stateName,false,false)));
							}

						}else if(!pieces[i].equals("->")){
							// transition symbol
							sym = pieces[i];

						}
					}

					delta.add(new Transition(current,next,sym,""));
					
				}



			}


		}catch(IOException | MalformedInputException e) {
			e.printStackTrace();
			return null;
		}finally{
			try{
				br.close();
			}catch(Exception c){
				System.err.println("Failed to close BufferedReader stream in loadAutomataWithAlternatePattern: " + c.getMessage() );
			}
		}

		Automaton a= new Automaton(initialstate,delta,states);

		return a;
	}

	public static Automaton loadAutomataWithFSM2RegexPattern(String path){
		BufferedReader br = null;


		HashMap<String, State> mapStates = new HashMap<>();
		HashSet<Transition> delta = new HashSet<Transition>();
		HashSet<State> states = new HashSet<State>();
		State initialstate = null;



		try{
			String currentLine;
			br = new BufferedReader(new FileReader(path) );

			/**
			 * 0 : #states
			 * 1 : #initial
			 * 2 : #accepting
			 * 3 : #alphabet
			 * 4 : #transition
			 *
			 */
			int currentMode = -1;


			while((currentLine = br.readLine()) != null ){
				State current = null;
				State next = null;
				String sym = "";
				String stateName = "";

				switch(currentLine){
					case "#states":
						currentMode = 0;
						continue;
					case "#initial":
						currentMode = 1;
						continue;
					case "#accepting":
						currentMode = 2;
						continue;
					case "#alphabet":
						currentMode = 3;
						continue;
					case "#transitions":
						currentMode = 4;
						continue;
				}

				switch (currentMode){
					case 0:
						states.add((current = new State(currentLine,false,false)));
						mapStates.put(currentLine,current);
						break;
					case 1:
						if(mapStates.containsKey(currentLine)) {
							current = mapStates.get(currentLine);
							current.setInitialState(true);
							initialstate = current;
						}else
							throw new MalformedInputException();
						break;
					case 2:
						if(mapStates.containsKey(currentLine)) {
							current = mapStates.get(currentLine);
							current.setFinalState(true);
						}else
							throw new MalformedInputException();
						break;
					case 3:
						break;
					case 4:
						current = mapStates.get(currentLine.split(">")[0].split(":")[0]);
						sym = (currentLine.split(">")[0]).split(":")[1];
						if(sym.equals("$")) sym = "";
						String[] to = currentLine.split(">")[1].split(",");

						if(current == null || to.length == 0) throw new MalformedInputException();

						for(String toS : to){
							if(mapStates.containsKey(toS)){
								delta.add(new Transition(current,mapStates.get(toS),sym,""));
							}else
								throw new MalformedInputException();
						}



						break;
					default:
						throw new MalformedInputException();
				}



			}


		}catch(IOException | MalformedInputException e) {
			e.printStackTrace();
			return null;
		}finally{
			try{
				br.close();
			}catch(Exception c){
				System.err.println("Failed to close BufferedReader stream in loadAutomataWithAlternatePattern: " + c.getMessage() );
			}
		}

		Automaton a= new Automaton(initialstate,delta,states);

		return a;
	}

	public static Automaton loadAutomataWithJFLAPPattern(String path){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		HashSet<State> newStates = new HashSet<>();
		State initialState = null;
		State temp;
		HashSet<Transition> newDelta = new HashSet<>();

		HashMap<String, State> statesMap = new HashMap<>();

		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(new File(path));
			document.getDocumentElement().normalize();

			NodeList nListStates = document.getElementsByTagName("state");
			NodeList nListTransitions = document.getElementsByTagName("transition");

			// analyze all the states
			for(int i = 0 ; i< nListStates.getLength(); i++){
				Node actual = nListStates.item(i);

				String stateName = actual.getAttributes().getNamedItem("name").getNodeValue();
				boolean isInitialState = false;
				boolean isFinalState = false;

				Node property = actual.getFirstChild();

				// analyze state properties
				while (property != null){
					switch (property.getNodeName()){
						case "initial":
							isInitialState = true;
							break;
						case "final":
							isFinalState = true;
							break;
					}

					property = property.getNextSibling();
				}

				State s = new State(stateName,isInitialState,isFinalState);
				if(s.isInitialState()) initialState = s;

				statesMap.put(actual.getAttributes().getNamedItem("id").getNodeValue(),s);
				newStates.add(statesMap.get(actual.getAttributes().getNamedItem("id").getNodeValue()));
			}

			// analyze all the transitions
			for(int i = 0 ; i< nListTransitions.getLength(); i++){
				Node actual = nListTransitions.item(i);
				State from = null;
				State to = null;
				String sym = "";

				Node property = actual.getFirstChild();

				// analyze transitions properties
				while (property != null){
					switch (property.getNodeName()){
						case "from":
							if(!statesMap.containsKey(property.getFirstChild().getNodeValue())) throw new MalformedInputException();
							from = statesMap.get(property.getFirstChild().getNodeValue());
							break;
						case "to":
							if(!statesMap.containsKey(property.getFirstChild().getNodeValue())) throw new MalformedInputException();
							to = statesMap.get(property.getFirstChild().getNodeValue());
							break;
						case "read":
							if(property.getFirstChild() != null)
								sym = property.getFirstChild().getNodeValue();
							break;
					}

					property = property.getNextSibling();
				}

				newDelta.add(new Transition(from,to,sym,""));


			}

			return new Automaton(initialState,newDelta,newStates);

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			return null;
		} catch (SAXException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}


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
		State initialstate = null;

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
						initialstate = currentState;
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


		}catch(IOException | MalformedInputException e) {
			e.printStackTrace();
			return null;
		}finally{
			try{
				br.close();
			}catch(Exception c){
				System.err.println("Failed to close BufferedReader stream in loadAutomata: " + c.getMessage() );
			}
		}

		Automaton a= new Automaton(initialstate,delta,states);

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

	public boolean run(String s, State state){
		ArrayList<String> input = (ArrayList<String>) toList(s);

		return _run(input, state);

	}

	private boolean _run(ArrayList<String> s, State state){
		boolean found = false;

		if(state.isFinalState() && s.isEmpty())
			return true;
		else if(!state.isFinalState() && s.isEmpty()) //string is empty and I'm not in a final state
			return false;
		else{
			ArrayList<String> scopy = new ArrayList<>(s);
			String ch = scopy.get(0);

			for(Transition t : getOutgoingTransitionsFrom(state)){
				if(t.isFirable(state, ch) && !t.getInput().equals("")) {
					scopy.remove(0);
					found = found || _run(scopy, t.fire(ch));
				}else if(t.getInput().equals("")){
					found = found || _run(scopy, t.fire(ch));
				}

			}

		}


		return found;
	}

	/**
	 * Runs a string on the automaton starting from a given state.
	 * 
	 * @param s the string
	 * @param state the starting state
	 * @return true if the string is accepted by the automaton, false otherwise
	 */
	/*
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
	} */

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
			delta.add(new Transition(state, next, input.get(i), ""));

			state = next;
		}

		return new Automaton(initialState, delta, states);
	}

	public HashSet<Transition> getOutgoingTransitionsFrom(State s) {
		return this.adjacencyListOutgoing.get(s);
	}
	
	public HashSet<Transition> getIncomingTransitionsFrom(State s) {
		return this.adjacencyListIncoming.get(s);
	}



	public void setAdjacencyListOutgoing(HashMap<State, HashSet<Transition>> adjacencyListOutgoing) {
		this.adjacencyListOutgoing = adjacencyListOutgoing;
	}

	public void setAdjacencyListIncoming(HashMap<State, HashSet<Transition>> adjacencyListIncoming) {
		this.adjacencyListIncoming = adjacencyListIncoming;
	}
	
	/**
	 * Union operation between two automata.
	 * 
	 * @param a1 first automaton.
	 * @param a2 second automaton.
	 * @return the union of the two automata.
	 */
	public static Automaton union(Automaton a1, Automaton a2) {
		State newInitialState = new State("q0", true, false);
		HashSet<Transition> newGamma = new HashSet<Transition>();
		HashSet<State> newStates = new HashSet<State>();

		int c = 1;
		HashMap<State, State> mappingA1 = new HashMap<State, State>(); 
		HashMap<State, State> mappingA2 = new HashMap<State, State>(); 

		newStates.add(newInitialState);

		State initialA1 = null; 
		State initialA2 = null;

		for (State s : a1.states) {

			mappingA1.put(s, new State("q" + c++, false, s.isFinalState()));

			newStates.add(mappingA1.get(s));



			if (s.isInitialState())
				initialA1 = mappingA1.get(s);
		}

		for (State s : a2.states) {
			mappingA2.put(s, new State("q" + c++, false, s.isFinalState()));
			newStates.add(mappingA2.get(s));



			if (s.isInitialState())
				initialA2 = mappingA2.get(s);
		}

		for (Transition t : a1.delta)
			newGamma.add(new Transition(mappingA1.get(t.getFrom()), mappingA1.get(t.getTo()), t.getInput(), ""));

		for (Transition t : a2.delta)
			newGamma.add(new Transition(mappingA2.get(t.getFrom()), mappingA2.get(t.getTo()), t.getInput(), ""));

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
			// partial.add(s);

			for (State reached : paths) 
				for (Transition t : this.getOutgoingTransitionsFrom(reached)) 
					if (t.isEpsilonTransition())
						partial.add(t.getTo());

			paths.addAll(partial);
		}

		return paths;
	}

	/**
	 * Epsilon closure of a Set of states
	 * @param set the set
	 * @return an HashSet of states reachable from the states by using only epsilon transition.
	 *
	 */
	public HashSet<State> epsilonClosure(HashSet<State> set){
		HashSet<State> solution = new HashSet<>();

		for(State s : set){
			solution.addAll(epsilonClosure(s));
		}

		return solution;
	}

	private HashSet<State> moveNFA(HashSet<State> set, String sym){
		HashSet<State> solution = new HashSet<>();

		for(State s : set){
			HashSet<Transition> outgoing = getOutgoingTransitionsFrom(s);
			for(Transition t : outgoing){
				if(t.getInput().equals(sym)){
					solution.add(t.getTo());
				}
			}

		}

		return solution;
	}



	/**
	 * Determinization automata operation.
	 *  
	 * @return a new determinized automaton. 
	 */

//	public Automaton determinize() {
//
//		HashMap<HashSet<State>, Boolean> dStates = new HashMap<HashSet<State>, Boolean>();
//		HashSet<Transition> dGamma = new HashSet<Transition>();
//		HashSet<State> newStates = new HashSet<State>();
//
//		dStates.put(epsilonClosure(this.getInitialState()), false);
//		HashSet<State> T;
//
//		State newInitialState = new State(createName(epsilonClosure(this.getInitialState())), true, isPartitionFinalState(epsilonClosure(this.getInitialState())));
//
//
//
//		newStates.add(newInitialState);
//
//		while ((T = notMarked(dStates)) != null) {
//			dStates.put(T, true);
//
//
//			for (String alphabet: readableCharFromState(T)) {
//
//				HashSet<State> newStateWithEpsilonTransition = new HashSet<State>();
//
//				// reachable states after epsilon transition
//				for (State s : T)
//					for (Transition t : this.getOutgoingTransitionsFrom(s))
//						if (t.getInput().equals(String.valueOf(alphabet)))
//							newStateWithEpsilonTransition.add(t.getTo());
//
//				HashSet<HashSet<State>> newStateWithNoEpsilon = new HashSet<HashSet<State>>();
//
//				for (State s : newStateWithEpsilonTransition)
//					newStateWithNoEpsilon.add(this.epsilonClosure(s));
//
//
//				HashSet<State> U = new HashSet<State>();
//
//				for (HashSet<State> ps : newStateWithNoEpsilon) // Flatting hashsets
//					for (State s : ps)
//						U.add(s);
//
//				// TODO:I think that lacks a control whether a set is contained in another one
//	/*			HashMap<HashSet<State>, Boolean> tempDStates = new HashMap<HashSet<State>, Boolean>();
//
//				for (HashSet<State> tst : dStates.keySet()) {
//					HashSet<State> newTst = (HashSet<State>) tst.clone();
//					newTst.remove(new State("init", true, false));
//					tempDStates.put(newTst, dStates.get(tst));
//				}
//	*/
//				if (!dStates.containsKey(U) )
//					dStates.put(U, false);
//				else {
//					for (HashSet<State> s : dStates.keySet())
//						if (s.equals(U))
//							U = s;
//				}
//
//
//				State from = new State(createName(T), false, isPartitionFinalState(T));
//				State to = new State(createName(U), false, isPartitionFinalState(U));
//
//				newStates.add(from);
//				newStates.add(to);
//
//				dGamma.add(new Transition(from, to, String.valueOf(alphabet), ""));
//
//			}
//
//		}
//
//		Automaton a = (new Automaton(newInitialState, dGamma, newStates)).deMerge(++initChar);
//		return a;
//	}
	public Automaton determinize(){
		HashSet<State> newStates = new HashSet<>();
		HashSet<Transition> newDelta = new HashSet<>();

		HashMap<HashSet<State>, Boolean> statesMarked = new HashMap<>();
		HashMap<HashSet<State>,State> statesName = new HashMap<>();
		int num = 0;
		LinkedList<HashSet<State>> unMarkedStates = new LinkedList<>();

		HashSet<State> temp;

		temp = epsilonClosure(this.getInitialState());
		statesName.put(temp,new State("q" + String.valueOf(num++) ,true,isPartitionFinalState(temp)));

		State initialState = statesName.get(temp);
		newStates.add(statesName.get(temp));
		statesMarked.put(temp, false);
		unMarkedStates.add(temp);



		while(!unMarkedStates.isEmpty()){
			HashSet<State> T = unMarkedStates.getFirst();
			newStates.add(statesName.get(T));

			// mark T
			unMarkedStates.removeFirst();
			statesMarked.put(T, true);

			for (String alphabet: readableCharFromState(T)) {
				temp = epsilonClosure(moveNFA(T, alphabet));

				if(!statesName.containsKey(temp))
					statesName.put(temp,new State("q" + String.valueOf(num++) ,false,isPartitionFinalState(temp)));

				newStates.add(statesName.get(temp));

				if (!statesMarked.containsKey(temp)) {
					statesMarked.put(temp, false);
					unMarkedStates.addLast(temp);
				}
				newDelta.add(new Transition(statesName.get(T),statesName.get(temp), alphabet, ""));

			}


		}


		Automaton a = new Automaton(initialState, newDelta, newStates);
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

		for (Transition t : this.getOutgoingTransitionsFrom(s)) {
			if (!t.getInput().equals(""))
				result.add(t.getInput());
		}

		return result;

	}

	/**
	 * Removes the unreachable states of an automaton.
	 * 
	 *
	 */
	public void removeUnreachableStates() {
		HashSet<State> reachableStates = new HashSet<State>();
		reachableStates.add(this.getInitialState());

		HashSet<State> newStates = new HashSet<State>();
		newStates.add(this.getInitialState());		

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
		HashSet<Transition> delta = (HashSet<Transition>) this.delta.clone();

		states.removeAll(statesToRemove);
		delta.removeAll(transitionsToRemove);


		this.states = states;
		this.delta = delta;
		this.computeAdjacencyList();
	}

	/**
	 * Brzozowski's minimization algorithm.
	 */
	public void minimize() {

		if (!isDeterministic(this)) {
			Automaton a = this.determinize();
			this.initialState = a.initialState;
			this.delta = a.delta;
			this.states = a.states;
			this.adjacencyListOutgoing = a.getAdjacencyListOutgoing();
			this.adjacencyListIncoming = a.getAdjacencyListIncoming();
		}


		this.reverse();
		Automaton a = this.determinize();
		a.reverse();
		a = a.determinize();

		this.initialState = a.initialState;
		this.delta = a.delta;
		this.states = a.states;
		this.adjacencyListIncoming = a.getAdjacencyListIncoming();
		this.adjacencyListOutgoing = a.getAdjacencyListOutgoing();
		

		/*
		this.minimizeHopcroft();

		Automaton a = this.deMerge(++initChar); 
		this.initialState = a.initialState;
		this.states = a.states;
		this.delta = a.delta;
		this.adjacencyListIncoming = a.getAdjacencyListIncoming();
		this.adjacencyListOutgoing = a.getAdjacencyListOutgoing();
		*/

	}

	public static HashSet<String> getAlphabet(Automaton a){
		HashSet<String> alphabet = new HashSet<String>();

		for(Transition t : a.delta){
			if(!alphabet.contains(t.getInput()))
				alphabet.add(t.getInput());
		}

		return alphabet;
	}

	private State getOutgoingStatefromTransitionSymbol(State s, String symbol){
		for(Transition t : delta){
			if(t.getInput().equals(symbol) && t.getFrom().equals(s)){
				return t.getTo();
			}
		}
		return null;
	}

	/*public void hopcroftremoveUnreachableStates(){
		HashSet<State> unreachableStates = new HashSet<>();
			HashSet<State> reachableStates = (HashSet<State>) this.getInitialStates().clone();
		HashSet<State> newStates = (HashSet<State>) this.getInitialStates().clone();
		HashSet<State> reachableStates = this.getInitialStates();
		HashSet<State> newStates = this.getInitialStates();
		HashSet<Transition> transitionstoRemove = new HashSet<>(); 
		HashSet<State> temp;
		do{
			temp = new HashSet<>(Collections.<State>emptySet());
			for(State s : newStates){
				for(String a : getAlphabet(this)){
					State to = getOutgoingStatefromTransitionSymbol(s, a);
					if(to != null) temp.add(to);
				}
			}
			newStates = new HashSet<>();
			newStates.addAll(temp);
			newStates.removeAll(reachableStates);
			reachableStates.addAll(newStates);
		}while(!newStates.equals(Collections.<State>emptySet()));
		// Opt
		unreachableStates.addAll(states);
		unreachableStates.removeAll(reachableStates);
		states.removeAll(unreachableStates);
		for(Transition t: delta)
			if(!states.contains(t.getFrom()))
				transitionstoRemove.add(t);
		delta.removeAll(transitionstoRemove);
		// Opt
		//this.adjacencyList = this.computeAdjacencyList();
	}*/

	private HashSet<Transition> getIncomingTransitionsTo(State state){
		return this.adjacencyListIncoming.get(state);
	}




	private HashSet<State> getXSet(HashSet<State> A, String c){
		HashSet<State> xSet = new HashSet<State>();

		for(State s : A){
			HashSet<Transition> transitionsIncoming = getIncomingTransitionsTo(s);

			for(Transition t : transitionsIncoming){
				if(t.getInput().equals(c))
					xSet.add(t.getFrom());
			}

		}


		return xSet;
	}



	private LinkedList<HashSet<State>> getYList(HashSet<HashSet<State>> P, HashSet<State> X){
		LinkedList<HashSet<State>> Ys = new LinkedList<>();
		HashSet<State> Ytemp ;

		for(HashSet<State> s : P){

			//try to select a set, see if condition is respected
			Ytemp = s;

			if(!setIntersection(X,Ytemp).isEmpty() && !setSubtraction(Ytemp,X).isEmpty()){
				Ys.add(Ytemp);
			}

		}

		return Ys;

	}

	private HashSet<State> setIntersection(HashSet<State> first, HashSet<State> second){
		HashSet<State> intersection = (HashSet<State>) first.clone();
		intersection.retainAll(second);
		return intersection;
	}

	private HashSet<State> setSubtraction(HashSet<State> first, HashSet<State> second){
		HashSet<State> firstCopy = (HashSet<State>) first.clone();

		/*for(State s: second){
			firstCopy.remove(s);
		}*/
		
		firstCopy.removeAll(second);
		return firstCopy;
		
	}


	private HashSet<State> getSet(State s1, HashSet<HashSet<State>> P){
		for(HashSet<State> S : P){
			if(S.contains(s1) ) return S;
		}

		return null;
	}


	private HashSet<HashSet<State>> moorePartition( HashSet<HashSet<State>> P, HashSet<State> S){
		// 2 map nested : State -> (Input symbol -> next Set)
		HashMap<State,
				HashMap<String, HashSet<State>>> setMap = new HashMap<>();

		HashSet<HashSet<State>> partitionS = new HashSet<>();
		boolean found = true;

		// classify states
		for(State s : S){
			HashSet<Transition> transitionsOutgoings = getOutgoingTransitionsFrom(s);

			// for all the transition, retrieve the next state set and update the map
			for(Transition t : transitionsOutgoings){
				if(!setMap.containsKey(s)){
					HashMap<String, HashSet<State>> inputstoSet = new HashMap<>();
					inputstoSet.put(t.getInput(),getSet(t.getTo(),P));
					setMap.put(s, inputstoSet);
				}else{
					HashMap<String, HashSet<State>> inputstoSet = setMap.get(s);
					if(!inputstoSet.containsKey(t.getInput())){
						inputstoSet.put(t.getInput(),getSet(t.getTo(),P));
					}
				}
			}

		}

		// create partition
		for(State s1 : S){
			HashMap<String, HashSet<State>> transitions_s1 = setMap.get(s1);
			HashSet<State> candidateSet = new HashSet<>();
			candidateSet.add(s1);


			for(State s2 : S){
				if(!s1.equals(s2) ){
					/*if(s1.isFinalState() && s2.isFinalState()){
						candidateSet.add(s2);
						continue;
					}*/

					HashMap<String, HashSet<State>> transitions_s2 = setMap.get(s2);


					/*
					for (String a : transitions_s1.keySet()) {
						if (transitions_s1.get(a).equals(transitions_s2.get(a))) {
							found = true;
						} else {
							found = false;
							break;
						}
					}
					if (found)
						candidateSet.add(s2);
					*/

					 if ( (transitions_s1 == null && transitions_s2 == null) || transitions_s1.equals(transitions_s2)) {

						 candidateSet.add(s2);
					 }

				}
			}

			partitionS.add(candidateSet);

		}

		return partitionS;

	}



	public void minimizeMoore(){
		if (!isDeterministic(this)) {
			Automaton a = this.determinize();
			this.initialState = a.initialState;
			this.delta = a.delta;
			this.states = a.states;
			this.adjacencyListOutgoing = a.getAdjacencyListOutgoing();
			this.adjacencyListIncoming = a.getAdjacencyListIncoming();
		}

		this.removeUnreachableStates();

		HashSet<HashSet<State>> Pnew = new HashSet<>();
		Pnew.add(setSubtraction(states, getFinalStates()));
		Pnew.add(getFinalStates());

		HashSet<HashSet<State>> P ;

		do{
			P = new HashSet<>(Pnew);
			Pnew = new HashSet<>(Collections.<HashSet<State>>emptySet());

			for(HashSet<State> S : P) {
				Pnew.addAll(moorePartition(P, S));
			}


		}while(!P.equals(Pnew));


		constructMinimumAutomatonFromPartition(P);



	}



	public void minimizeHopcroft(){
		if (!isDeterministic(this)) {
			Automaton a = this.determinize();
			this.initialState = a.initialState;
			this.delta = a.delta;
			this.states = a.states;
			this.adjacencyListOutgoing = a.getAdjacencyListOutgoing();
			this.adjacencyListIncoming = a.getAdjacencyListIncoming();
		}

		this.removeUnreachableStates();


		// the partition P
		HashSet<HashSet<State>> P = new HashSet<>();
		P.add(this.getFinalStates());
		P.add(setSubtraction(this.states, this.getFinalStates()) );

		//the partition W
		HashSet<HashSet<State>> W = new HashSet<>();
		W.add(this.getFinalStates());

		HashSet<State> A = new HashSet<>();
		HashSet<State> X;
		List<HashSet<State>> listYs;
		//Random r = new Random();

		while(!W.isEmpty()){
			//choose and remove a set A from W

			for(HashSet<State> s : W){
				A = s;
				//	if(r.nextInt(2) == 0)
				break;
			}
			W.remove(A);

			for(String c : getAlphabet(this)){
				// select a X set for which a transition in c leads to a state in A
				X = getXSet(A,c);

				// list of set Y in P such that X intersect Y != empty and Y \ X != empty
				listYs = getYList(P, X);

				for(HashSet<State> Y : listYs){
					HashSet<State> xyintersection = setIntersection(X,Y);
					HashSet<State> yxsubtraction = setSubtraction(Y,X);

					P.remove(Y);
					P.add(xyintersection);
					P.add(yxsubtraction);

					if(W.contains(Y)){
						W.remove(Y);
						W.add(xyintersection);
						W.add(yxsubtraction);

					}else{
						if(xyintersection.size() <= yxsubtraction.size()){
							W.add(xyintersection);
						}else
							W.add(yxsubtraction);
					}
				}

			}
		}

		// construct the minimum automata
		constructMinimumAutomatonFromPartition(P);

	}



	private void constructMinimumAutomatonFromPartition(HashSet<HashSet<State>> P) {
		HashMap<State, State> automatonStateBinding = new HashMap<>();

		int num = 0;
		initChar++;

		this.states = new HashSet<State>();

		for(HashSet<State> macroState : P){
			boolean isInitialState = isPartitionInitialState(macroState);
			boolean isFinalState = isPartitionFinalState(macroState);

			String macroStatename = (initChar) + String.valueOf(num++);


			State mergedMacroState = new State(macroStatename, isInitialState, isFinalState);

			this.states.add(mergedMacroState);

			if(isInitialState)
				this.initialState = mergedMacroState;

			for(State s : macroState)
				automatonStateBinding.put(s, mergedMacroState);
		}

		HashSet<Transition> newDelta = new HashSet<>();

		for(Transition t : this.delta){

			newDelta.add(new Transition(automatonStateBinding.get(t.getFrom()), automatonStateBinding.get(t.getTo()), t.getInput(), ""));
		}

		this.delta = newDelta;
		this.computeAdjacencyList();
	}

	/**
	 * Gets the adjacency list of the automaton.
	 */
	public HashMap<State, HashSet<Transition>> getAdjacencyListOutgoing() {
		return adjacencyListOutgoing;
	}

	public HashMap<State, HashSet<Transition>> getAdjacencyListIncoming() {
		return adjacencyListIncoming;
	}

	/**
	 * Sets the adjacency list of the automaton.
	 */
	public void setAdjacencyList(HashMap<State, HashSet<Transition>> adjacencyList) {
		this.adjacencyListOutgoing = adjacencyList;
	}


	/**
	 * Reverse automata operation.
	*/
	public void reverse() {

		HashSet<State> newStates = new HashSet<State>();
		HashSet<Transition> newDelta = new HashSet<Transition>();
		HashMap<State, State> mapping = new HashMap<State, State>();

		final State newInitialState = new State("init", true, false);
		newStates.add(newInitialState);

//		for (State s : this.states) {
//			State newState = new State(s.getState(), false , false);
//
//			if (s.isFinalState()) {
//				newState.setFinalState(false);
//				//newState.setInitialState(true);
//				newDelta.add(new Transition(newInitialState, newState, "", ""));
//				//newInitialState = newState;
//			}else if (s.isInitialState())
//				newState.setFinalState(true);
//
//			newStates.add(newState);
//			mapping.put(s, newState);
//		}
//
//		for (Transition t : this.delta) {
//			newDelta.add(new Transition(mapping.get(t.getTo()) , mapping.get(t.getFrom()), t.getInput(), ""));
//		}
//
//		this.delta = newDelta;
//		this.initialState = newInitialState;
//		this.states = newStates;
//		this.computeAdjacencyList();

		// reversing edges
		for (Transition t : this.delta) {
			mapping.put(t.getFrom(),t.getFrom());
			mapping.put(t.getTo(),t.getTo());
			newDelta.add(new Transition(mapping.get(t.getTo()) , mapping.get(t.getFrom()), t.getInput(), ""));
		}

		for (State s : this.states) {
			State newState = mapping.containsKey(s) ? mapping.get(s) : new State(s.getState(), false, false);

			if (s.isFinalState()) {
				newState.setFinalState(false);
				newDelta.add(new Transition(newInitialState, newState, "", ""));
				//newInitialState = newState;
			}

			if (s.isInitialState()) {
				newState.setFinalState(true);
				newState.setInitialState(false);
			}

			newStates.add(newState);
		}

		this.delta = newDelta;
		this.initialState = newInitialState;
		this.states = newStates;
		this.computeAdjacencyList();


	}



	/**
	 * Returns the regular expressions associated to this automaton
	 * using the Brzozowski algebraic method.
	 */

	/*public RegularExpression toRegex() {
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
	}*/


	/**
	 * Returns the regular expressions associated to this automaton
	 * using the Brzozowski algebraic method.
	 */

	public RegularExpression toRegex() {

		Vector<Equation> equations = new Vector<Equation>();

		HashMap<State, Equation> toSubstitute = new HashMap<>();
		boolean equationReplaced = true;
		boolean toSubstituteUpdated = false;

		for (State s : this.getStates()) {
			RegularExpression result = null;
			RegularExpression resultToSameState = null;

			HashSet<Transition> out = this.getOutgoingTransitionsFrom(s);

			if (out.size() > 0) {
				for (Transition t : out) {
					if(!t.getTo().equals(s))
						if (result == null)
							result = new Comp(new GroundCoeff(t.getInput()), new Var(t.getTo()));
						else
							result = new Or(result, new Comp(new GroundCoeff(t.getInput()), new Var(t.getTo())));
					else{
						if(resultToSameState == null)
							resultToSameState = new GroundCoeff(t.getInput());
						else
							resultToSameState = new Or(resultToSameState, new GroundCoeff(t.getInput()));

					}
				}

				if(resultToSameState != null && result != null){
					resultToSameState = new Star(resultToSameState);
					result = new Comp(resultToSameState, result);
				}else if(resultToSameState != null){
					resultToSameState = new Star(resultToSameState);
					result = resultToSameState;
				}

				equations.add(new Equation(s, result));
			} else
				equations.add(new Equation(s, new GroundCoeff("")));
		}

		int indexOfInitialState = 0;


		// search for initial state index and minimize equations first, then add ground formulas
		for (int i = 0; i < equations.size(); ++i) {
			Equation e;

			if (equations.get(i).getLeftSide().isInitialState()) {
				indexOfInitialState = i;
				//break;
			}


			equations.set(i, (e = new Equation(equations.get(i).getLeftSide(), equations.get(i).getE().simplify())));

			if (equations.get(i).isIndipendent()) {
				equations.set(i, equations.get(i).syntetize());
				equations.set(i, new Equation(equations.get(i).getLeftSide(), equations.get(i).getE().simplify()));
			}

			if(e.getE().isGround()) toSubstitute.put(e.getLeftSide(),e);
		}


		// Fix-point
		while (!equations.get(indexOfInitialState).getE().isGround()) {


			// syntetize all the equations
			for(int i = 0; i < equations.size(); i++){

				//System.out.println("Simplifying 1" + equations.get(i).getLeftSide());
				equations.set(i, new Equation(equations.get(i).getLeftSide(),
						equations.get(i).getE().simplify()));

				if (equations.get(i).isIndipendent()) {
					equations.set(i, equations.get(i).syntetize());
					equations.set(i, new Equation(equations.get(i).getLeftSide(), equations.get(i).getE().simplify()));

					//System.out.println("Syntetized 1" + equations.get(i).getLeftSide());

					// replacing in toSubstitute
					if( toSubstitute.containsKey(equations.get(i).getLeftSide())){
						toSubstitute.replace(equations.get(i).getLeftSide(), equations.get(i));
					}else {
						// add to toSubstitute if the formula is ground
						if (equations.get(i).getE().isGround()) {
							toSubstitute.put(equations.get(i).getLeftSide(), equations.get(i));
						}
					}

				}

			}

			// heuristic: if no equation has been replaced, pick one and add to toSubstitute set
			if(!equationReplaced){
				for(int l = 0 ; l < equations.size(); l++){
					if(l != indexOfInitialState && !toSubstitute.containsKey(equations.get(l).getLeftSide())){
						toSubstitute.put(equations.get(l).getLeftSide(), equations.get(l));
						break;
					}
				}
			}


			equationReplaced = false;
			toSubstituteUpdated = false;

			for (State s : toSubstitute.keySet()) {

				// search for equations to replace
				for(int i = 0 ; i < equations.size(); i++){

					// Synthetize the indipendent equations
					if (!equations.get(i).isIndipendent()) {

						equations.set(i, equations.get(i).syntetize());
						//System.out.println("Simplifying 2" + equations.get(i).getLeftSide());
						equations.set(i, new Equation(equations.get(i).getLeftSide(), equations.get(i).getE().simplify()));

						//System.out.println("Syntetized 2" + equations.get(i).getLeftSide());

						// replacing in toSubstitute
						if( toSubstitute.containsKey(equations.get(i).getLeftSide())){
							toSubstitute.replace(equations.get(i).getLeftSide(), equations.get(i));
						}else {
							// add to toSubstitute if the formula is ground
							if (equations.get(i).getE().isGround()) {
								toSubstitute.put(equations.get(i).getLeftSide(), equations.get(i));
							}
						}

					}
					if(!equations.get(i).getE().isGround()) {
						if(equations.get(i).getE().contains(s)) {
							Equation getFromSubstituteMap = toSubstitute.get( s );

							// substitute
							equations.set(i, new Equation(equations.get(i).getLeftSide(),
									equations.get(i).getE().replace(s, getFromSubstituteMap.getE() )));
							equationReplaced = true;
							//System.out.println("Replaced: " + equations.get(i).getLeftSide());

							// replacing in toSubstitute
							if( toSubstitute.containsKey(equations.get(i).getLeftSide())){
								toSubstitute.replace(equations.get(i).getLeftSide(), equations.get(i));
								break;
							}else {
								// add to toSubstitute if the formula is ground
								if (equations.get(i).getE().isGround()) {
									toSubstitute.put(equations.get(i).getLeftSide(), equations.get(i));
									toSubstituteUpdated = true;
									break;
								}
							}

							if (equations.get(i).getE().isGround()) break;

						}

					}
				}
				if(toSubstituteUpdated) break;  // to avoid ConcurrentModificationException

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

		/*State next = null;
		if (q.isInitialState()) {
			for (Transition t : this.getOutgoingTransitionsFrom(q))
				next = t.getTo();
		} else
			next = q;*/

		MultiValueMap<String, State> B = build(q);

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
		return this.prettyPrint();
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
			Automaton second = Automaton.intersection(Automaton.complement(this), (Automaton) other);
			Automaton c = Automaton.union(first, second);
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