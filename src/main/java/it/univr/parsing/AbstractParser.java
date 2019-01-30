package it.univr.parsing;

import java.util.HashMap;
import org.apache.commons.lang3.tuple.*;

import java.util.HashSet;
import java.util.Map;

import it.univr.fsm.machine.Automaton;
import it.univr.fsm.machine.State;
import it.univr.fsm.machine.Transition;

public class AbstractParser {


	public Automaton reduceProgram(Automaton a) {
		
		// Normalize automaton cycles
		normalizeAutomaton(a);
		
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		HashSet<State> W = new HashSet<State>();
		W.add(a.getInitialState());

		do {
			State q = null;

			for (State w : W) {
				q = w;
				break;
			}

			W.remove(q);

			HashMap<String, State> stmts = reduceStatement(a, q);

			for (Map.Entry<String, State> t : stmts.entrySet()) {

				State from = q.clone();
				State to = t.getValue().clone();

				states.add(from);
				states.add(to);

				delta.add(new Transition(from, to, t.getKey()));
				W.add(t.getValue());
			}


		} while (!W.isEmpty());

		return new Automaton(delta, states);
	}


	public HashMap<String, State> reduceStatement(Automaton a, State q) {
		return reduceAssignment(a, q);
	}

	public HashMap<String, State> reduceExpression(Automaton a, State q) {
		HashMap<String, State> exps = new HashMap<String, State>();
		HashMap<String, State> pexps = new HashMap<String, State>();

		for (Transition t : a.getOutgoingTransitionsFrom(q)) {

			//
			//			if (isIdLetter(t.getInput())) {
			//				pexps = reduceValue(a, t.getFrom());
			//
			//				for (Map.Entry<String, State> entry : pexps.entrySet()) 
			//					exps.put(entry.getKey(), entry.getValue());	
			//				
			//			} else {

			switch(t.getInput()) {
			case "+":  
			case "*":  
			case "-":  
			case "/":  

				pexps = reduceExpression(a, t.getTo());

				for (Map.Entry<String, State> entry : pexps.entrySet()) 
					exps.put(t.getInput() + entry.getKey(), entry.getValue());					

				break;


			default:
				pexps = reduceValue(a, t.getFrom());

				for (Map.Entry<String, State> entry : pexps.entrySet()) 
					exps.put(entry.getKey(), entry.getValue());	
			}
		}
		//		}

		return exps;
	}

	public HashMap<String, State> reduceValue(Automaton a, State q) {
		HashMap<String, State> pvals = new HashMap<String, State>();

		for (Transition t : a.getOutgoingTransitionsFrom(q)) {
			if (isCipher(t.getInput()))
				pvals.putAll(reduceInteger(a,q));
			else if (isIdLetter(t.getInput()))
				pvals.putAll(reduceIdExpression(a,q));

		}

		return pvals;
	}

	public HashMap<String, State> reduceAssignment(Automaton a, State q) {
		HashMap<String, State> ids = reduceIds(a, q);

		HashMap<String, State> asg = new HashMap<String, State>();

		for (Map.Entry<String, State> t : ids.entrySet()) {
			HashMap<String, State> values = reduceExpression(a, t.getValue());

			for (Map.Entry<String, State> p : values.entrySet()) {
				asg.put(t.getKey() + " = " + p.getKey() + ";", p.getValue());
			}

		}

		return asg;
	}

	public HashMap<String, State> reduceInteger(Automaton a, State q) {
		HashMap<String, State> ids = new HashMap<String, State>();

		for (Transition t : a.getOutgoingTransitionsFrom(q)) {
			if (isCipher(t.getInput())) {
				HashMap<String, State> pids = reduceInteger(a, t.getTo());

				for (Map.Entry<String, State> pid : pids.entrySet()) {
					ids.put(t.getInput() + pid.getKey(), pid.getValue());
				}
			} else if (t.getInput().equals(";")){
				ids.put("", t.getTo());
			} else {
				HashMap<String, State> restOfExpression = reduceExpression(a, t.getFrom());

				for (Map.Entry<String, State> e : restOfExpression.entrySet()) {
					ids.put(e.getKey(), e.getValue());
				}
			}
		}

		return ids;
	}


	public HashMap<String, State> reduceIds(Automaton a, State q) {
		HashMap<String, State> ids = new HashMap<String, State>();

		for (Transition t : a.getOutgoingTransitionsFrom(q)) {
			if (!(t.getInput().equals("="))) {
				HashMap<String, State> pids = reduceIds(a, t.getTo());

				for (Map.Entry<String, State> pid : pids.entrySet()) {
					ids.put(t.getInput() + pid.getKey(), pid.getValue());
				}
			} else {
				ids.put("", t.getTo());

			}
		}

		return ids;
	}


	public HashMap<String, State> reduceIdExpression(Automaton a, State q) {
		HashMap<String, State> ids = new HashMap<String, State>();

		for (Transition t : a.getOutgoingTransitionsFrom(q)) {
			if (isIdLetter(t.getInput())) {
				HashMap<String, State> pids = reduceIdExpression(a, t.getTo());

				for (Map.Entry<String, State> pid : pids.entrySet()) {
					ids.put(t.getInput() + pid.getKey(), pid.getValue());
				}
			} else if (t.getInput().equals(";")){
				ids.put("", t.getTo());
			} else {
				HashMap<String, State> restOfExpression = reduceExpression(a, t.getFrom());

				for (Map.Entry<String, State> e : restOfExpression.entrySet()) {
					ids.put(e.getKey(), e.getValue());
				}
			}
		}

		return ids;
	}

	private boolean isCipher(String n) {
		return n.equals("0") ||
				n.equals("1") ||
				n.equals("2") ||
				n.equals("3") ||
				n.equals("4") ||
				n.equals("5") ||
				n.equals("6") ||
				n.equals("7") ||
				n.equals("8") ||
				n.equals("9");
	}

	private boolean isIdLetter(String c) {
		return c.equals("a") ||
				c.equals("b") ||
				c.equals("c") ||
				c.equals("d") ||
				c.equals("e") ||
				c.equals("f") ||
				c.equals("g") ||
				c.equals("h") ||
				c.equals("i") ||
				c.equals("j") ||
				c.equals("k") ||
				c.equals("l") ||
				c.equals("m") ||
				c.equals("n") ||
				c.equals("o") ||
				c.equals("p") ||
				c.equals("q") ||
				c.equals("r") ||
				c.equals("s") ||
				c.equals("t") ||
				c.equals("u") ||
				c.equals("v") ||
				c.equals("w") ||
				c.equals("y") ||
				c.equals("x") ||
				c.equals("z") ||
				c.equals("A") ||
				c.equals("B") ||
				c.equals("C") ||
				c.equals("D") ||
				c.equals("E") ||
				c.equals("F") ||
				c.equals("G") ||
				c.equals("H") ||
				c.equals("I") ||
				c.equals("L") ||
				c.equals("M") ||
				c.equals("N") ||
				c.equals("O") ||
				c.equals("P") ||
				c.equals("Q") ||
				c.equals("R") ||
				c.equals("S") ||
				c.equals("T") ||
				c.equals("U") ||
				c.equals("V") ||
				c.equals("Z") ||
				c.equals("W") ||
				c.equals("X") ||
				c.equals("Y") ||
				c.equals("J") ||
				c.equals("K");
	}
	
	public Automaton normalizeAutomaton(Automaton a) {
		return normalizeAutomatonAux(a, 0);
	}
	
	public Automaton normalizeAutomatonAux(Automaton a, int n) {

		HashSet<Triple<HashSet<State>, State, State>> SCCs = a.extendedTarjan();
		for (Triple<HashSet<State>, State, State> scc : SCCs) {
		
			HashSet<State> exits = a.exitStates(scc.getLeft());
			
			exits.remove(scc.getRight()); // Remove real exit node
			exits.remove(scc.getMiddle()); // Remove entry node

			if (exits.size() > 0) {
				for (State o : exits) {
					HashSet<Transition> outgoing = a.getOutgoingTransitionsFrom(o);
					HashSet<Transition> toRemove = new HashSet<Transition>();

					for (Transition out : outgoing) {
						if (scc.getLeft().contains(out.getTo())) 
							continue;
						
						State qf = new State("s" + n++, o.isInitialState(), o.isFinalState());
						
						a.addState(qf);
						a.addTransition(qf, out.getTo(), out.getInput());
						
						for (Transition in : a.getIncomingTransitionsTo(o)) {
							a.addTransition(in.getFrom(), qf, in.getInput());
						}

						toRemove.add(out);
						
					}
					a.removeTransitions(toRemove);
				}
				
				return normalizeAutomatonAux(new Automaton(a.getDelta(), a.getStates()), n);
			}
		}

		return a;

	}
}
