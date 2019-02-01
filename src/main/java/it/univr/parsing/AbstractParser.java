package it.univr.parsing;

import java.util.HashMap;
import org.apache.commons.lang3.tuple.*;

import java.util.HashSet;
import java.util.Map;

import it.univr.fsm.machine.Automaton;
import it.univr.fsm.machine.State;
import it.univr.fsm.machine.Transition;

public class AbstractParser {

	public static void main(String[] args) {
		AbstractParser parser = new AbstractParser();

		
		Automaton a = Automaton.concat(Automaton.concat(Automaton.makeRealAutomaton("x=5"), Automaton.star("5")), Automaton.makeRealAutomaton(";"));
		a = Automaton.concat(a, Automaton.concat(Automaton.concat(Automaton.makeRealAutomaton("y=6"), Automaton.star("6")), Automaton.makeRealAutomaton(";")));
		
		System.out.println(parser.reduceCycles(a));
	}


	public String buildRestrictedRegex(Automaton a, Triple<HashSet<State>, State, State> scc) {

		State entry = scc.getMiddle();
		State exit = scc.getRight();

		Automaton r = a.clone();

		for (State s : r.getStates()) {

			if (s.equals(entry))
				s.setInitialState(true);

			if (s.equals(exit))
				s.setFinalState(true);

			if (!s.equals(exit) && !s.equals(entry))   {
				s.setFinalState(false);
				s.setInitialState(false);
			}
		}

		r.removeUnreachableStates();

		HashSet<Transition> toRemove = new HashSet<Transition>();

		for (Transition t : r.getDelta())
			if (!scc.getLeft().contains(t.getFrom()) || !scc.getLeft().contains(t.getTo())) 
				toRemove.add(t);


		r.removeTransitions(toRemove);

		r.minimizeHopcroft();


		if (entry.equals(exit))
			return buildSCCRegex(r);
		else {
			String starRegex = buildSCCRegex(r);
			for (Transition t : r.getOutgoingTransitionsFrom(exit))
				r.removeTransition(t);

			return starRegex + "" + buildPlainRegex(r);
		}
	}

	public Automaton reduceCycles(Automaton a) {
		int n = 0;

		Automaton r = normalizeAutomaton(a);
		
		for (Triple<HashSet<State>, State, State> scc : r.extendedTarjan()) {
			String regex = buildRestrictedRegex(r, scc);
			
			State entry = scc.getMiddle();
			State exit = scc.getRight();
			HashSet<State> sccc = scc.getLeft();

			Transition newTransition = new Transition(entry, exit, regex);

			if (entry.equals(exit)) {
				State fresh = new State("w" + n++, entry.isInitialState(), entry.isFinalState());

				r.addState(fresh);

				for (Transition t : r.getOutgoingTransitionsFrom(entry)) {
					if (!sccc.contains(t.getTo())) {
						r.addTransition(fresh, t.getTo(), t.getInput());
						r.removeTransition(t);
					}
				}

				newTransition = new Transition(entry, fresh, regex);
				r.addTransition(newTransition);	
			} else {
				newTransition = new Transition(entry, exit, regex);
				r.addTransition(newTransition);

				for (Transition t : r.getOutgoingTransitionsFrom(entry))
					r.addTransition(new Transition(exit, t.getTo(), t.getInput()));
			}


			HashSet<Transition> ts = new HashSet<Transition>();

			for (Transition t : r.getDelta())
				if (sccc.contains(t.getTo()) && sccc.contains(t.getFrom()) && !t.equals(newTransition)) {
					ts.add(t);
				}

			r.removeTransitions(ts);

		}

		r.minimize();	
	
		return r;
	}

	public Automaton reduceProgram(Automaton a) {

		// Normalize automaton cycles
		a = reduceCycles(a);
		
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
		return 	n.matches("\\d")||n.matches("\\(\\d\\)") || n.matches("\\(\\d\\)\\*\\d?");
	}

	private boolean isIdLetter(String c) {
		return c.matches("[a-z]|[A-Z]");
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

	private String buildSCCRegex(Automaton a) {

		String result = "";
		State curr = a.getInitialState();
		HashSet<State> marked = new HashSet<State>();

		while (!marked.contains(curr)) {

			marked.add(curr);

			for (Transition t : a.getOutgoingTransitionsFrom(curr)) {
				result += t.getInput();
				curr = t.getTo();
			}
		}

		return "(" + result + ")*";
	}

	private String buildPlainRegex(Automaton a) {

		String result = "";
		State curr = a.getInitialState();
		HashSet<State> marked = new HashSet<State>();

		while (!marked.contains(curr)) {

			marked.add(curr);

			for (Transition t : a.getOutgoingTransitionsFrom(curr)) {
				result += t.getInput();
				curr = t.getTo();
			}
		}

		return result;
	}
}
