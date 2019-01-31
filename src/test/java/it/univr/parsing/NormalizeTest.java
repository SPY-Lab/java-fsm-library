package it.univr.parsing;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;

import org.junit.Test;

import it.univr.fsm.machine.Automaton;
import it.univr.fsm.machine.State;
import it.univr.fsm.machine.Transition;

public class NormalizeTest {
	
	AbstractParser parser = new AbstractParser();
	
	@Test
	public void reduceCycles001() {
		
		Automaton a = Automaton.concat(Automaton.concat(Automaton.makeRealAutomaton("x=5"), Automaton.star("5")), Automaton.makeRealAutomaton(";"));
			
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, false);
		State q3 = new State("q3", false, false);
		State q4 = new State("q4", false, false);
		State q5 = new State("q5", false, true);


		states.add(q0);
		states.add(q1);
		states.add(q2);
		states.add(q3);
		states.add(q4);
		states.add(q5);

		delta.add(new Transition(q0, q1, "x"));
		delta.add(new Transition(q1, q2, "="));
		delta.add(new Transition(q2, q3, "5"));
		delta.add(new Transition(q3, q4, "(5)*"));
		delta.add(new Transition(q4, q5, ";"));
		
		assertEquals(new Automaton(delta, states), parser.reduceCycles(a));
	}

	@Test
	public void reduceCycles002() {
		
		Automaton a = Automaton.concat(Automaton.concat(Automaton.makeRealAutomaton("x=5"), Automaton.star("56")), Automaton.makeRealAutomaton(";"));

		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, false);
		State q3 = new State("q3", false, false);
		State q4 = new State("q4", false, false);
		State q5 = new State("q5", false, true);


		states.add(q0);
		states.add(q1);
		states.add(q2);
		states.add(q3);
		states.add(q4);
		states.add(q5);

		delta.add(new Transition(q0, q1, "x"));
		delta.add(new Transition(q1, q2, "="));
		delta.add(new Transition(q2, q3, "5"));
		delta.add(new Transition(q3, q5, ";"));
		delta.add(new Transition(q3, q4, "(56)*56"));
		delta.add(new Transition(q4, q5, ";"));
		
		assertEquals(new Automaton(delta, states), parser.reduceCycles(a));
	}
	@Test
	public void reduceCycles003() {
		
		Automaton a = Automaton.concat(Automaton.concat(Automaton.makeRealAutomaton("x=5"), Automaton.star("567")), Automaton.makeRealAutomaton(";"));

		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, false);
		State q3 = new State("q3", false, false);
		State q4 = new State("q4", false, false);
		State q5 = new State("q5", false, true);


		states.add(q0);
		states.add(q1);
		states.add(q2);
		states.add(q3);
		states.add(q4);
		states.add(q5);

		delta.add(new Transition(q0, q1, "x"));
		delta.add(new Transition(q1, q2, "="));
		delta.add(new Transition(q2, q3, "5"));
		delta.add(new Transition(q3, q5, ";"));
		delta.add(new Transition(q3, q4, "(567)*567"));
		delta.add(new Transition(q4, q5, ";"));
		
		assertEquals(new Automaton(delta, states), parser.reduceCycles(a));
	}
}
