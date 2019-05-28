package it.univr.parsing;

import java.util.HashMap;
import org.apache.commons.lang3.tuple.*;

import java.util.HashSet;
import java.util.Map;

import it.univr.fsm.machine.Automaton;
import it.univr.fsm.machine.State;
import it.univr.fsm.machine.Transition;

public class AbstractParser {

	public int freshInt = 0; 

	public static void main(String[] args) {
		AbstractParser parser = new AbstractParser();
		Automaton a = Automaton.makeRealAutomaton("while(x<5){x=x+1;}");		
		System.err.println(parser.reduceProgram(a).automatonPrint());
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
			} else {
				newTransition = new Transition(entry, exit, regex);

				for (Transition t : r.getOutgoingTransitionsFrom(entry)) 
					r.addTransition(new Transition(exit, t.getTo(), t.getInput()));
			}

			r.addTransition(newTransition);

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

		HashSet<State> visited = new HashSet<State>();
		do {
			State q = null;

			for (State w : W) {
				q = w;
				break;
			}

			W.remove(q);

			HashSet<Triple<State, String, State>> stmts = reduceStatement(a, q);

			for (Triple<State, String, State> t : stmts) {

				State from = t.getLeft().clone();
				State to = t.getRight().clone();

				states.add(from);
				states.add(to);

				visited.add(t.getLeft());

				delta.add(new Transition(from, to, t.getMiddle()));
				W.add(t.getRight());
				W.removeAll(visited);
			}


		} while (!W.isEmpty());


		Automaton result = new Automaton(delta, states);
		return result;
	}


	public HashSet<Triple<State, String, State>> reduceStatement(Automaton a, State q) {
		HashSet<Triple<State, String, State>> stmts = new HashSet<Triple<State, String, State>>();

		HashSet<State> mayWhile = mayReduceWhile(a, q);

		if (!mayWhile.isEmpty()) {
			for(State reachedState : mayWhile) {

				HashMap<String, State> startingBodyStates = reduceBooleanGuard(a, reachedState, 1);

				HashSet<Triple<State, String, State>> triplesToAdd = new HashSet<Triple<State, String, State>>();

				for (Map.Entry<String, State> b : startingBodyStates.entrySet()) {

					HashSet<Triple<State, String, State>> pbodies = reduceStatement(a, b.getValue());

					HashSet<State> entryPoints = new HashSet<State>();
					HashSet<State> exitPoints = new HashSet<State>();
					HashSet<State> W = new HashSet<State>();


					for (Triple<State, String, State> tr : pbodies) 
						W.add(tr.getRight());

					while (!W.isEmpty()) {

						State curr = null;
						for (State w : W) {
							curr = w; break;
						}

						HashSet<Triple<State, String, State>> exitBodies = reduceStatement(a, curr);
						pbodies.addAll(exitBodies);

						for (Triple<State, String, State> tr : exitBodies) 
							W.add(tr.getRight());



						W.remove(curr);

					}


					exitPoints = new HashSet<State>();
					entryPoints = new HashSet<State>();

					boolean notAnEntryPoint = false;
					boolean notAnExitPoint = false;

					for (Triple<State, String, State> tr : pbodies) {
						notAnEntryPoint = false;
						notAnExitPoint = false;
						for (Triple<State, String, State> tr1 : pbodies) {
							if (tr.getLeft().equals(tr1.getRight()))
								notAnEntryPoint = true;
							if (tr.getRight().equals(tr1.getLeft()))
								notAnExitPoint = true;
						}

						if (!notAnEntryPoint)
							entryPoints.add(tr.getLeft());

						if (!notAnExitPoint)
							exitPoints.add(tr.getRight());
					}

					HashSet<State> fakeExitStates = new HashSet<State>();

					for (State exit : exitPoints)
						for (State exitP : exitPoints)
							for (Transition t : a.getIncomingTransitionsTo(exitP))
								if (t.getFrom().equals(exit))
									fakeExitStates.add(exit);

					exitPoints.removeAll(fakeExitStates);

					State freshEntryState = new State("f" + freshInt++, false, false);

					//					HashMap<State, State> mapping = new HashMap<State, State>();

					for (Triple<State, String, State> body : pbodies) {	
						State freshExitLoop = new State("f" + freshInt++, false, false);

						//						if (mapping.get(body.getLeft()) != null)
						//							freshExitLoop = mapping.get(body.getLeft()); 
						//						else {
						//							mapping.put(body.getLeft(), freshExitLoop);
						//						}					

						if (entryPoints.contains(body.getLeft())) {

							stmts.add(Triple.of(q.equals(body.getRight()) ? freshExitLoop : q , "", freshEntryState));
							stmts.add(Triple.of(freshEntryState, "(" + b.getKey(), b.getValue().equals(body.getRight()) ? freshExitLoop : b.getValue()));
							stmts.add(Triple.of(freshExitLoop, "", freshEntryState));

							for (Triple<State, String, State> tr : pbodies) {
								Triple<State, String, State> trToAdd = tr;

								if (exitPoints.contains(tr.getLeft()))
									trToAdd = Triple.of(freshExitLoop, tr.getMiddle(), tr.getRight());

								if (exitPoints.contains(tr.getRight()))
									trToAdd = Triple.of(tr.getLeft(), tr.getMiddle(), freshExitLoop);

								triplesToAdd.add(trToAdd);

							}	
						}

						for (Transition t: a.getOutgoingTransitionsFrom(body.getRight()))
							if (exitPoints.contains(body.getRight()))
								stmts.add(Triple.of(freshEntryState, "!(" + b.getKey(), t.getTo().equals(body.getRight()) ? freshExitLoop : t.getTo()));					

					}

				}

				stmts.addAll(triplesToAdd);

				Automaton a_copy = a.clone();

				a_copy.removeTransitions(a_copy.getIncomingTransitionsTo(reachedState));
				a_copy.minimize();

				stmts.addAll(reduceStatement(a_copy, q));
			}

			return stmts;
		}

		else {

			return reduceAssignment(a, q, 0);
		}
	}




	private HashMap<String, State> reduceBooleanGuard(Automaton a, State q, int opcl) {

		HashMap<String, State> guards = new HashMap<String, State>();



		for (Transition t : a.getOutgoingTransitionsFrom(q)) {

			int opcl_c = opcl; 
			if (t.getInput().equals("("))
				opcl_c++;
			else if (t.getInput().equals(")"))
				opcl_c--;

			HashMap<String, State> pguards = reduceExpression(a, t.getTo(), opcl_c);

			for (Map.Entry<String, State> pid : pguards.entrySet()) 
				guards.put(t.getInput() + pid.getKey(), pid.getValue());
		}

		return guards;
	}


	public HashSet<State> mayReduceWhile(Automaton a, State q) {

		HashSet<State> result = new HashSet<State>();

		for (Transition t: a.getOutgoingTransitionsFrom(q))
			if (t.getInput().equals("w"))
				for (Transition t1: a.getOutgoingTransitionsFrom(t.getTo()))
					if (t1.getInput().equals("h"))
						for (Transition t2: a.getOutgoingTransitionsFrom(t1.getTo()))
							if (t2.getInput().equals("i"))
								for (Transition t3: a.getOutgoingTransitionsFrom(t2.getTo()))
									if (t3.getInput().equals("l"))
										for (Transition t4: a.getOutgoingTransitionsFrom(t3.getTo()))
											if (t4.getInput().equals("e"))
												for (Transition t5: a.getOutgoingTransitionsFrom(t4.getTo()))
													if (t5.getInput().equals("("))
														result.add(t5.getTo());
		return result;
	}


	public HashMap<String, State> reduceExpression(Automaton a, State q, int opcl) {
		HashMap<String, State> exps = new HashMap<String, State>();
		HashMap<String, State> pexps = new HashMap<String, State>();

		for (Transition t : a.getOutgoingTransitionsFrom(q)) {

			switch(t.getInput()) {

			case "(":
				opcl++;

				pexps = reduceExpression(a, t.getTo(), opcl);

				for (Map.Entry<String, State> entry : pexps.entrySet()) 
					exps.put(t.getInput() + entry.getKey(), entry.getValue());					

				break;
			case ")":


				opcl--;

				pexps = reduceExpression(a, t.getTo(), opcl);

				for (Map.Entry<String, State> entry : pexps.entrySet()) 
					exps.put(t.getInput() + entry.getKey(), entry.getValue());					

				break;

			case "+":  
			case "*":  
			case "-":  
			case "/": 
			case "<":
			case ">":

				pexps = reduceExpression(a, t.getTo(), opcl);

				for (Map.Entry<String, State> entry : pexps.entrySet()) {
					exps.put(t.getInput() + entry.getKey(), entry.getValue());
				}

				break;


			default:
				if (t.getInput().equals("{")) {
					pexps.put("", t.getTo());

					for (Map.Entry<String, State> entry : pexps.entrySet()) 
						exps.put(entry.getKey(), entry.getValue());	

					break;
				} else {
					pexps = reduceValue(a, t.getFrom(), opcl);

					for (Map.Entry<String, State> entry : pexps.entrySet()) 
						exps.put(entry.getKey(), entry.getValue());	
				}
			}
		}
		//		}

		return exps;
	}

	public HashMap<String, State> reduceValue(Automaton a, State q, int opcl) {
		HashMap<String, State> pvals = new HashMap<String, State>();

		for (Transition t : a.getOutgoingTransitionsFrom(q)) {
			if (isCipher(t.getInput()))
				pvals.putAll(reduceInteger(a,q, opcl));
			else if (isIdLetter(t.getInput()))
				pvals.putAll(reduceIdExpression(a,q, opcl));

		}

		return pvals;
	}

	public HashSet<Triple<State, String, State>> reduceAssignment(Automaton a, State q, int opcl) {
		HashMap<String, State> ids = reduceIds(a, q);

		HashSet<Triple<State, String, State>> asg = new HashSet<Triple<State, String, State>>();

		for (Map.Entry<String, State> t : ids.entrySet()) {
			HashMap<String, State> values = reduceExpression(a, t.getValue(), opcl);

			for (Map.Entry<String, State> p : values.entrySet()) {
				if (!t.getKey().isEmpty())
					asg.add(Triple.of(q, t.getKey() + " = " + p.getKey() + ";", p.getValue()));
			}

		}

		return asg;
	}

	public HashMap<String, State> reduceInteger(Automaton a, State q, int opcl) {
		HashMap<String, State> ids = new HashMap<String, State>();


		for (Transition t : a.getOutgoingTransitionsFrom(q)) {

			if (isCipher(t.getInput())) {

				HashMap<String, State> pids = reduceInteger(a, t.getTo(), opcl);

				for (Map.Entry<String, State> pid : pids.entrySet()) {
					ids.put(t.getInput() + pid.getKey(), pid.getValue());
				}
			} else if (t.getInput().equals(";")){

				ids.put("", t.getTo());
			} else {
				HashMap<String, State> restOfExpression = reduceExpression(a, t.getFrom(), opcl);

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
			if (isIdLetter(t.getInput())) {
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


	public HashMap<String, State> reduceIdExpression(Automaton a, State q, int opcl) {
		HashMap<String, State> ids = new HashMap<String, State>();

		for (Transition t : a.getOutgoingTransitionsFrom(q)) {
			if (isIdLetter(t.getInput())) {
				HashMap<String, State> pids = reduceIdExpression(a, t.getTo(), opcl);

				for (Map.Entry<String, State> pid : pids.entrySet()) {
					ids.put(t.getInput() + pid.getKey(), pid.getValue());
				}
			} else if (t.getInput().equals(";")){
				ids.put("", t.getTo());
			} else {
				HashMap<String, State> restOfExpression = reduceExpression(a, t.getFrom(), opcl);

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
