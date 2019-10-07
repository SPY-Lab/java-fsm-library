package it.univr.parsing;

import java.util.HashMap;
import org.apache.commons.lang3.tuple.*;

import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.univr.fsm.machine.Automaton;
import it.univr.fsm.machine.State;
import it.univr.fsm.machine.Transition;

public class AbstractParser {

	public int freshInt = 0; 


	public static void main(String[] args) {
		AbstractParser parser = new AbstractParser();
		Automaton five = Automaton.concat(Automaton.star("5"), Automaton.makeRealAutomaton(";"));
		Automaton a = Automaton.concat(Automaton.makeRealAutomaton("x=5"), five);

		a = Automaton.concat(Automaton.makeRealAutomaton("if(x<5){"), a);
		a = Automaton.concat(a, Automaton.makeRealAutomaton("}else{x=1;}"));
		Automaton psfa = parser.reduceProgram(a);
		Automaton sfa = parser.toASFA(psfa);

		System.err.println(sfa);
	}

	public Automaton toASFA(Automaton aut) {

		Automaton result = aut.clone();

		OmegaPredicate omega = new OmegaPredicate();
		omega.add(new OmegaAssignmentPredicate("x", "+"));
		omega.add(new OmegaGuardPredicate("x", "<", "5"));
		omega.add(new OmegaNegatedGuardPredicate("x", "<", "5"));

		HashSet<Transition> modified = new HashSet<Transition>();

		for (Transition t : aut.getDelta())
			for (SingleOmegaPredicate o : omega) {

				if (Automaton.isContained(parseRegex(t.getInput()), o.evaluate()) && !modified.contains(t)) {
					modified.add(t);
					t.setInput(o.toString());
				}
			}

		return result;
	}

	private Automaton parseRegex(String regex) {

		Automaton result = null;

		if (regex.contains("*")) {
			Pattern p = Pattern.compile("(\\w)=([-,+]?)(\\d)\\(\\d\\)\\*;");
			Matcher matcher = p.matcher(regex.replace(" ", ""));
			
			matcher.find();
			
			if (matcher.group(2).isEmpty() || matcher.group(2).equals("+")) {
				Automaton a = Automaton.concat(Automaton.star(matcher.group(3)), Automaton.makeAutomaton(";"));
				a = Automaton.concat(Automaton.makeAutomaton(matcher.group(3)), a);
				a = Automaton.concat(Automaton.makeAutomaton(matcher.group(1)+ "="), a);
				result = a;
			}
			
		} else {
			result = Automaton.makeRealAutomaton(regex.replace(" ", ""));
		}

		return result;
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
		
		visited.add(a.getInitialState());
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
		HashSet<State> mayIf = mayReduceIf(a, q);

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


					State freshEntryState = new State("i" + freshInt++, false, false);
					State freshBodyEntryState = new State("i" + freshInt++, false, false);

					//HashMap<State, State> mapping = new HashMap<State, State>();

					for (Triple<State, String, State> body : pbodies) {	
						State freshExitLoop = new State("o" + freshInt++, false, false);

						//						if (mapping.get(body.getLeft()) != null)
						//							freshExitLoop = mapping.get(body.getLeft()); 
						//						else {
						//							mapping.put(body.getLeft(), freshExitLoop);
						//						}					

						if (entryPoints.contains(body.getLeft())) {

							stmts.add(Triple.of(q.equals(body.getRight()) ? freshExitLoop : q , "", freshEntryState));
							stmts.add(Triple.of(freshEntryState, "(" + b.getKey(), freshBodyEntryState));
							stmts.add(Triple.of(freshExitLoop, "", freshEntryState));

							for (Triple<State, String, State> tr : pbodies) {
								Triple<State, String, State> trToAdd = tr;


								if (exitPoints.contains(trToAdd.getLeft()))
									trToAdd = Triple.of(freshExitLoop, trToAdd.getMiddle(), trToAdd.getRight());

								if (exitPoints.contains(trToAdd.getRight()))
									trToAdd = Triple.of(trToAdd.getLeft(), trToAdd.getMiddle(), freshExitLoop);

								if (entryPoints.contains(trToAdd.getLeft()))
									trToAdd = Triple.of(freshBodyEntryState, trToAdd.getMiddle(), trToAdd.getRight());

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
		} else if (!mayIf.isEmpty()) {
			for(State reachedState : mayIf) {

				HashMap<String, State> startingTrueBodyState = reduceBooleanGuard(a, reachedState, 1);
				HashSet<Triple<State, String, State>> triplesToAdd = new HashSet<Triple<State, String, State>>();
				for (Map.Entry<String, State> b : startingTrueBodyState.entrySet()) {
					HashSet<Triple<State, String, State>> pTrueBodies = reduceStatement(a, b.getValue());

					HashSet<State> trueExitPoints = new HashSet<State>();
					HashSet<State> falseExitPoints = new HashSet<State>();
					HashSet<State> W = new HashSet<State>();


					for (Triple<State, String, State> tr : pTrueBodies) 
						W.add(tr.getRight());

					while (!W.isEmpty()) {

						State curr = null;
						for (State w : W) {
							curr = w; break;
						}

						HashSet<Triple<State, String, State>> trueExitBodies = reduceStatement(a, curr);
						pTrueBodies.addAll(trueExitBodies);

						for (Triple<State, String, State> tr : trueExitBodies) 
							W.add(tr.getRight());

						W.remove(curr);
					}

					falseExitPoints = new HashSet<State>();
					trueExitPoints = new HashSet<State>();

					boolean notAnEntryPoint = false;
					boolean notAnExitPoint = false;

					for (Triple<State, String, State> tr : pTrueBodies) {
						notAnEntryPoint = false;
						notAnExitPoint = false;

						for (Triple<State, String, State> tr1 : pTrueBodies) {
							if (tr.getLeft().equals(tr1.getRight()))
								notAnEntryPoint = true;
							if (tr.getRight().equals(tr1.getLeft()))
								notAnExitPoint = true;
						}

						if (!notAnEntryPoint)
							trueExitPoints.add(tr.getLeft());

						if (!notAnExitPoint)
							falseExitPoints.add(tr.getRight());
					}

					HashSet<State> fakeTrueExitStates = new HashSet<State>();

					for (State exit : falseExitPoints)
						for (State exitP : falseExitPoints)
							for (Transition t : a.getIncomingTransitionsTo(exitP))
								if (t.getFrom().equals(exit))
									fakeTrueExitStates.add(exit);

					falseExitPoints.removeAll(fakeTrueExitStates);

					State freshTrueEntryState = new State("i" + freshInt++, false, false);
					State freshBodyEntryState = new State("i" + freshInt++, false, false);

					State freshExitIf = new State("o" + freshInt++, false, false);				

					for (Triple<State, String, State> trueBody : pTrueBodies) {	

						if (trueExitPoints.contains(trueBody.getLeft())) {

							stmts.add(Triple.of(q.equals(trueBody.getRight()) ? freshExitIf : q , "", freshTrueEntryState));
							stmts.add(Triple.of(freshTrueEntryState, "(" + b.getKey(), freshBodyEntryState));

							for (Triple<State, String, State> tr : pTrueBodies) {
								Triple<State, String, State> trToAdd = tr;


								if (falseExitPoints.contains(trToAdd.getLeft()))
									trToAdd = Triple.of(freshExitIf, trToAdd.getMiddle(), trToAdd.getRight());

								if (falseExitPoints.contains(trToAdd.getRight()))
									trToAdd = Triple.of(trToAdd.getLeft(), trToAdd.getMiddle(), freshExitIf);

								if (trueExitPoints.contains(trToAdd.getLeft()))
									trToAdd = Triple.of(freshBodyEntryState, trToAdd.getMiddle(), trToAdd.getRight());

								triplesToAdd.add(trToAdd);

							}

						} 


						// False branch
						if (falseExitPoints.contains(trueBody.getRight())) {
							HashSet<State> mayElse = mayReduceElse(a, trueBody.getRight());

							for (State elseState : mayElse) {
								HashSet<Triple<State, String, State>> pFalseBodies = reduceStatement(a, elseState);

								HashSet<State> ptrueExitPoints = new HashSet<State>();
								HashSet<State> pfalseExitPoints = new HashSet<State>();
								HashSet<State> pW = new HashSet<State>();

								for (Triple<State, String, State> tr : pFalseBodies) 
									pW.add(tr.getRight());

								while (!pW.isEmpty()) {

									State curr = null;
									for (State w : pW) {
										curr = w; break;
									}

									HashSet<Triple<State, String, State>> falseExitBodies = reduceStatement(a, curr);
									pFalseBodies.addAll(falseExitBodies);

									for (Triple<State, String, State> tr : falseExitBodies) 
										W.add(tr.getRight());

									pW.remove(curr);
								}

								pfalseExitPoints = new HashSet<State>();
								ptrueExitPoints = new HashSet<State>();

								notAnEntryPoint = false;
								notAnExitPoint = false;

								for (Triple<State, String, State> tr : pFalseBodies) {
									notAnEntryPoint = false;
									notAnExitPoint = false;

									for (Triple<State, String, State> tr1 : pFalseBodies) {
										if (tr.getLeft().equals(tr1.getRight()))
											notAnEntryPoint = true;
										if (tr.getRight().equals(tr1.getLeft()))
											notAnExitPoint = true;
									}

									if (!notAnEntryPoint)
										ptrueExitPoints.add(tr.getLeft());

									if (!notAnExitPoint)
										pfalseExitPoints.add(tr.getRight());
								}

								HashSet<State> fakeFalseExitStates = new HashSet<State>();

								for (State exit : falseExitPoints)
									for (State exitP : falseExitPoints)
										for (Transition t : a.getIncomingTransitionsTo(exitP))
											if (t.getFrom().equals(exit))
												fakeFalseExitStates.add(exit);

								fakeFalseExitStates.removeAll(fakeTrueExitStates);

								State freshFalseBodyEntryState = new State("i" + freshInt++, false, false);

								for (Triple<State, String, State> falseBody : pFalseBodies) {	

									stmts.add(Triple.of(freshTrueEntryState , "!(" + b.getKey(), freshFalseBodyEntryState));


									for (Triple<State, String, State> tr : pFalseBodies) {
										Triple<State, String, State> trToAdd = tr;

										if (pfalseExitPoints.contains(trToAdd.getRight()))
											trToAdd = Triple.of(trToAdd.getLeft(), trToAdd.getMiddle(), freshExitIf);

										if (ptrueExitPoints.contains(trToAdd.getLeft()))
											trToAdd = Triple.of(freshFalseBodyEntryState, trToAdd.getMiddle(), trToAdd.getRight());

										triplesToAdd.add(trToAdd);

									}

									for (Transition t: a.getOutgoingTransitionsFrom(falseBody.getRight()))
										if (pfalseExitPoints.contains(falseBody.getRight()))
											stmts.add(Triple.of(freshExitIf, "", t.getTo().equals(falseBody.getRight()) ? freshExitIf : t.getTo()));					

								}
							}
						}
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

	public HashSet<State> mayReduceIf(Automaton a, State q) {

		HashSet<State> result = new HashSet<State>();

		for (Transition t: a.getOutgoingTransitionsFrom(q))
			if (t.getInput().equals("i"))
				for (Transition t1: a.getOutgoingTransitionsFrom(t.getTo()))
					if (t1.getInput().equals("f"))
						for (Transition t2: a.getOutgoingTransitionsFrom(t1.getTo()))
							if (t2.getInput().equals("("))
								result.add(t2.getTo());
		return result;
	}

	public HashSet<State> mayReduceElse(Automaton a, State q) {

		HashSet<State> result = new HashSet<State>();

		for (Transition t: a.getOutgoingTransitionsFrom(q))
			if (t.getInput().equals("}"))
				for (Transition t1: a.getOutgoingTransitionsFrom(t.getTo()))
					if (t1.getInput().equals("e"))
						for (Transition t2: a.getOutgoingTransitionsFrom(t1.getTo()))
							if (t2.getInput().equals("l"))
								for (Transition t3: a.getOutgoingTransitionsFrom(t2.getTo()))
									if (t3.getInput().equals("s"))
										for (Transition t4: a.getOutgoingTransitionsFrom(t3.getTo()))
											if (t4.getInput().equals("e"))
												for (Transition t5: a.getOutgoingTransitionsFrom(t4.getTo()))
													if (t5.getInput().equals("{"))
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
			case "<":
			case ">":
			case "&":
			case "|":

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

		return exps;
	}

	public HashMap<String, State> reduceValue(Automaton a, State q, int opcl) {
		HashMap<String, State> pvals = new HashMap<String, State>();

		for (Transition t : a.getOutgoingTransitionsFrom(q)) {
			if (isCipher(t.getInput()))
				pvals.putAll(reduceInteger(a,q, opcl));
			else if (isLetter(t.getInput()))
				pvals.putAll(reduceIdExpressionOrBooleans(a,q, opcl));
			else if (isDoubleQuote(t.getInput()))
				pvals.putAll(reduceString(a,q, opcl));			
		}

		return pvals;
	}

	private boolean isDoubleQuote(String input) {
		return input.equals("\"");
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

	public HashMap<String, State> reduceString(Automaton a, State q, int opcl) {
		HashMap<String, State> strings = new HashMap<String, State>();

		for (Transition t : a.getOutgoingTransitionsFrom(q)) {



			if (isLetter(t.getInput()) || isDoubleQuote(t.getInput())) {

				HashMap<String, State> pstrings = reduceString(a, t.getTo(), opcl);

				for (Map.Entry<String, State> ps : pstrings.entrySet()) {
					strings.put(t.getInput() + ps.getKey(), ps.getValue());
				}
			} else if (t.getInput().equals(";")){
				strings.put("", t.getTo());
			} else {

				HashMap<String, State> restOfExpression = reduceExpression(a, t.getFrom(), opcl);
				for (Map.Entry<String, State> e : restOfExpression.entrySet()) {
					strings.put(e.getKey(), e.getValue());
				}
			}
		}

		return strings;
	}


	public HashMap<String, State> reduceIds(Automaton a, State q) {
		HashMap<String, State> ids = new HashMap<String, State>();

		for (Transition t : a.getOutgoingTransitionsFrom(q)) {
			if (isLetter(t.getInput())) {
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


	public HashMap<String, State> reduceIdExpressionOrBooleans(Automaton a, State q, int opcl) {
		HashMap<String, State> ids = new HashMap<String, State>();

		for (Transition t : a.getOutgoingTransitionsFrom(q)) {
			if (isLetter(t.getInput())) {
				HashMap<String, State> pids = reduceIdExpressionOrBooleans(a, t.getTo(), opcl);

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

	private boolean isLetter(String c) {
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
