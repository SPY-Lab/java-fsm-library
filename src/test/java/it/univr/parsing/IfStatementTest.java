package it.univr.parsing;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;

import org.junit.Test;

import it.univr.fsm.machine.Automaton;
import it.univr.fsm.machine.State;
import it.univr.fsm.machine.Transition;

public class IfStatementTest {
	
	AbstractParser parser = new AbstractParser();

	
	@Test
	public void ifStatementTest001() {
		Automaton a = Automaton.makeRealAutomaton("if(x<5){x=1;x=2;}else{x=3;x=4;}");

		Automaton realResult = parser.reduceProgram(a);

		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, false);
		State q3 = new State("q3", false, false);
		State q4 = new State("q4", false, false);
		State q5 = new State("q5", false, false);
		State q6 = new State("q6", false, false);
		State q7 = new State("q7", false, true);

		states.add(q0);
		states.add(q1);
		states.add(q2);
		states.add(q3);
		states.add(q4);
		states.add(q5);
		states.add(q6);
		states.add(q7);

		System.err.println(new Automaton(delta,states));
		System.err.println(realResult);
		assertTrue(new Automaton(delta,states).equals(realResult)); 
	}
}
