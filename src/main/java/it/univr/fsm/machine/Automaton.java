package it.univr.fsm.machine;

import it.univr.fsm.equations.*;

import org.apache.commons.lang3.StringUtils;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.IRFactory;

import it.univr.dimpminus.DImpMinus;
import it.univr.dimpminus.ParseException;
import it.univr.dimpminus.TokenMgrError;
import it.univr.exception.*;

import it.univr.fsm.equations.Comp;
import it.univr.fsm.equations.Equation;
import it.univr.fsm.equations.GroundCoeff;
import it.univr.fsm.equations.Or;
import it.univr.fsm.equations.RegularExpression;
import it.univr.fsm.equations.Var;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.collect.HashMultimap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Finite-state automaton class.
 * 
 * @author <a href="mailto:vincenzo.arceri@univr.it">Vincenzo Arceri</a>
 * @version 1.0
 * @since 24-10-2016
 */
public class Automaton {

	public static void main(String[] args) {

		Automaton a = Automaton.star(Automaton.makeRealAutomaton("a"));



		Automaton b = Automaton.reverse(a);
		b.minimize();
		System.out.println(Automaton.singleSubstring(a, 0));
	}

	/**
	 * Starting symbol to name the states.
	 */
	public static char initChar = 'a';

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

	private String singleString;

	private boolean isSingleValue;

	/**
	 * Constructs a new automaton.
	 * @param delta the set of transitions
	 * @param states the set of states
	 */
	public Automaton(HashSet<Transition> delta, HashSet<State> states)  {
		this.delta = delta;
		this.states = states;
		this.computeAdjacencyList();

		this.singleString = null;
		this.isSingleValue = false;
	}

	public String getSingleString() {
		return this.singleString;
	}

	public void setSingleString(String s) {
		this.singleString = s;
	}

	public boolean isSingleString() {
		return isSingleValue;
	}


	public Automaton() {}


	private void computeAdjacencyList() {	
		adjacencyListOutgoing = new HashMap<State, HashSet<Transition>>();

		for (Transition t : getDelta()) {
			if (!adjacencyListOutgoing.containsKey(t.getFrom()))
				adjacencyListOutgoing.put(t.getFrom(), new HashSet<Transition>());

			adjacencyListOutgoing.get(t.getFrom()).add(t);	
		}
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

	public boolean recognizesExactlyOneString() {
		for (State f : getStates())
			if (getAdjacencyListOutgoing().get(f) != null && getAdjacencyListOutgoing().get(f).size() > 1)
				return false;

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
	public static boolean isReachable(State f, Automaton a) {
		HashSet<Transition> transitionSet;

		if (f.isInitialState()) {
			return true;
		} else {
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
	public static boolean isEmptyLanguageAccepted(Automaton automaton) {
		automaton.minimize();
		
		if (automaton.isSingleString())
			return false;
		
		return automaton.getFinalStates().isEmpty(); //&& !automaton.states.isEmpty();
	}


	/**
	 * Performs an intersection between multiple automatons
	 * 
	 * @param collection a collection of automatons
	 * @return the intersection
	 */
	public static Automaton intersection(Collection<Automaton> collection){
		Automaton a = null;

		for(Automaton aut: collection)
			a = (a == null) ? aut : Automaton.intersection(a, aut);

		if(a!=null)
			a.minimize();
		return a;
	}

	public boolean isEmptyString() {
		return equals(Automaton.makeEmptyString());
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
		Automaton a = null;

		for(Automaton aut: collection)
			a = (a == null) ? aut : Automaton.minus(a, aut);

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


		if (first.isSingleString() && second.isSingleString()) 
			return Automaton.makeAutomaton(first.getSingleString() + second.getSingleString());

		first = first.isSingleString() ? Automaton.makeRealAutomaton(first.getSingleString()) : first;
		second = second.isSingleString() ? Automaton.makeRealAutomaton(second.getSingleString()) : second;

		HashMap<State, State> mappingFirst = new HashMap<State,State>();
		HashMap<State, State> mappingSecond = new HashMap<State, State>();
		HashSet<Transition> newDelta = new HashSet<Transition>();
		HashSet<State> newStates = new HashSet<State>();
		HashSet<State> firstFinalStates = new HashSet<>();
		HashSet<State> secondInitialStates = new HashSet<>();

		int c = 0;

		// Add all the first automaton states
		for (State s: first.states) {

			// The first automaton states are not final, can be initial states
			mappingFirst.put(s, new State("q" + c++, s.isInitialState(), false));
			newStates.add(mappingFirst.get(s));

			if (s.isFinalState())
				firstFinalStates.add(s);
		}

		// Add all the second automaton states
		for (State s: second.states) {

			// the second automaton states are final, can't be initial states
			mappingSecond.put(s, new State("q" + c++, false, s.isFinalState()));
			newStates.add(mappingSecond.get(s));

			if(s.isInitialState())
				secondInitialStates.add(s);
		}

		// Add all the first automaton transitions
		for (Transition t: first.delta)
			newDelta.add(new Transition(mappingFirst.get(t.getFrom()), mappingFirst.get(t.getTo()), t.getInput()));

		// Add all the second automaton transitions
		for (Transition t: second.delta)
			newDelta.add(new Transition(mappingSecond.get(t.getFrom()), mappingSecond.get(t.getTo()), t.getInput()));

		// Add the links between the first automaton final states and the second automaton initial state

		for (State f: firstFinalStates)
			for(State s : secondInitialStates)
				newDelta.add(new Transition(mappingFirst.get(f), mappingSecond.get(s), ""));

		Automaton a = new Automaton(newDelta, newStates);
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

		automaton = automaton.isSingleString() ? Automaton.totalize(Automaton.makeRealAutomaton(automaton.getSingleString())): Automaton.totalize(automaton.clone());

		// Add states to the mapping, replacing accept states to reject
		for(State s: automaton.states) {
			mapping.put(s, new State(s.getState(), s.isInitialState(), !s.isFinalState()));
			newStates.add(mapping.get(s));
		}

		// Copying delta set
		for (Transition t:  automaton.delta)
			newDelta.add(new Transition(mapping.get(t.getFrom()), mapping.get(t.getTo()), t.getInput()));

		Automaton a = new Automaton(newDelta,newStates);
		a.minimize();
		return a;
	}

	public static Automaton totalize(Automaton automaton) {		
		HashSet<State> newState = new HashSet<>();
		HashSet<Transition> newDelta = new HashSet<>();

		for (State s : automaton.getStates())
			newState.add(s);

		State qbottom = new State("qbottom", false, false);

		newState.add(qbottom);

		for (Transition t : automaton.getDelta())
			newDelta.add(t);

		for (char alphabet = '!'; alphabet <= '~'; ++alphabet) 
			newDelta.add(new Transition(qbottom, qbottom, String.valueOf(alphabet)));

		newDelta.add(new Transition(qbottom, qbottom, String.valueOf(' ')));

		Automaton result = new Automaton(newDelta, newState);

		for (State s : newState)
			for (char alphabet = '!'; alphabet <= '~'; ++alphabet) {
				HashSet<State> states = new HashSet<>();
				states.add(s);

				if (!result.readableCharFromState(states).contains(String.valueOf(alphabet)))
					newDelta.add(new Transition(s, qbottom, String.valueOf(alphabet)));


				if (!result.readableCharFromState(states).contains(String.valueOf(' ')))
					newDelta.add(new Transition(s, qbottom, String.valueOf(' ')));

			}

		return new Automaton(newDelta, newState);
	}


	/**
	 * Does the automata intersection
	 * 
	 * @param first the first automata
	 * @param second the first automata
	 * @return a new automata, the intersection of the first and the second
	 */

	public static Automaton intersection(Automaton first, Automaton second) {

		if (first.isSingleString() && second.isSingleString()) {

			if (first.getSingleString().equals(second.getSingleString()))
				return Automaton.makeAutomaton(first.getSingleString());
			else
				return Automaton.makeEmptyLanguage();

		}
		
		if (first.isSingleString())
			return second.run(first.getSingleString()) ? first : Automaton.makeEmptyLanguage();
		
		if (second.isSingleString())
			return first.run(second.getSingleString()) ? second : Automaton.makeEmptyLanguage();

		// !(!(first) u !(second))
		Automaton notFirst = Automaton.complement(first);
		Automaton notSecond = Automaton.complement(second);
		Automaton union = Automaton.union(notFirst, notSecond);
		Automaton result = Automaton.complement(union);

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
	public static Automaton loadAutomataWithAlternatePattern(String path) {
		BufferedReader br = null;

		HashMap<String, State> mapStates = new HashMap<>();
		HashSet<Transition> delta = new HashSet<Transition>();
		HashSet<State> states = new HashSet<State>();

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

					delta.add(new Transition(current,next,sym));
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

		Automaton a = new Automaton(delta, states);

		return a;
	}

	public static Automaton loadAutomataWithFSM2RegexPattern(String path){
		BufferedReader br = null;


		HashMap<String, State> mapStates = new HashMap<>();
		HashSet<Transition> delta = new HashSet<Transition>();
		HashSet<State> states = new HashSet<State>();

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
				String sym = "";

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
					} else
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
							delta.add(new Transition(current,mapStates.get(toS),sym));
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

		Automaton a = new Automaton(delta, states);

		return a;
	}

	public static Automaton loadAutomataWithJFLAPPattern(String path){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		HashSet<State> newStates = new HashSet<>();
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

				newDelta.add(new Transition(from,to,sym));


			}

			return new Automaton(newDelta, newStates);

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

					delta.add(new Transition(mapStates.get(pieces[0]),mapStates.get(pieces[1]),pieces[2]));

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

		Automaton a = new Automaton(delta, states);

		return a;
	}


	/**
	 * Runs a string on the automaton.
	 * 
	 * @param s the string
	 * @return true if the string is accepted by the automaton, false otherwise
	 */
	public boolean run(String s) {
		if (isSingleString())
			return s.equals(getSingleString());

		return run(s, getInitialState());
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
	 * Renames the states of the automaton.
	 * @return the automaton with renamed states.
	 */
	private Automaton deMerge(char initChar) {
		HashSet<State> newStates = new HashSet<State>();
		HashSet<Transition> newGamma = new HashSet<Transition>();
		HashMap<String, State> mapping = new HashMap<String, State>();

		int counter = 0;

		for (State s : this.states) {
			State newState = new State(String.valueOf(initChar) + counter, s.isInitialState(), s.isFinalState());
			mapping.put(s.getState(), newState);
			newStates.add(newState);
			counter++;
		}


		for (Transition t : this.delta)
			newGamma.add(new Transition(mapping.get(t.getFrom().getState()), mapping.get(t.getTo().getState()), t.getInput()));

		return new Automaton(newGamma, newStates);
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

	public static Automaton makeSingleString(String s) {
		Automaton a = new Automaton();
		a.singleString = s;
		a.isSingleValue = true;
		return a;
	}

	/**
	 * Builds an automaton from a given string.
	 * 
	 * @param s the string.
	 * @return a new automaton recognize the given string.
	 */
	public static Automaton makeRealAutomaton(String s) {


		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State initialState = new State("q0", true, false);
		states.add(initialState);

		if (s.isEmpty()) {
			initialState.setFinalState(true);
			return new Automaton(delta, states);
		}

		State state = initialState;


		ArrayList<String> input = (ArrayList<String>) toList(s);

		for (int i = 0; i < input.size(); ++i) {
			State next = new State("q" + (i+1), false, i == input.size() -1 ? true : false );
			states.add(next);		

			/*if (input.get(i).equals(" "))
				gamma.add(new Transition(state, next, "", ""));
			else	*/
			delta.add(new Transition(state, next, input.get(i)));

			state = next;
		}

		Automaton result = new Automaton(delta, states);

		result.isSingleValue = false;
		result.singleString = null;
		return result;
	}

	public static Automaton makeAutomaton(String s) {
		Automaton a = new Automaton();
		a.singleString = s;
		a.isSingleValue = true;

		return a;
	}

	public HashSet<Transition> getOutgoingTransitionsFrom(State s) {
		HashSet<Transition> result = adjacencyListOutgoing.get(s);

		if (result == null) {
			adjacencyListOutgoing.put(s,  new HashSet<Transition>());
			return new HashSet<Transition>();
		}

		return result;

	}

	public void setAdjacencyListOutgoing(HashMap<State, HashSet<Transition>> adjacencyListOutgoing) {
		this.adjacencyListOutgoing = adjacencyListOutgoing;
	}


	public static Automaton union(HashSet<Automaton> automata) {
		Automaton result = Automaton.makeEmptyLanguage();


		for (Automaton a : automata)
			result = Automaton.union(result, a);

		return result;
	}

	/**
	 * Union operation between two automata.
	 * 
	 * @param a1 first automaton.
	 * @param a2 second automaton.
	 * @return the union of the two automata.
	 */
	public static Automaton union(Automaton a1, Automaton a2) {

		Automaton first;
		Automaton second;

		if (a1.isSingleString())
			first = Automaton.makeRealAutomaton(a1.getSingleString());
		else 
			first = a1;

		if (a2.isSingleString())
			second = Automaton.makeRealAutomaton(a2.getSingleString());
		else 
			second = a2;

		State newInitialState = new State("initialState", true, false);
		HashSet<Transition> newGamma = new HashSet<Transition>();
		HashSet<State> newStates = new HashSet<State>();

		int c = 1;
		HashMap<State, State> mappingA1 = new HashMap<State, State>(); 
		HashMap<State, State> mappingA2 = new HashMap<State, State>(); 

		newStates.add(newInitialState);

		State initialA1 = null; 
		State initialA2 = null;

		for (State s : first.states) {

			mappingA1.put(s, new State("q" + c++, false, s.isFinalState()));

			newStates.add(mappingA1.get(s));



			if (s.isInitialState())
				initialA1 = mappingA1.get(s);
		}

		for (State s : second.states) {
			mappingA2.put(s, new State("q" + c++, false, s.isFinalState()));
			newStates.add(mappingA2.get(s));



			if (s.isInitialState())
				initialA2 = mappingA2.get(s);
		}

		for (Transition t : first.delta)
			newGamma.add(new Transition(mappingA1.get(t.getFrom()), mappingA1.get(t.getTo()), t.getInput()));

		for (Transition t : second.delta)
			newGamma.add(new Transition(mappingA2.get(t.getFrom()), mappingA2.get(t.getTo()), t.getInput()));

		newGamma.add(new Transition(newInitialState, initialA1, ""));
		newGamma.add(new Transition(newInitialState, initialA2, ""));

		Automaton a =  new Automaton(newGamma, newStates);
		a.minimize();
		return a;
	}


	public static Automaton star(Automaton a) {
		Automaton result =  a.isSingleString() ? Automaton.makeRealAutomaton(a.getSingleString()) : a.clone();

		for (State f : result.getFinalStates())
			for (State i : result.getInitialStates()) {
				i.setFinalState(true);
				result.getDelta().add(new Transition(f, i, ""));
			}

		result.minimize();
		return result;
	}

	public static Automaton union(Automaton... automata) {
		Automaton result = Automaton.makeEmptyLanguage();

		for (Automaton a : automata)
			result = Automaton.union(a, result);

		return result;
	}

	/**
	 * Returns an automaton recognize any string.
	 */
	public static Automaton makeTopLanguage() {
		HashSet<State> newStates = new HashSet<State>();
		HashSet<Transition> newGamma = new HashSet<Transition>();
		State initialState = new State("q0", true, true);

		newStates.add(initialState);

		for (char alphabet = '!'; alphabet <= '~'; ++alphabet) 
			newGamma.add(new Transition(initialState, initialState, String.valueOf(alphabet)));
		newGamma.add(new Transition(initialState, initialState, String.valueOf(' ')));

		return new Automaton(newGamma, newStates);
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
			newGamma.add(new Transition(initialState, initialState, String.valueOf(alphabet)));
		newGamma.add(new Transition(initialState, initialState, String.valueOf(' ')));

		return new Automaton(newGamma, newStates);
	}

	/**
	 * Returns an automaton recognize the empty string.
	 */
	public static Automaton makeEmptyString() {

		HashSet<State> newStates = new HashSet<State>();
		HashSet<Transition> newDelta = new HashSet<Transition>();

		State q0 = new State("q0", true, true);

		newStates.add(q0);

		return new Automaton(newDelta, newStates);
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

		for(State s : set)
			solution.addAll(epsilonClosure(s));

		return solution;
	}

	private HashSet<State> moveNFA(HashSet<State> set, String sym){
		HashSet<State> solution = new HashSet<>();

		for(State s : set) {
			HashSet<Transition> outgoing = getOutgoingTransitionsFrom(s);
			for(Transition t : outgoing) {
				if(t.getInput().equals(sym)) {
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

	public Automaton determinize() {
		HashSet<State> newStates = new HashSet<>();
		HashSet<Transition> newDelta = new HashSet<>();

		HashMap<HashSet<State>, Boolean> statesMarked = new HashMap<>();
		HashMap<HashSet<State>,State> statesName = new HashMap<>();
		int num = 0;
		LinkedList<HashSet<State>> unMarkedStates = new LinkedList<>();

		HashSet<State> temp;

		temp = epsilonClosure(this.getInitialStates());
		statesName.put(temp,new State("q" + String.valueOf(num++) , true, isPartitionFinalState(temp)));

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

				newDelta.add(new Transition(statesName.get(T), statesName.get(temp), alphabet));
			}
		}

		Automaton a = new Automaton(newDelta, newStates);
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
		return isSingleString() ? getSingleString()  : this.toRegex().toString();
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

	//	/**
	//	 * Returns the set of strings readable from the state s.
	//	 * 
	//	 * @param s state of this automaton.
	//	 */
	//	private HashSet<String> readableCharFromState(State s) {
	//
	//		HashSet<String> result = new HashSet<String>();
	//
	//		for (Transition t : this.getOutgoingTransitionsFrom(s)) {
	//			if (!t.getInput().equals(""))
	//				result.add(t.getInput());
	//		}
	//
	//		return result;
	//
	//	}

	/**
	 * Removes the unreachable states of an automaton.
	 */
	public void removeUnreachableStates() {
		HashSet<State> reachableStates = new HashSet<State>();
		reachableStates.add(this.getInitialState());

		HashSet<State> newStates = new HashSet<State>();
		newStates.add(this.getInitialState());		

		do {
			HashSet<State> temp = new HashSet<State>();
			for (State s : newStates) {
				//				for (String alphabet : this.readableCharFromState(s))
				for (Transition t : this.getOutgoingTransitionsFrom(s))
					//						if (t.getFrom().equals(s)/* && t.getInput().equals(alphabet)*/)
					temp.add(t.getTo());
			}

			temp.removeAll(reachableStates);
			newStates = temp;

			reachableStates.addAll(newStates);

		} while (!newStates.isEmpty());


		//		int oldSize;
		//		do {
		//			oldSize = newStates.size();
		//
		//			for (State s : newStates) 
		//				for (Transition t : this.getOutgoingTransitionsFrom(s))
		//					newStates.add(t.getTo());
		//
		//		} while (newStates.size() != oldSize);

		states.removeIf(s -> !reachableStates.contains(s));
		delta.removeIf(t -> !reachableStates.contains(t.getFrom()));
	}

	/**
	 * Brzozowski's minimization algorithm.
	 */
	public void minimize() {

		if (isSingleString())
			return;
		
		if (!isDeterministic(this)) {
			Automaton a = this.determinize();
			this.delta = a.delta;
			this.states = a.states;
			this.adjacencyListOutgoing = a.getAdjacencyListOutgoing();
		}

		Automaton a = Automaton.reverse(this).determinize();
		a.removeUnreachableStates();
		a = Automaton.reverse(a).determinize();
		a.removeUnreachableStates();

		this.delta = a.delta;
		this.states = a.states;
		this.adjacencyListOutgoing = a.getAdjacencyListOutgoing();


		//				this.minimizeHopcroft();
		//		
		//				Automaton a = this.deMerge(++initChar); 
		//				this.initialState = a.initialState;
		//				this.states = a.states;
		//				this.delta = a.delta;
		//				this.adjacencyListOutgoing = a.getAdjacencyListOutgoing();


	}

	//	public void minimizeBrowozozwi() {
	//
	//		if (!isDeterministic(this)) {
	//			Automaton a = this.determinize();
	//			this.initialState = a.initialState;
	//			this.delta = a.delta;
	//			this.states = a.states;
	//			this.adjacencyListOutgoing = a.getAdjacencyListOutgoing();
	//			this.adjacencyListIncoming = a.getAdjacencyListIncoming();
	//		}
	//
	//
	//		this.reverse();
	//		Automaton a = this.determinize();
	//		a.reverse();
	//		a = a.determinize();
	//
	//		this.initialState = a.initialState;
	//		this.delta = a.delta;
	//		this.states = a.states;
	//		this.adjacencyListIncoming = a.getAdjacencyListIncoming();
	//		this.adjacencyListOutgoing = a.getAdjacencyListOutgoing();
	//	}

	public static HashSet<String> getAlphabet(Automaton a){
		HashSet<String> alphabet = new HashSet<String>();

		for (Transition t : a.delta)
			//			if (!alphabet.contains(t.getInput()))
			alphabet.add(t.getInput());

		return alphabet;
	}

	//	private State getOutgoingStatefromTransitionSymbol(State s, String symbol){
	//		for(Transition t : delta){
	//			if(t.getInput().equals(symbol) && t.getFrom().equals(s)){
	//				return t.getTo();
	//			}
	//		}
	//		return null;
	//	}

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

	private HashSet<Transition> getIncomingTransitionsTo(State s) {
		HashSet<Transition> result = new HashSet<Transition>();

		for (Transition t : this.delta)
			if (t.getTo().equals(s))
				result.add(t);

		return result;
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

			if(!setIntersection(X,Ytemp).isEmpty() && !setSubtraction(Ytemp,X).isEmpty())
				Ys.add(Ytemp);


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
			//			this.initialState = a.initialState;
			this.delta = a.delta;
			this.states = a.states;
			this.adjacencyListOutgoing = a.getAdjacencyListOutgoing();
			//			this.adjacencyListIncoming = a.getAdjacencyListIncoming();
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
			//			this.initialState = a.initialState;
			this.delta = a.delta;
			this.states = a.states;
			this.adjacencyListOutgoing = a.getAdjacencyListOutgoing();
			//			this.adjacencyListIncoming = a.getAdjacencyListIncoming();
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

			//			if(isInitialState)
			//				this.initialState = mergedMacroState;

			for(State s : macroState)
				automatonStateBinding.put(s, mergedMacroState);
		}

		HashSet<Transition> newDelta = new HashSet<>();

		for(Transition t : this.delta)
			newDelta.add(new Transition(automatonStateBinding.get(t.getFrom()), automatonStateBinding.get(t.getTo()), t.getInput()));

		this.delta = newDelta;
		this.computeAdjacencyList();
	}

	/**
	 * Gets the adjacency list of the automaton.
	 */
	public HashMap<State, HashSet<Transition>> getAdjacencyListOutgoing() {
		return adjacencyListOutgoing;
	}

	//	public HashMap<State, HashSet<Transition>> getAdjacencyListIncoming() {
	//		return adjacencyListIncoming;
	//	}

	/**
	 * Sets the adjacency list of the automaton.
	 */
	public void setAdjacencyList(HashMap<State, HashSet<Transition>> adjacencyList) {
		this.adjacencyListOutgoing = adjacencyList;
	}


	/**
	 * Reverse automata operation.
	 */
	public static Automaton reverse(Automaton a) {

		//		for (State s : this.states) {
		//			State newState = new State(s.getState(), false , false);
		//
		//			if (s.isFinalState() && s.isInitialState()) {
		//				newState.setFinalState(true);
		//				newState.setInitialState(true);
		//				newDelta.add(new Transition(newInitialState, newState, ""));
		//			} else if (s.isFinalState()) {
		//				newState.setFinalState(false);
		//				newDelta.add(new Transition(newInitialState, newState, ""));
		//			} else if (s.isInitialState())
		//				newState.setFinalState(true);
		//
		//			newStates.add(newState);
		//			mapping.put(s.getState(), newState);
		//		}
		//
		//		for (Transition t : this.delta) {
		//			newDelta.add(new Transition(mapping.get(t.getTo().getState()) , mapping.get(t.getFrom().getState()), t.getInput()));
		//		}
		//
		//		this.delta = newDelta;
		//		this.initialState = newInitialState;
		//		this.states = newStates;
		//		this.isSingleValue = false;
		//		this.singleString = null;
		//		this.computeAdjacencyList();

		//		// reversing edges
		//		for (Transition t : a.delta) {
		//			mapping.put(t.getFrom(),t.getFrom());
		//			mapping.put(t.getTo(),t.getTo());
		//			newDelta.add(new Transition(mapping.get(t.getTo()) , mapping.get(t.getFrom()), t.getInput()));
		//		}
		//
		//		for (State s : a.states) {
		//			State newState = mapping.containsKey(s) ? mapping.get(s) : new State(s.getState(), false, false);
		//
		//			if (s.isFinalState() && s.isInitialState()) {
		//				newState.setFinalState(true);
		//				newState.setInitialState(false);
		//				newDelta.add(new Transition(newInitialState, newState, ""));
		//			} else if (s.isFinalState()) {
		//				newState.setFinalState(false);
		//				newDelta.add(new Transition(newInitialState, newState, ""));
		//			} else if (s.isInitialState()) {
		//				newState.setFinalState(true);
		//				newState.setInitialState(false);
		//			}
		//
		//			newStates.add(newState);
		//		}
		//
		//		return new Automaton(newDelta, newStates);


		if (a.isSingleString())
			return Automaton.makeAutomaton(new StringBuilder(a.getSingleString()).reverse().toString());


		HashSet<State> newStates = new HashSet<State>();
		HashSet<Transition> newDelta = new HashSet<Transition>();
		HashMap<State, State> mapping = new HashMap<State, State>();

		State newInitialState = new State("init", true, false);
		newStates.add(newInitialState);

		for (State s : a.getStates()) {
			State newState = new State(s.getState(), false, false);

			if (s.isFinalState() && s.isInitialState()) {
				newState.setFinalState(true);
				newDelta.add(new Transition(newInitialState, newState, ""));
			} else

				if (s.isFinalState()) {
					newState.setFinalState(false);
					newDelta.add(new Transition(newInitialState, newState, ""));
				} else if (s.isInitialState()) {
					newState.setFinalState(true);
					newState.setInitialState(false);
				}

			mapping.put(s, newState);
			newStates.add(newState);
		}

		for (Transition t : a.getDelta())
			newDelta.add(new Transition(mapping.get(t.getTo()) , mapping.get(t.getFrom()), t.getInput()));

		Automaton r =  new Automaton(newDelta, newStates);
		return r;
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

			/**
			 * This fixes the "unsoundness problem"
			 */
			if (equations.get(i).getLeftSide().isFinalState()) {				
				equations.get(i).setE(new Or(equations.get(i).getE(), new GroundCoeff("")));
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
		return e.getProgram();
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

	public HashMultimap<String, State> build(State q) {
		HashMultimap<String, State> Iq = HashMultimap.create();
		build_tr(q, "", new HashSet<Transition>(), Iq);
		return Iq;
	}

	//	private void build_tr(State q, String stm, HashSet<Transition> mark, HashMultimap<String, State> Iq, int opcl) {
	//
	//		HashMultimap<String, State> delta_q = HashMultimap.create();
	//
	//		for (Transition t : this.getOutgoingTransitionsFrom(q)) 
	//			delta_q.put(t.getInput(), t.getTo());
	//
	//		while (!delta_q.isEmpty()) {
	//
	//			String sigma = null; 
	//			State p = null;
	//
	//			for (String s : delta_q.keySet()) {
	//				sigma = (String) s;
	//				for (State state : delta_q.get(s)) {
	//					p = state;
	//					break;
	//				}
	//
	//				//				p = ((ArrayList<State>) delta_q.get((String) s)).get(0);
	//				break;
	//			}
	//
	//			delta_q.remove(sigma, p); //FIX?
	//
	//			if (!mark.contains(new Transition(q, p, sigma))) {
	//				mark.add(new Transition(q, p, sigma));
	//
	//				if (!isPuntaction(sigma) && !p.isFinalState()) {
	//					HashSet<Transition> markTemp = (HashSet<Transition>) mark.clone();
	//					markTemp.add(new Transition(q, p, sigma));
	//
	//					build_tr(p, stm + sigma, markTemp, Iq, opcl);
	//				} else if (isPuntaction(sigma)) {
	//					if (sigma.equals(")")) {
	//						if (opcl == 1) {
	//							opcl--;
	//
	//							if (isJS(stm))
	//								Iq.put(stm + sigma, p);
	//						} else {
	//							HashSet<Transition> markTemp = (HashSet<Transition>) mark.clone();
	//							markTemp.add(new Transition(q, p, sigma));
	//							build_tr(p, stm + sigma, markTemp, Iq, opcl-1);
	//						}
	//					} else if (sigma.equals("(")) {
	//
	//						if (opcl > 1) {
	//							HashSet<Transition> markTemp = (HashSet<Transition>) mark.clone();
	//							markTemp.add(new Transition(q, p, sigma));
	//							build_tr(p, stm + sigma, markTemp, Iq, opcl + 1);
	//						} else {
	//							opcl++;
	//							Iq.put(stm + sigma, p);
	//						}
	//					} else if (sigma.equals(";") && opcl > 0) {		
	//						HashSet<Transition> markTemp = (HashSet<Transition>) mark.clone();
	//						markTemp.add(new Transition(q, p, sigma));
	//						build_tr(p, stm + sigma, markTemp, Iq, opcl);
	//					} else if (sigma.equals("{") || sigma.equals("}") && !isJS(stm + sigma)) {	// Is not a block, it is an object
	//
	//						HashSet<Transition> markTemp = (HashSet<Transition>) mark.clone();
	//						markTemp.add(new Transition(q, p, sigma));
	//						build_tr(p, stm + sigma, markTemp, Iq, opcl);
	//
	//					} else {
	//						Iq.put(stm + sigma, p);
	//					}
	//
	//					//					else if (isJS(stm + sigma) || (isJS(stm) && sigma.equals(")")))
	//					//						Iq.put(stm + sigma, p);
	//
	//				} 
	//
	//				if (p.isFinalState() /*&& isJS(stm + sigma) */&& opcl == 0) 
	//					Iq.put(stm + sigma, p);
	//
	//			}		
	//		}
	//	}

	private void build_tr(State q, String stm, HashSet<Transition> mark, HashMultimap<String, State> Iq) {

		HashMultimap<String, State> delta_q = HashMultimap.create();

		for (Transition t : this.getOutgoingTransitionsFrom(q)) 
			delta_q.put(t.getInput(), t.getTo());

		while (!delta_q.isEmpty()) {

			String sigma = null; 
			State p = null;

			for (String s : delta_q.keySet()) {
				sigma = (String) s;
				for (State state : delta_q.get(s)) {
					p = state;
					break;
				}

				//				p = ((ArrayList<State>) delta_q.get((String) s)).get(0);
				break;
			}

			delta_q.remove(sigma, p); //FIX?

			if (!mark.contains(new Transition(q, p, sigma))) {
				mark.add(new Transition(q, p, sigma));

				if (!isPuntaction(sigma) && !p.isFinalState()) {
					HashSet<Transition> markTemp = (HashSet<Transition>) mark.clone();
					markTemp.add(new Transition(q, p, sigma));

					build_tr(p, stm + sigma, markTemp, Iq);
				} 

				if (isPuntaction(sigma)) 
					Iq.put(stm + sigma, p);

				if (p.isFinalState()) 
					Iq.put(stm + sigma, p);

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


		Automaton a = new Automaton(delta, Q_first);

		return a; 
	}


	private void stmSyn_tr(State q, HashSet<State> q_first, HashSet<State> f_first, HashSet<Transition> delta, HashSet<State> visited) {

		/*State next = null;
		if (q.isInitialState()) {
			for (Transition t : this.getOutgoingTransitionsFrom(q))
				next = t.getTo();
		} else
			next = q;*/

		HashMultimap<String, State> B = build(q);

		visited.add(q);
		HashSet<State> W = new HashSet<State>();

		for (State s : B.values())
			q_first.add(s);

		f_first.retainAll((HashSet<State>) this.getFinalStates()); 

		for (String sigma : B.keySet())
			for (State to : (Set<State>) B.get((String) sigma))
				delta.add(new Transition(q, to, sigma));

		for (State s : B.values())  
			W.add(s);

		W.removeAll(visited);

		for (State p : W) 
			stmSyn_tr(p, q_first, f_first, delta, visited);
	}

	private boolean dfs(State current, Set<State> whiteSet, Set<State> graySet, Set<State> blackSet ) {
		//move current to gray set from white set and then explore it.
		moveVertex(current, whiteSet, graySet);
		for(Transition t : getOutgoingTransitionsFrom(current)) {
			State neighbor = t.getTo();

			//if in black set means already explored so continue.
			if (blackSet.contains(neighbor)) {
				continue;
			}
			//if in gray set then cycle found.
			if (graySet.contains(neighbor)) {
				return true;
			}
			if(dfs(neighbor, whiteSet, graySet, blackSet)) {
				return true;
			}
		}

		//move vertex from gray set to black set when done exploring.
		moveVertex(current, graySet, blackSet);
		return false;
	}


	//
	//	public HashSet<State> dfs() {
	//		HashMap<State, String> color = new HashMap<State, String>();
	//		HashMap<State, State> pi = new HashMap<State, State>();
	//
	//		HashSet<State> result = new HashSet<State>();
	//
	//		for (State s : this.getStates())
	//			color.put(s, "w");
	//
	//		for (State s : this.getStates())
	//			result.addAll(dfs_visit(s, color, pi));
	//
	//		return result;
	//	}
	//
	//	public HashSet<State> dfs_visit(State s, HashMap<State, String> color, HashMap<State, State> pi) {
	//		HashSet<State> cycles = new HashSet<State>();
	//
	//		color.put(s, "g");
	//
	//		for (Transition t : this.getOutgoingTransitionsFrom(s)) {
	//			State v = t.getTo();
	//
	//			if (color.get(v).equals("w")) {
	//				pi.put(v, s);
	//				cycles.addAll(dfs_visit(v, color, pi));
	//			} else if (color.get(v).equals("g")) {
	//				cycles.add(v);
	//			}
	//		}
	//
	//		color.put(s, "b");
	//		return cycles;
	//	}
	//
	//
	//	public HashMap<State, String> bfs() {
	//		return bfsAux(this.getInitialState());
	//	}

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

	private void moveVertex(State vertex, Set<State> sourceSet, Set<State> destinationSet) {
		sourceSet.remove(vertex);
		destinationSet.add(vertex);
	}

	/**
	 * Returns the sets of final states.
	 * 
	 * @return an HashSet of final states.
	 */
	public HashSet<State> getFinalStates() {
		
		assertTrue(!isSingleString());
		
		HashSet<State> result = new HashSet<State>();

		for (State s : this.states) 
			if (s.isFinalState())
				result.add(s);

		return result;
	}

	private boolean isPuntaction(String s) {
		return s.equals("$") || s.equals("{") || s.equals("}")  || s.equals(";") || s.equals("\n")|| s.equals(")") || s.equals("(");
	}

	public HashSet<String> getNumbers() {
		HashMap<State, String> bfs = new HashMap<State, String>();

		for (Transition t : this.getOutgoingTransitionsFrom(this.getInitialState()))
			bfs.putAll(this.bfsAux(t.getTo()));

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
		HashMap<State, HashSet<String>> languages = new HashMap<State, HashSet<String>>();
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


		int i = 0;

		for (HashSet<State> ps : powerStates) {
			State ns = new State("q" + i++, isPartitionInitialState(ps), isPartitionFinalState(ps));
			newStates.add(ns);
			mapping.put(ps, ns);
		}

		HashSet<Transition> newDelta = new HashSet<Transition>();

		HashSet<State> fromPartition = null;
		HashSet<State> toPartition = null;


		for (Transition t : this.getDelta()) {
			for (HashSet<State> ps : powerStates) {
				if (ps.contains(t.getFrom()))
					fromPartition = ps;
				if (ps.contains(t.getTo()))
					toPartition = ps;			
			}

			newDelta.add(new Transition(mapping.get(fromPartition), mapping.get(toPartition), t.getInput()));

		}
		return new Automaton(newDelta, newStates);
	}

	/**
	 * Returns the initial state.
	 */
	@Deprecated
	public State getInitialState() {
		for (State s : this.getStates())
			if (s.isInitialState())
				return s;

		return null;
	}

	public HashSet<State> getInitialStates() {
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
		for (State s : this.getStates())
			if (s.getState().equals(initialState.getState()))
				s.setInitialState(true);
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

		if (isSingleString())
			return Automaton.makeAutomaton(getSingleString());

		HashSet<State> newStates = new HashSet<State>();
		HashSet<Transition> newDelta = new HashSet<Transition>();
		HashMap<String, State> nameToStates = new HashMap<String, State>();

		for (State s: this.states) {
			State newState = new State(s.getState(), s.isInitialState(), s.isFinalState());
			newStates.add(newState);
			nameToStates.put(newState.getState(), newState);
		}

		for (Transition t : this.delta)
			newDelta.add(new Transition(nameToStates.get(t.getFrom().getState()), nameToStates.get(t.getTo().getState()), t.getInput()));

		Automaton result = new Automaton();

		//		result.setInitialState(getInitialState());
		result.setDelta(newDelta);
		result.setStates(newStates);

		result.setAdjacencyListOutgoing((HashMap<State, HashSet<Transition>>) getAdjacencyListOutgoing().clone());
		return result;
	}

	public boolean approxEquals(Object other) {
		if (other instanceof Automaton) 
			return (this.getDelta().size() == ((Automaton) other).getDelta().size() && this.getStates().size() == ((Automaton) other).getStates().size());

		return false;
	}

	public HashSet<String> getLanguage(){
		if (isSingleString()) {
			HashSet<String> s = new HashSet<String>();
			s.add(getSingleString());
			return s;
		}

		return this.extractStrings(new HashSet<String>(), "", this.getInitialState(), null);

	}

	/**
	 * Recursive function to extract all the strings recognized by a not cyclic automaton
	 * @param set Hashset of strings
	 * @param partialString
	 * @param currentState
	 * @param prevT
	 * @return
	 */
	private HashSet<String> extractStrings(HashSet<String> set, String partialString, State currentState, Transition prevT){
		if(prevT != null){
			partialString += prevT.getInput();
			if(currentState.isFinalState()) {
				set.add(partialString);
			}
		}else if(currentState.isInitialState() && currentState.isFinalState()){
			set.add("");
		}

		for(Transition t : this.getOutgoingTransitionsFrom(currentState)){
			extractStrings(set, new String(partialString), t.getTo(), t);
		}

		return set;
	}

	/**
	 * Equal operator between automata.
	 */
	@Override
	public boolean equals(Object other) {

		if (other instanceof Automaton) {
			if (isSingleString() && ((Automaton) other).isSingleString())
				return getSingleString().equals(((Automaton) other).getSingleString());

			Automaton a = isSingleString() ? Automaton.makeRealAutomaton(getSingleString()) : clone();
			Automaton b =  ((Automaton) other).isSingleString() ? Automaton.makeRealAutomaton(((Automaton) other).getSingleString()) : ((Automaton) other).clone();

			a.minimize();
			b.minimize();
			
			if (a.hasCycle() && !b.hasCycle() || !a.hasCycle() && b.hasCycle())
				return false;
			
			if (!a.hasCycle() && !b.hasCycle()) {
				HashSet<String> aL = a.getLanguage();
				HashSet<String> bL = b.getLanguage();

				return aL.equals(bL);
			}
		
			
			
			Automaton first = Automaton.intersection(a, Automaton.complement(b));

			first.removeUnreachableStates();
			if (!Automaton.isEmptyLanguageAccepted(first)) 
				return false;

			Automaton second = Automaton.intersection(Automaton.complement(a), b);

			second.removeUnreachableStates();
			if (!Automaton.isEmptyLanguageAccepted(second)) 
				return false;

			return true;
		}

		return false;
	}

	@Override
	public int hashCode() {
		return isSingleValue ? getSingleString().hashCode() : getStates().size() + getDelta().size();
	}

	public int maxLengthString() {
		int max = Integer.MIN_VALUE;

		for (Vector<State> v : this.pahtsFrom(this.getInitialState(), new Vector<State>()))
			if (v.size() > max)
				max = v.size();

		return max; //- 2; // removes ''
	}


	/**
	 * Checks if this automaton contains a cycle.
	 * 
	 * @return true if this automaton contains a cycle, false otherwise.
	 */
	public boolean hasCycle() {

		Set<State> whiteSet = new HashSet<>();
		Set<State> graySet = new HashSet<>();
		Set<State> blackSet = new HashSet<>();

		if (isSingleString())
			return false;

		for (State vertex : getStates())
			whiteSet.add(vertex);

		while (whiteSet.size() > 0) {
			State current = whiteSet.iterator().next();
			if(dfs(current, whiteSet, graySet, blackSet)) {
				return true;
			}
		}
		return false;

	}

	public static boolean isJS(String js) {
		InputStream r = new ByteArrayInputStream(js.getBytes(StandardCharsets.UTF_8));
		DImpMinus grammar = new DImpMinus(r);

		//		if (js.equals("typeof("))
		//			return true;

		try {
			grammar.DImp();	
		} catch (TokenMgrError e1) {
			try {
				CompilerEnvirons env = new CompilerEnvirons();
				env.setRecoverFromErrors(true);
				IRFactory factory = new IRFactory(env);
				factory.parse(js, null, 0);

			} catch (Exception e) {

				if (e.getMessage().equals("invalid return"))
					return true;
				return false;
			}
			return true;
		} catch (ParseException e) {
			try {
				CompilerEnvirons env = new CompilerEnvirons();
				env.setRecoverFromErrors(true);
				IRFactory factory = new IRFactory(env);
				factory.parse(js, null, 0);

			} catch (Exception e2) {
				if (e2.getMessage().equals("invalid return"))
					return true;
				return false;
			}
			return true;
		}

		return true;
	}

	public static boolean isJSExecutable(String js) {


		try {
			CompilerEnvirons env = new CompilerEnvirons();
			env.setRecoverFromErrors(true);
			IRFactory factory = new IRFactory(env);
			factory.parse(js, null, 0);

		} catch (Exception e2) {
			if (e2.getMessage().equals("invalid return"))
				return true;
			return false;
		}
		return true;
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

	public static Automaton leftQuotient(Automaton L1, Automaton L2) {


		Automaton result = L1.clone();
		Automaton L1copy = L1.clone();

		// Remove the initial state
		for (State q : result.getStates())
			if (q.isInitialState())
				q.setInitialState(false);


		// Remove the initial state
		for (State q : L1copy.getStates())
			if (q.isFinalState())
				q.setFinalState(false);

		for (State q: L1copy.getStates()) {
			q.setFinalState(true);

			Automaton copy = L1copy.clone();
			copy.minimize();

			if (!Automaton.isEmptyLanguageAccepted(Automaton.intersection(copy, L2))) {
				for (State rS: result.getStates())
					if (rS.getState().equals(q.getState()))
						rS.setInitialState(true);
			}

			q.setFinalState(false);
		}

		result.minimize();

		return result;
	}

	public static Automaton rightQuotient(Automaton L1, Automaton L2) {
		Automaton result = L1.clone();
		Automaton L1copy = L1.clone();


		// Remove the initial state
		for (State q : result.getStates())
			if (q.isFinalState())
				q.setFinalState(false);


		// Remove the initial state
		for (State q : L1copy.getStates())
			if (q.isInitialState())
				q.setInitialState(false);

		for (State q: L1copy.getStates()) {
			q.setInitialState(true);

			Automaton copy = L1copy.clone();
			copy.minimize();


			if (!Automaton.isEmptyLanguageAccepted(Automaton.intersection(copy, L2))) {
				for (State rS: result.getStates())
					if (rS.getState().equals(q.getState()))
						rS.setFinalState(true);
			}

			q.setInitialState(false);
		}

		result.minimize();
		return result;
	}

	public static Automaton prefix(Automaton automaton) {
		Automaton result = automaton.isSingleString() ? Automaton.makeRealAutomaton(automaton.getSingleString()) : automaton.clone();

		for (State s : result.getStates())
			s.setFinalState(true);

		result.minimize();

		return result;
	}

	public static Automaton suffix(Automaton automaton) {
		Automaton result = automaton.isSingleString() ? Automaton.makeRealAutomaton(automaton.getSingleString()) : automaton.clone();

		for (State s : result.getStates())
			s.setInitialState(true);

		result.minimize();

		return result;
	}

	public static Automaton prefixAtMost(long i, Automaton automaton) {
		return Automaton.intersection(Automaton.prefix(automaton), Automaton.exactLengthAutomaton(i));
	}

	public static Automaton suffixAtMost(long i, Automaton automaton) {
		return Automaton.intersection(Automaton.suffix(automaton), Automaton.exactLengthAutomaton(i));
	}

	public static Automaton su(Automaton a, long n){

		int i = 0;
		a = Automaton.explodeAutomaton(a);
		State currentState = a.getInitialState();
		Automaton result = Automaton.makeEmptyLanguage();
		Automaton partial = Automaton.deleteCycle((Automaton)a.clone());
		//Automaton partial = a.clone();
		HashSet<Transition> delta = (HashSet<Transition>)partial.getDelta().clone();

		while(i!=n){
			partial = Automaton.explodeAutomaton(partial);
			delta = (HashSet<Transition>)partial.getDelta().clone();

			for(Transition removeT: partial.getOutgoingTransitionsFrom(currentState)){
				HashSet<Transition> to = partial.getOutgoingTransitionsFrom(removeT.getTo());
				if(to.size() == 0 || removeT.getTo().isFinalState()){
					result = Automaton.makeEmptyString();
				}

				for(Transition addT: to){
					delta.add(new Transition(currentState, addT.getTo(), addT.getInput()));
				}

				delta.remove(removeT);
				partial = new Automaton(delta, partial.getStates());
			}

			i++;
		}

		result = Automaton.union(result, new Automaton(delta, partial.getStates()));
		result.minimize();
		return result;
	}

	public static Automaton explodeAutomaton(Automaton a){
		HashMap<State, String> selfT = new HashMap<>();

		for(Transition t : a.getDelta()){
			if(t.getTo() == t.getFrom()){
				selfT.put(t.getFrom(), t.getInput());
			}
		}

		if(selfT.size() > 0){
			HashMap<State, State> doubleState = new HashMap<>();
			for (State s : selfT.keySet()){
				doubleState.put(s, new State(s.getState() + "b", s.isInitialState(), s.isFinalState()));
			}

			HashSet<Transition> delta = (HashSet<Transition>) a.getDelta().clone();

			for(State s: doubleState.keySet()){
				//tutte le transizioni allo stato vengono dirottate sul nuovo stato doppione
				//a parte l'autoanello
				for (Transition t: a.getIncomingTransitionsTo(s)){
					if (!t.getFrom().equals(s)) {
						delta.add(new Transition(t.getFrom(), doubleState.get(s), t.getInput()));
						delta.remove(t);
					}
				}
				//lo stato doppione ha tutte le transizioni in uscita dello stato originale
				//a parte l'autoanello
				for(Transition t: a.getOutgoingTransitionsFrom(s)) {
					if (!t.getTo().equals(s)) {
						delta.add(new Transition(doubleState.get(s), t.getTo(), t.getInput()));
					}
				}

				delta.add(new Transition(doubleState.get(s), s, selfT.get(s)));
				delta.add(new Transition(s, s, selfT.get(s)));
			}

			HashSet<State> states = (HashSet<State>)a.getStates().clone();
			for (State s: doubleState.values()){
				states.add(s);
			}

			return new Automaton(delta, states);
		}

		return a;
	}

	/**
	 * The method deletes the cycles that run through the initial state
	 * @param a
	 * @return
	 */
	public static Automaton deleteCycle(Automaton a){
		boolean cycleOnStart = false;
		for(Transition t: a.getDelta()){
			if(t.getTo().equals(a.getInitialState())){
				cycleOnStart = true;
			}
		}

		if(!cycleOnStart){
			return a;
		}

		HashSet<Transition> delta = new HashSet<>();
		State q0Clone = new State("q0b", false, a.getInitialState().isFinalState());
		for(Transition t: a.getDelta()){
			if(!t.getTo().equals(a.getInitialState())) {
				delta.add(t);
			}else{
				delta.add(new Transition(t.getFrom(), q0Clone, t.getInput()));
			}

			if(t.getFrom().equals(a.getInitialState())){
				delta.add(new Transition(q0Clone, t.getTo(), t.getInput()));
			}
		}

		HashSet<State> states = (HashSet<State>) a.getStates().clone();
		states.add(q0Clone);
		return new Automaton(delta, states);

	}

	public static Automaton suffixesAt(long i, Automaton automaton) {
		Automaton result = Automaton.leftQuotient(automaton, Automaton.prefixAtMost(i, automaton));	
		return Automaton.isEmptyLanguageAccepted(result) ? Automaton.makeEmptyString() : result;	
		//return Automaton.su(automaton, i);
	}

	public static Automaton singleParameterSubstring(Automaton a, long i) {
		int initIndex = (int) (i < 0 ? 0 : i);

		if (a.isSingleString()) {

			if (i >= a.getSingleString().length())
				return Automaton.makeEmptyString();
			else
				return Automaton.makeAutomaton(a.getSingleString().substring(initIndex));
		}

		return Automaton.suffixesAt(initIndex, a);
	}

	public static Automaton singleSubstring(Automaton a, long i) {	

		long initPoint = i < 0 ? 0 : i ;

		if (a.isSingleString()) {
			String s = a.getSingleString();

			if (initPoint >= s.length())
				if (initPoint >= s.length())
					return Automaton.makeEmptyString();

			return Automaton.makeAutomaton(s.substring((int) initPoint));

		}


		Automaton left = Automaton.suffixesAt(initPoint, a);	

		return Automaton.rightQuotient(left,  Automaton.makeRealAutomaton(""));
	}


	public static int indexOf(Automaton a, Automaton b) {

		Automaton aut = a.isSingleString() ? Automaton.makeRealAutomaton(a.getSingleString()) : a;
		Automaton search = b.isSingleString() ? Automaton.makeRealAutomaton(b.getSingleString()) : b;

		if (Automaton.isEmptyLanguageAccepted(Automaton.intersection(search, Automaton.factors(aut))))
			return -1;
		else if (aut.hasCycle() || search.hasCycle())
			return -2;


		Automaton build = aut.clone();

		int indexOf = -2;

		for (State s : build.getStates()) {
			if (s.isInitialState())
				s.setInitialState(false);
			s.setFinalState(true);
		}

		for (State q : build.getStates()) {
			q.setInitialState(true);

			if (!Automaton.isEmptyLanguageAccepted(Automaton.intersection(build, search))) {
				int index =  aut.minimumDijkstra(q).size() - 1;
				if (index != indexOf && indexOf != -2)
					return -2;
				else
					indexOf = index;
			}

			q.setInitialState(false);	
		}

		return indexOf;
	}

	public static Automaton substring(Automaton a, long i, long j) {	

		long initPoint = Long.min(i, j) < 0 ? 0 : Long.min(i, j);
		long endPoint = Long.max(i, j) < 0 ? 0 : Long.max(i, j);

		if (a.isSingleString()) {
			String s = a.getSingleString();

			if (endPoint >= s.length())
				if (initPoint >= s.length())
					return Automaton.makeEmptyString();
				else
					return Automaton.makeAutomaton(s.substring((int) initPoint));
			else
				return Automaton.makeAutomaton(a.getSingleString().substring((int) initPoint, (int) endPoint));
		}


		Automaton left = Automaton.suffixesAt(initPoint, a);	

		Automaton noProperSubs = Automaton.intersection(left, Automaton.atMostLengthAutomaton(endPoint-initPoint));
		return Automaton.union(Automaton.intersection(Automaton.rightQuotient(left,  Automaton.suffixesAt(endPoint, a)), Automaton.exactLengthAutomaton(endPoint-initPoint)), noProperSubs);	
	}

	public static Automaton substringWithUnknownEndPoint(Automaton a, long i, long j) {	 
		return Automaton.rightQuotient(Automaton.suffixesAt(i,a),  Automaton.suffix(Automaton.suffixesAt(j, a)));
	}

	public static Automaton factorsStartingAt(Automaton a, long i) {
		Automaton left = Automaton.leftQuotient(a, Automaton.prefixAtMost(i, a));	
		return Automaton.suffix(Automaton.prefix(left));
	}

	public static Automaton exactLengthAutomaton(long max) {
		HashSet<State> states = new HashSet<>();
		HashSet<Transition> delta = new HashSet<>();

		State q0 = new State("q0", true, false);
		states.add(q0);

		State prev = q0;

		for (int i = 0; i < max; ++i) {
			State next = new State("q" + i + 1, false, false);
			states.add(next);

			for (char alphabet = '!'; alphabet <= '~'; ++alphabet) 
				delta.add(new Transition(prev, next, String.valueOf(alphabet)));

			delta.add(new Transition(prev, next, " "));

			prev = next;
		}

		prev.setFinalState(true);

		return new Automaton(delta, states);
	}

	public static Automaton atMostLengthAutomaton(long max) {
		HashSet<State> states = new HashSet<>();
		HashSet<Transition> delta = new HashSet<>();

		State q0 = new State("q0", true, true);
		states.add(q0);

		State prev = q0;

		for (int i = 0; i < max; ++i) {
			State next = new State("q" + i + 1, false, true);
			states.add(next);

			for (char alphabet = '!'; alphabet <= '~'; ++alphabet) 
				delta.add(new Transition(prev, next, String.valueOf(alphabet)));
			delta.add(new Transition(prev, next, " "));

			prev = next;
		}

		prev.setFinalState(true);

		return new Automaton(delta, states);
	}

	public static Automaton charAt(Automaton a, long i) {
		return Automaton.substring(a, i, i + 1);
	}

	public static Automaton factors(Automaton a) {
		return Automaton.suffix(Automaton.prefix(a));
	}

	public LinkedList<State> minimumDijkstra(State target) {
		Set<State>  settledNodes = new HashSet<State>();
		Set<State> unSettledNodes = new HashSet<State>();
		Map<State, Integer> distance = new HashMap<State, Integer>();
		Map<State, State> predecessors = new HashMap<State, State>();

		distance.put(getInitialState(), 0);
		unSettledNodes.add(getInitialState());

		while (unSettledNodes.size() > 0) {
			State node = getMinimum(unSettledNodes, distance);
			settledNodes.add(node);
			unSettledNodes.remove(node);
			findMinimalDistances(node, distance, predecessors, unSettledNodes, settledNodes);
		}

		return getPath(target, predecessors);
	}

	public LinkedList<State> maximumDijkstra(State target) {
		Set<State>  settledNodes = new HashSet<State>();
		Set<State> unSettledNodes = new HashSet<State>();
		Map<State, Integer> distance = new HashMap<State, Integer>();
		Map<State, State> predecessors = new HashMap<State, State>();

		distance.put(getInitialState(), 0);
		unSettledNodes.add(getInitialState());

		while (unSettledNodes.size() > 0) {
			State node = getMaximum(unSettledNodes, distance);
			settledNodes.add(node);
			unSettledNodes.remove(node);
			findMaximalDistances(node, distance, predecessors, unSettledNodes, settledNodes);
		}

		return getPath(target, predecessors);
	}

	private void findMinimalDistances(State node, Map<State, Integer> distance, Map<State, State> predecessors, Set<State> unSettledNodes, Set<State> settledNodes) {
		List<State> adjacentNodes = getNeighbors(node, settledNodes);
		for (State target : adjacentNodes) {
			if (getShortestDistance(target, distance) > getShortestDistance(node, distance)
					+ getDistance(node, target)) {
				distance.put(target, getShortestDistance(node, distance)
						+ getDistance(node, target));
				predecessors.put(target, node);
				unSettledNodes.add(target);
			}
		}
	}

	private void findMaximalDistances(State node, Map<State, Integer> distance, Map<State, State> predecessors, Set<State> unSettledNodes, Set<State> settledNodes) {
		List<State> adjacentNodes = getNeighbors(node, settledNodes);
		for (State target : adjacentNodes) {
			if (getLongestDistance(target, distance) < getLongestDistance(node, distance)
					+ getDistance(node, target)) {
				distance.put(target, getShortestDistance(node, distance)
						+ getDistance(node, target));
				predecessors.put(target, node);
				unSettledNodes.add(target);
			}
		}
	}

	private int getDistance(State node, State target) {
		for (Transition edge : getDelta()) {
			if (edge.getFrom().equals(node)
					&& edge.getTo().equals(target)) {
				return 1;
			}
		}
		throw new RuntimeException("Should not happen");
	}

	private List<State> getNeighbors(State node, Set<State> settledNodes) {
		List<State> neighbors = new ArrayList<State>();
		for (Transition edge : getDelta()) {
			if (edge.getFrom().equals(node)
					/*&& !isSettled(edge.getTo(), settledNodes)*/) {
				neighbors.add(edge.getTo());
			}
		}
		return neighbors;
	}

	private State getMinimum(Set<State> vertexes, Map<State, Integer> distance) {
		State minimum = null;
		for (State vertex : vertexes) {
			if (minimum == null) {
				minimum = vertex;
			} else {
				if (getShortestDistance(vertex, distance) < getShortestDistance(minimum, distance)) {
					minimum = vertex;
				}
			}
		}
		return minimum;
	}

	private State getMaximum(Set<State> vertexes, Map<State, Integer> distance) {
		State maximum = null;
		for (State vertex : vertexes) {
			if (maximum == null) {
				maximum = vertex;
			} else {
				if (getShortestDistance(vertex, distance) > getLongestDistance(maximum, distance)) {
					maximum = vertex;
				}
			}
		}

		return maximum;
	}

	private boolean isSettled(State vertex, Set<State> settledNodes) {
		return settledNodes.contains(vertex);
	}

	private int getShortestDistance(State destination, Map<State, Integer> distance) {
		Integer d = distance.get(destination);
		if (d == null) {
			return Integer.MAX_VALUE;
		} else {
			return d;
		}
	}

	private int getLongestDistance(State destination, Map<State, Integer> distance) {
		Integer d = distance.get(destination);

		if (d == null) {
			return Integer.MIN_VALUE;
		} else {
			return d;
		}

	}

	public LinkedList<State> getPath(State target, Map<State, State> predecessors) {
		LinkedList<State> path = new LinkedList<State>();
		State step = target;

		// check if a path exists
		if (predecessors.get(step) == null) {
			path.add(target);
			return path;
		}

		path.add(step);
		while (predecessors.get(step) != null) {
			step = predecessors.get(step);
			path.add(step);
		}
		// Put it into the correct order
		Collections.reverse(path);
		return path;
	}

	/**
	 * Length algorithm in the constant integers abstract domain.
	 *
	 * @param a input automaton
	 * @return Returns -1 if the automaton has no constant lengths (i.e., top integer), the length otherwise.
	 */
	public static int length(final Automaton a) {

		Automaton aut = a.clone();
		if (aut.hasCycle()) {
			return -1;
		} else {

			int constantLength = -1;

			for (State f : aut.getFinalStates()) {
				int min = aut.minimumDijkstra(f).size() - 1;
				int max = aut.maximumDijkstra(f).size() - 1;

				if (min != max || (min == max && min != constantLength && constantLength != -1))
					return -1;
				else
					constantLength = min;
			}

			return constantLength;
		}
	}

	public static Automaton trimLeft(Automaton a){
		Automaton result = a.clone();

		boolean todoagain = true;

		while (todoagain) {

			for (Transition t : result.getOutgoingTransitionsFrom(result.getInitialState())){
				if (t.getInput().equals(" "))
					t.setInput("");
			}

			result.minimize();

			boolean b = false;
			for (Transition t : result.getOutgoingTransitionsFrom(result.getInitialState())){
				if (t.getInput().equals(" "))
					b = true;
			}

			if (b == false)
				break;
		}


		return result;
	}

	public static Automaton trimRight(Automaton a){

		Automaton result;

		if (a.isSingleString())
			result = Automaton.makeRealAutomaton(a.getSingleString());
		else 
			result = a.clone();



		boolean todoagain = true;

		while (todoagain) {

			for (State qf : result.getFinalStates()) {
				for (Transition t : result.getIncomingTransitionsTo(qf)){
					if (t.getInput().equals(" "))
						t.setInput("");
				}
			}

			result.minimize();

			boolean b = false;
			for (State qf : result.getFinalStates()) {

				for (Transition t : result.getIncomingTransitionsTo(qf)){
					if (t.getInput().equals(" "))
						b = true;
				}
			}

			if (b == false)
				break;
		}


		return result;
	}


	public static Automaton trim(Automaton a){
		return Automaton.trimRight(Automaton.trimLeft(a));
	}

	/**
	 * Creates a new FA that recognizes the upper case of all the strings of the automaton
	 * @return
	 */
	public static Automaton toLowerCase(Automaton a){

		if (a.isSingleString())
			a = Automaton.makeRealAutomaton(a.getSingleString());

		Automaton toLower = a.clone();

		for (Transition t : toLower.getDelta()){
			t.setInput(t.getInput().toLowerCase());
		}

		toLower.minimize();

		return toLower;
	}

	/**
	 * Creates a new FA that recognizes the upper case of all the strings of the automaton
	 * @return
	 */
	public static Automaton toUpperCase(Automaton a){

		if (a.isSingleString())
			a = Automaton.makeRealAutomaton(a.getSingleString());

		Automaton toUpper = a.clone();
		for (Transition t : toUpper.getDelta()){
			t.setInput(t.getInput().toUpperCase());
		}

		toUpper.minimize();

		return toUpper;
	}

	/**
	 * Method that, given two indexes, returns the automaton that recognizes all the substrings in
	 * those indexes. If any of them is negative then index = index + length.
	 * If the first index is grater then the second epsilon is returned
	 * @param start starting index, included. Negative value possible
	 * @param end ending index, not included. Negative value possible
	 * @return
	 */
	public static Automaton slice(Automaton a, long start, long end){

		if(start == end){
			return Automaton.makeEmptyString();
		}

		if(a.hasCycle() && (start < 0 || end < 0)) {
			return Automaton.makeTopLanguage();
		}

		if (start >= 0 && end >= 0 && start < end) {
			return Automaton.substring(a, start, end);
		}

		return a.cutter(start, end, a.getInitialState(), new HashSet<Transition>(), Automaton.makeEmptyLanguage());

	}

	/**
	 * Recursive auxiliary function that, for each possible path, returns the substring of the automaton
	 * in the given indexes.
	 * @param start starting index, negative value possible
	 * @param end ending index, negative value possible
	 * @param currentState state we are currently searching
	 * @param delta set of all the transitions
	 * @param result result automaton
	 * @return
	 */
	private Automaton cutter(long start, long end, State currentState, HashSet<Transition> delta, Automaton result){
		if(currentState.isFinalState()) {

			Automaton partial = Automaton.copy(this, currentState, delta);
			//printDetails(partial);

			long s = start;
			long e = end;

			int length = Automaton.length(partial);

			if (s < 0) {
				s = s + length;
				if (s < 0) {
					s = 0;
				}
			}

			if (e < 0) {
				e = e + length;
				if (e < 0) {
					e = 0;
				}
			}

			//if the start index is greater than end epsilon is returned
			if (s >= e) {
				result = Automaton.union(result, Automaton.makeEmptyString());
			} else {
				result = Automaton.union(result, Automaton.substring(partial, s, e));
			}
		}

		for(Transition t : this.getOutgoingTransitionsFrom(currentState)){
			HashSet<Transition> clone = (HashSet<Transition>)delta.clone();
			clone.add(t);
			result = Automaton.union(result, cutter(start, end, t.getTo(), clone, result));
		}

		return result;
	}

	/**
	 * Checks whether an automaton includes another one.
	 * @param other Automaton on which we set the search
	 * @param a Automaton
	 * @return true (1) if all the strings in automaton contains the strings in other,
	 *         false (0) if none of the strings cointain strings of other
	 *         topbool (-1) otherwise
	 */
	public static int includes(Automaton a, Automaton other){

		if (a.isSingleString())
			a = Automaton.makeRealAutomaton(a.getSingleString());

		if (other.isSingleString())
			other = Automaton.makeRealAutomaton(other.getSingleString());

		a.minimize();


		Automaton intersection = Automaton.intersection(Automaton.factors(a), other);
		if (Automaton.isEmptyLanguageAccepted(intersection))
			return 0;

		if (other.equals(Automaton.makeEmptyString())){
			return 1;
		}

		if(other.hasCycle() || a.hasCycle()){
			return -1;
		}

		return a.auxIncludes(other,new HashSet<Transition>(), a.getInitialState());
	}


	/**
	 * Checks whether an automaton includes another one.
	 * @param other FA on which we set the search
	 * @param i Interval on which we start the search
	 * @return true if all the strings in automaton contains the strings in other,
	 *         false if none of the strings contain strings of other
	 *         topbool otherwise
	 */
	public static int includes(Automaton a, Automaton other, long i){

		return Automaton.includes(Automaton.singleParameterSubstring(a, i),other);

	}

	/**
	 * Auxiliary function for includes() that checks whether automaton contains all the strings in
	 * Automaton other or just some of them. The method is recursive.
	 * @param other Automaton on which the search is set on
	 * @param delta set of transitions that lead to current state
	 * @param currentState state from which we start to explore the automaton
	 * @return
	 */
	private int auxIncludes(Automaton other, HashSet<Transition> delta, State currentState){
		int outgoingTrans = this.getOutgoingTransitionsFrom(currentState).size();

		//we enter in this body only if a state has more than one outgoing transaction and
		//it is not the initial state of the automaton
		//or if we arrived at a final state
		if(( outgoingTrans > 1 && !(currentState.equals(this.getInitialState()))) || currentState.isFinalState()) {
			Automaton factors = Automaton.factors(new Automaton(delta, this.getStates()));
			//check if the intersection between the factorization on the automaton visited so far
			//and the other automaton are equals, if they are it means the strings are contained
			boolean equalsOther = Automaton.intersection(factors, other).equals(other);


			if (equalsOther) {
				return 1;
			}

			if (currentState.isFinalState())
				return -1;
		}

		//for each path we check if the string is contained or not
		for(Transition t : this.getOutgoingTransitionsFrom(currentState)){
			HashSet<Transition> clone = (HashSet<Transition>)delta.clone();
			clone.add(t);
			if(auxIncludes(other, clone, t.getTo()) == -1){
				return -1;
			}
		}
		return 1;
	}


	/**
	 * startsWith with two parameters.
	 * @param other
	 * @param i
	 * @return
	 */
	public static int startsWith(Automaton a, Automaton other, long i){
		return Automaton.startsWith(Automaton.singleParameterSubstring(a, i), other);
	}

	/**
	 * starsWith with one parameter.
	 * Determines if every string of automaton starts with all the strings in Automaton  other
	 * @param other Automaton  containing the strings to search for
	 * @return Bool True if automaton starts with other
	 *         Bool False if none of the strings in automaton starts with a string in other
	 *         Bool TopBool otherwise.
	 */
	public static int startsWith(Automaton a, Automaton  other){

		if (a.isSingleString() && other.isSingleString())
			return a.getSingleString().startsWith(other.getSingleString()) ? 1 : 0;

		if (a.isSingleString())
			a = Automaton.makeRealAutomaton(a.getSingleString());

		if (other.isSingleString())
			other = Automaton.makeRealAutomaton(other.getSingleString());

		other.minimize();
		a.minimize();
		if (other.equals(Automaton.makeEmptyString())) {
			return 1;
		}

		Automaton intersection = Automaton.intersection(Automaton.prefix(a), other);

		if(Automaton.isEmptyLanguageAccepted(intersection)) {
			return 0;
		}

		if(other.hasCycle() || a.hasCycle()) {
			return -1;
		}



		if(other.hasOnlyOnePath()) {

			Automaton  C = other.extractLongestString();
			Automaton  B = Automaton.substring(a, 0, Automaton.length(C));
			B.minimize();

			if (B.equals(C)) {
				return 1;
			}

		}

		return -1;
	}

	/**
	 * Checks if the automaton has only one path meaning that the
	 * recongnized strings all have common prefixes.
	 * A string is basically the concat of the shortest string recognized with
	 * other characters.
	 * The automaton must be minimized.
	 * @return true if it is only one path, false otherwise
	 */
	private boolean hasOnlyOnePath(){
		HashSet<State> transFrom = new HashSet<>();
		HashSet<State> transTo = new HashSet<>();
		State from = null;
		State to = null;

		/**
		 * The general idea is that if the automaton has only one path
		 * then the set of states contain a node at most once as starting point of
		 * a transition and at most once as arrival node.
		 */
		for (Transition t: this.getDelta()) {

			from = t.getFrom();
			if(transFrom.contains(from)){
				return false;
			}

			transFrom.add(from);

			to = t.getTo();
			if(transTo.contains(to)){
				return false;
			}

			transTo.add(to);

		}

		return true;
	}

	/**
	 * Creates a new Automaton  recognizing only the longest string of the Automaton 
	 * Works only if the Automaton  has only one path
	 * @return Automaton  for the longest string found
	 */
	private Automaton  extractLongestString(){
		State lastFinalState = null;

		for (State finalState : this.getFinalStates()){
			HashSet<Transition> outgoingTransaction = this.getOutgoingTransitionsFrom(finalState);
			if(outgoingTransaction.size() == 0){
				lastFinalState = finalState;
			}
		}

		State nextState = this.getInitialState();
		String s = "";
		while(!nextState.equals(lastFinalState)){
			for(Transition t: this.getDelta()){
				if(t.getFrom().equals(nextState)){
					nextState = t.getTo();
					s += t.getInput();
				}
			}
		}

		return Automaton.makeRealAutomaton(s);
	}

	public static Automaton repeat(Automaton a, long i){

		if(i < 0){
			//todo should return OUT OF RANGE EXCEPTION
			return Automaton.makeEmptyLanguage();
		}

		if(i == 0){
			return Automaton.makeEmptyString();
		}

		if(a.hasCycle() || a.equals(Automaton.makeEmptyString()) || a.equals(Automaton.makeEmptyLanguage())){
			return a;
		}

		return a.auxRepeat(i, a.getInitialState(), new HashSet<Transition>(), makeEmptyLanguage());
	}

	public Automaton auxRepeat(long i, State currentState, HashSet<Transition> delta, Automaton result){

		if(currentState.isFinalState()){

			Automaton temp = copy(this, currentState, delta);
			Automaton tempResult = temp.clone();

			if(i == Integer.MAX_VALUE){
				tempResult = Automaton.minus(Automaton.makeCyclic(temp), Automaton.makeEmptyString());
			}else {
				for (int k = 1; k < i; k++) {
					tempResult = Automaton.concat(tempResult, temp);
				}
			}

			result = Automaton.union(result, tempResult);

		}

		for(Transition t: this.getOutgoingTransitionsFrom(currentState)){
			HashSet<Transition> clone = (HashSet<Transition>)delta.clone();
			clone.add(t);
			result = Automaton.union(auxRepeat(i, t.getTo(), clone, result), result);
		}

		return result;

	}

	/**
	 * Method that creates a cyclic automaton from a given one, the automaton must recognize only one string
	 * (it should be a function for automaton and not for FA)
	 * @return
	 */
	public static Automaton makeCyclic(Automaton a){

		HashSet<State> newStates = new HashSet<>();
		HashSet<Transition> newDelta = new HashSet<>();
		Transition finalT = null;

		for(State s: a.getStates()){
			newStates.add(s);
		}

		for(State s: a.getFinalStates()){
			newStates.remove(s);
		}

		a.getInitialState().setFinalState(true);

		for(Transition t: a.getDelta()){
			if(!t.getTo().isFinalState()){
				newDelta.add(t);
			}else{
				finalT = t;
			}
		}

		newDelta.add(new Transition(finalT.getFrom(), a.getInitialState(), finalT.getInput()));
		return new Automaton(newDelta, newStates);
	}

	/**
	 * Method that replaces the automaton searchFor in the current automaton with replaceWith
	 * @param searchFor FA to search for in the current FA
	 * @param replaceWith FA that replaces the first occurrence of searchFor
	 * @return
	 */
	public static Automaton replace(Automaton a, Automaton searchFor, Automaton replaceWith){
		//if the current automaton or the one we are searching have cycles the top language is returned
		if(a.hasCycle() || searchFor.hasCycle()) {
			return Automaton.makeTopLanguage();
		}

		//if the intersection between the factorization of this and searchFor is empty then the current Automaton is returned
		Automaton intersection = Automaton.intersection(Automaton.factors(a), searchFor);
		if (Automaton.isEmptyLanguageAccepted(intersection)){
			return a;
		}

		//the intersection indicates how many strings of searchFor I find in the current Automaton, if all the strings are
		//present, we just return the Automaton with all the substitutions, if there is at least a string not found then we
		//return the substitution plus the current Automaton
		if(intersection.equals(searchFor)){
			return a.DFSReplace(intersection, replaceWith, a.getInitialState(), new HashSet<Transition>(),
					Automaton.makeEmptyLanguage());
		}

		return Automaton.union(a, a.DFSReplace(intersection, replaceWith, a.getInitialState(), new HashSet<Transition>(),
				Automaton.makeEmptyLanguage()));
	}


	/**
	 * For each path in the FA, finds which strings in searchFor are present to make the replacement
	 * @param searchFor FA to search for
	 * @param replaceWith FA with whom replace the first occurrence of searchFor
	 * @param currentState state on which we arrived in the search
	 * @param delta set of transitions, includes all of the transitions collected from the initial state to currentState
	 *              in a single path
	 * @param result
	 * @return
	 */
	private Automaton DFSReplace(Automaton searchFor, Automaton replaceWith, State currentState, HashSet<Transition> delta, Automaton result){

		if(currentState.isFinalState()) {
			//searchIn is the automaton that recognizes a single recognized string in the current Automaton
			Automaton searchIn = new Automaton(delta, (HashSet<State>) getStates().clone());
			searchIn = searchIn.clone();
			Automaton intersection = Automaton.intersection(Automaton.factors(searchIn), searchFor);

			//if the intersection is empty and there are no other transitions from the current state,
			//we return searchIn
			if (Automaton.isEmptyLanguageAccepted(intersection) && getOutgoingTransitionsFrom(currentState).size() == 0) {
				return searchIn;

			}else if(!Automaton.isEmptyLanguageAccepted(intersection)){
				Automaton mr = searchIn.makeReplacement(intersection, replaceWith, this.getInitialState(), new HashSet<Transition>(), Automaton.makeEmptyLanguage());
				result = Automaton.union(result, mr);

				//if the intersection is not empty but is not equal to searchFor it means that there is at least a
				//string of searchFor that is not included in this path(i.e. the string on which we are running the search)
				//so, besides the string with the replacement we also return the original string
				if(!intersection.equals(searchFor)) {
					result = Automaton.union(result, searchIn);
				}
			}
		}

		for(Transition t: this.getOutgoingTransitionsFrom(currentState)){
			HashSet<Transition> clone = (HashSet<Transition>)delta.clone();
			clone.add(t);
			result = Automaton.union(result, DFSReplace(searchFor.clone(), replaceWith, t.getTo(), clone, result));
		}

		return result;
	}

	/**
	 * makes the replacement and returns a result automaton. The general idea is to start from the empty automaton and then
	 * add the transitions of the this.getAutomaton() one by one until we have build the automaton that recognizes each string
	 * in searchFor. We thus know that the build automaton finishes with the string we are looking for.
	 * Each time we find a string of searchFor we remove it from searchFor and find the other strings until we find them all.
	 * @param searchFor Automaton of the strings we are searching for. All the strings have at least one occurrence in this.getAutomaton()
	 * @param replaceWith Automaton with whom we replace the occurrence of searchFor
	 * @param currentState current state in the search
	 * @param delta set of transitions from the initial state to currentState
	 * @param resultOfRep partial result of the replacement
	 * @return result of the replacement
	 */
	private Automaton makeReplacement(Automaton searchFor, Automaton replaceWith, State currentState, HashSet<Transition> delta, Automaton resultOfRep){

		Automaton searchIn = copy(this, currentState, delta);

		Automaton intersection = Automaton.intersection(Automaton.factors(searchIn), searchFor);

		if (!intersection.equals(Automaton.makeEmptyLanguage())) {
			Automaton temp = null;

			//if the intersection is epsilon, it means that searchFor contains epsilon therefore replaceWith will be
			//added at the start of the string
			if(intersection.equals(Automaton.makeEmptyString())){
				temp = this.auxMakeReplacement(intersection, replaceWith, delta);
			}else {
				temp = searchIn.auxMakeReplacement(intersection, replaceWith, delta);
			}

			//we remove the strings we have already found
			searchFor = Automaton.minus(searchFor, intersection);

			//if the intersection is not epsilon we need to add to the first part of the result, which is resultOfRep
			//containing the automaton till the replacement, the rest of the original automaton

			if(!intersection.equals(Automaton.makeEmptyString())) {
				Automaton remainingAutomaton = Automaton.singleParameterSubstring(this, searchIn.maxLengthString());
				temp = Automaton.concat(temp, remainingAutomaton);
			}

			resultOfRep = Automaton.union(resultOfRep, temp);

			if(searchFor.equals(Automaton.makeEmptyLanguage())){
				return resultOfRep;
			}
		}

		for(Transition t: this.getOutgoingTransitionsFrom(currentState)){
			delta.add(t);
			return makeReplacement(searchFor, replaceWith, t.getTo(), delta, resultOfRep);
		}

		return resultOfRep;

	}

	/**
	 * Method that finds the initial point of the string to search for and makes the replacement.
	 * Uses the methods length and substring of FA
	 * @param searchFor
	 * @param replaceWith
	 * @param delta
	 * @return
	 */
	private Automaton auxMakeReplacement(Automaton searchFor, Automaton replaceWith, HashSet<Transition> delta){

		if(searchFor.equals(Automaton.makeEmptyString())){
			return Automaton.concat(replaceWith, this);
		}

		this.minimize();
		//int length = Automaton.length(this);
		//int start = Automaton.length(searchFor);
		int length = this.getDelta().size();
		int start = searchFor.getDelta().size();

		Automaton prefix = Automaton.substring(this,0, length - start);

		return Automaton.concat(prefix, replaceWith);
	}

	public static void printDetails(Automaton a){
		System.out.println("stati: ");
		for (State s: a.states){
			System.out.println(s.toString() + ",iniziale: " + s.isInitialState() + ", finale: " + s.isFinalState());
		}
		System.out.println("transizioni :");
		for (Transition t: a.getDelta()){
			System.out.println(t);
		}
	}

	private static Automaton copy(Automaton a, State currentState, HashSet<Transition> delta){

		HashMap<State, State> mapping = new HashMap<>();
		HashSet<Transition> newDelta = new HashSet<>();
		HashSet<State> newStates = new HashSet<>();

		for(State s: a.getStates()){
			State newState = (State)s.clone();
			newStates.add(newState);
			mapping.put(s, newState);
		}
		if(!currentState.isInitialState()){
			mapping.get(currentState).setFinalState(true);
		}

		for(State s: a.getFinalStates()){
			if(!s.equals(currentState)){
				mapping.get(s).setFinalState(false);
			}
		}

		for (Transition t: delta){
			newDelta.add(new Transition(mapping.get(t.getFrom()), mapping.get(t.getTo()), t.getInput()));
		}

		Automaton result = new Automaton(newDelta, newStates);
		result.minimize();

		return result;
	}

	public static int endsWith(Automaton a, Automaton other){
		return Automaton.startsWith(Automaton.reverse(a), Automaton.reverse(other));

	}

	public static Automaton chars(Automaton a) {

		if (a.isSingleString()) 
			a = Automaton.makeRealAutomaton(a.getSingleString());

		a.minimize();

		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State qf = new State("qf", false, true);

		states.add(q0);
		states.add(qf);

		for (Transition t : a.getDelta())
			delta.add(new Transition(q0, qf, t.getInput()));

		Automaton aut = new Automaton(delta, states);
		aut.minimize();
		return aut;
	}

	public static Automaton makeArrayIndexAutomaton() {

		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();


		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, true);

		states.add(q0);
		states.add(q1);

		for (char c = '0'; c < '9'; c++) { 
			delta.add(new Transition(q0, q1, String.valueOf(c)));
			delta.add(new Transition(q1, q1, String.valueOf(c)));
		}

		return new Automaton(delta, states);
	}

}