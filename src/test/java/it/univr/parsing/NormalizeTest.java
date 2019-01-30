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
	public void singleAssignmentTest012() {
		
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, false);
		State q3 = new State("q3", false, false);
		State q4 = new State("q4", false, false);
		State q5 = new State("q5", false, false);
		State q6 = new State("q6", false, true);
		
		states.add(q0);
		states.add(q1);
		states.add(q2);
		states.add(q3);
		states.add(q4);
		states.add(q5);
		states.add(q6);
		

		delta.add(new Transition(q0, q1, "x"));
		delta.add(new Transition(q1, q2, "="));
		delta.add(new Transition(q2, q3, "5"));
		delta.add(new Transition(q3, q4, "6"));
		delta.add(new Transition(q4, q5, "7"));
		delta.add(new Transition(q5, q3, "5"));
		delta.add(new Transition(q5, q6, ";"));
		delta.add(new Transition(q4, q6, ";"));
		
		
		Automaton a = new Automaton(delta, states);
		System.out.println(parser.normalizeAutomaton(a).automatonPrint());
	}
}
