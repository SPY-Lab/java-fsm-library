package it.univr.parsing;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;

import org.junit.Test;

import it.univr.fsm.machine.Automaton;
import it.univr.fsm.machine.State;
import it.univr.fsm.machine.Transition;

public class MultipleAssignmentTest {

	AbstractParser parser = new AbstractParser();

	@Test
	public void singleAssignmentTest001() {

		Automaton a = Automaton.union(Automaton.makeRealAutomaton("x=7;y=5;"),Automaton.makeRealAutomaton("x=9;"));

		Automaton realResult = parser.reduceProgram(a);

		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, true);

		states.add(q0);
		states.add(q1);
		states.add(q2);

		delta.add(new Transition(q0, q1, "x = 7;"));
		delta.add(new Transition(q1, q2, "y = 5;"));
		delta.add(new Transition(q0, q2, "x = 9;"));
		
		assertEquals(new Automaton(delta,states), realResult); 
	}

	@Test
	public void singleAssignmentTest002() {

		Automaton a = Automaton.union(Automaton.makeRealAutomaton("x=7;y=5;"),Automaton.makeRealAutomaton("x=9;y=5;"));

		Automaton realResult = parser.reduceProgram(a);

		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, true);

		states.add(q0);
		states.add(q1);
		states.add(q2);

		delta.add(new Transition(q0, q1, "x = 7;"));
		delta.add(new Transition(q0, q1, "x = 9;"));
		delta.add(new Transition(q1, q2, "y = 5;"));

		
		assertEquals(new Automaton(delta,states), realResult); 
	}

	@Test
	public void singleAssignmentTest003() {

		Automaton a = Automaton.union(Automaton.makeRealAutomaton("xx=7;y=5;"),Automaton.makeRealAutomaton("x=9;y=5;"));

		Automaton realResult = parser.reduceProgram(a);

		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, true);

		states.add(q0);
		states.add(q1);
		states.add(q2);

		delta.add(new Transition(q0, q1, "xx = 7;"));
		delta.add(new Transition(q0, q1, "x = 9;"));
		delta.add(new Transition(q1, q2, "y = 5;"));

		
		assertEquals(new Automaton(delta,states), realResult); 
	}


	@Test
	public void singleAssignmentTest004() {

		Automaton a = Automaton.union(Automaton.makeRealAutomaton("a=1;b=2;c=3;"),Automaton.makeRealAutomaton("a=4;b=5;c=6;"));

		Automaton realResult = parser.reduceProgram(a);

		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, false);
		State q3 = new State("q3", false, true);
		State q4 = new State("q4", false, false);
		State q5 = new State("q5", false, false);

		states.add(q0);
		states.add(q1);
		states.add(q2);
		states.add(q3);
		states.add(q4);
		states.add(q5);

		delta.add(new Transition(q0, q1, "a = 1;"));
		delta.add(new Transition(q1, q2, "b = 2;"));
		delta.add(new Transition(q2, q3, "c = 3;"));
		delta.add(new Transition(q0, q4, "a = 4;"));
		delta.add(new Transition(q4, q5, "b = 5;"));
		delta.add(new Transition(q5, q3, "c = 6;"));

		assertEquals(new Automaton(delta,states), realResult); 
	}
	
}
