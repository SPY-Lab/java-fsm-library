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
	public void multipleAssignmentsTest001() {

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
	public void multipleAssignmentsTest002() {

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
	public void multipleAssignmentsTest003() {

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
	public void multipleAssignmentsTest004() {

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
	
	@Test
	public void multipleAssignmentsTest005() {
		
		Automaton a = Automaton.union(Automaton.makeRealAutomaton("x=1+1;y=2;"), Automaton.makeRealAutomaton("x=1+2;z=33;"));
		
		Automaton realResult = parser.reduceProgram(a);
		
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, false);
		State q3 = new State("q3", false, true);

		states.add(q0);
		states.add(q1);
		states.add(q2);
		states.add(q3);
		
		delta.add(new Transition(q0, q1, "x = 1+1;"));
		delta.add(new Transition(q1, q3, "y = 2;"));
		delta.add(new Transition(q2, q3, "z = 33;"));
		delta.add(new Transition(q0, q2, "x = 1+2;"));
		
		assertEquals(new Automaton(delta,states), realResult); 
	}
	
	
	@Test
	public void multipleAssignmentsTest006() {
		
		Automaton a = Automaton.union(Automaton.makeRealAutomaton("x=2-1;y=2;"), Automaton.makeRealAutomaton("x=1+2;z=33;"));
		
		Automaton realResult = parser.reduceProgram(a);
		
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, false);
		State q3 = new State("q3", false, true);

		states.add(q0);
		states.add(q1);
		states.add(q2);
		states.add(q3);
		
		delta.add(new Transition(q0, q1, "x = 2-1;"));
		delta.add(new Transition(q1, q3, "y = 2;"));
		delta.add(new Transition(q2, q3, "z = 33;"));
		delta.add(new Transition(q0, q2, "x = 1+2;"));
		
		assertEquals(new Automaton(delta,states), realResult); 
	}
	

	
	@Test
	public void multipleAssignmentsTest007() {
		
		Automaton a = Automaton.union(Automaton.makeRealAutomaton("x=1*1;y=2;"), Automaton.makeRealAutomaton("x=1+2;z=33;"));
		
		Automaton realResult = parser.reduceProgram(a);
		
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, false);
		State q3 = new State("q3", false, true);

		states.add(q0);
		states.add(q1);
		states.add(q2);
		states.add(q3);
		
		delta.add(new Transition(q0, q1, "x = 1*1;"));
		delta.add(new Transition(q1, q3, "y = 2;"));
		delta.add(new Transition(q2, q3, "z = 33;"));
		delta.add(new Transition(q0, q2, "x = 1+2;"));
		
		assertEquals(new Automaton(delta,states), realResult); 
	}
	
	@Test
	public void multipleAssignmentsTest008() {
		
		Automaton a = Automaton.union(Automaton.makeRealAutomaton("y=x+y*z;y=5;"), Automaton.makeRealAutomaton("z=x+z*y;y=5;"));
		
		Automaton realResult = parser.reduceProgram(a);
		
	
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, true);

		states.add(q0);
		states.add(q1);
		states.add(q2);
		
		delta.add(new Transition(q0, q1, "y = x+y*z;"));
		delta.add(new Transition(q0, q1, "z = x+z*y;"));
		delta.add(new Transition(q1, q2, "y = 5;"));
		
		assertEquals(new Automaton(delta,states), realResult); 
	}
	
	@Test
	public void multipleAssignmentsTest009() {
		
		Automaton a = Automaton.union(Automaton.makeRealAutomaton("y=x+y*1;y=5;"), Automaton.makeRealAutomaton("z=x+2*y;y=5;"));
		
		Automaton realResult = parser.reduceProgram(a);
		
	
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, true);

		states.add(q0);
		states.add(q1);
		states.add(q2);
		
		delta.add(new Transition(q0, q1, "y = x+y*1;"));
		delta.add(new Transition(q0, q1, "z = x+2*y;"));
		delta.add(new Transition(q1, q2, "y = 5;"));
		
		assertEquals(new Automaton(delta,states), realResult); 
	}

	@Test
	public void multipleAssignmentsTest010() {
		
		Automaton a = Automaton.concat(Automaton.concat(Automaton.makeRealAutomaton("x=5"), Automaton.union(Automaton.star("4"), Automaton.star("7"))), Automaton.makeRealAutomaton("8;"));
		
		a = Automaton.concat(a, Automaton.makeRealAutomaton("y=6;"));
		Automaton realResult = parser.reduceProgram(a);
		
	
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, true);

		states.add(q0);
		states.add(q1);
		states.add(q2);
		
		delta.add(new Transition(q0, q1, "x = 54(4)*8;"));
		delta.add(new Transition(q0, q1, "x = 57(7)*8;"));
		delta.add(new Transition(q0, q1, "x = 58;"));
		delta.add(new Transition(q1, q2, "y = 6;"));
		
		assertEquals(new Automaton(delta,states), realResult); 
	}
	
	@Test
	public void multipleAssignmentsTest011() {
		
		
		Automaton a = Automaton.concat(Automaton.concat(Automaton.makeRealAutomaton("x=5"), Automaton.union(Automaton.star("4"), Automaton.star("7"))), Automaton.makeRealAutomaton("8;"));
		
		a = Automaton.concat(a, Automaton.union(Automaton.makeRealAutomaton("y=6;"), Automaton.makeRealAutomaton("y=7;")));
		Automaton realResult = parser.reduceProgram(a);
		
	
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, true);

		states.add(q0);
		states.add(q1);
		states.add(q2);
		
		delta.add(new Transition(q0, q1, "x = 54(4)*8;"));
		delta.add(new Transition(q0, q1, "x = 57(7)*8;"));
		delta.add(new Transition(q0, q1, "x = 58;"));
		delta.add(new Transition(q1, q2, "y = 6;"));
		delta.add(new Transition(q1, q2, "y = 7;"));
		
		assertEquals(new Automaton(delta,states), realResult); 
	}
	
	@Test
	public void multipleAssignmentsTest012() {
		
		
		Automaton a = Automaton.concat(Automaton.concat(Automaton.makeRealAutomaton("x=5"), Automaton.star("5")), Automaton.makeRealAutomaton(";"));
		a = Automaton.concat(a, Automaton.concat(Automaton.concat(Automaton.makeRealAutomaton("y=6"), Automaton.star("6")), Automaton.makeRealAutomaton(";")));
		
		Automaton realResult = parser.reduceProgram(a);
		
	
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, false);
		State q2 = new State("q2", false, true);

		states.add(q0);
		states.add(q1);
		states.add(q2);
		
		delta.add(new Transition(q0, q1, "x = 5(5)*;"));
		delta.add(new Transition(q1, q2, "y = 6(6)*;"));
		
		assertEquals(new Automaton(delta,states), realResult); 
	}
}
