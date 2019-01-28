package it.univr.parsing;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;

import org.junit.Test;

import it.univr.fsm.machine.Automaton;
import it.univr.fsm.machine.State;
import it.univr.fsm.machine.Transition;

public class SingleAssignmentTest {

	AbstractParser parser = new AbstractParser();
	
	@Test
	public void singleAssignmentTest001() {
		
		Automaton a = Automaton.makeRealAutomaton("x=5;");
		
		Automaton realResult = parser.reduceProgram(a);
		
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, true);

		states.add(q0);
		states.add(q1);
		
		delta.add(new Transition(q0, q1, "x = 5;"));
		
		assertEquals(new Automaton(delta,states), realResult); 
	}
	
	@Test
	public void singleAssignmentTest002() {
		
		Automaton a = Automaton.union(Automaton.makeRealAutomaton("x=7;"),Automaton.makeRealAutomaton("x=5;"));
		
		Automaton realResult = parser.reduceProgram(a);
		
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, true);

		states.add(q0);
		states.add(q1);
		
		delta.add(new Transition(q0, q1, "x = 5;"));
		delta.add(new Transition(q0, q1, "x = 7;"));
		
		assertEquals(new Automaton(delta,states), realResult); 
	}
	
	@Test
	public void singleAssignmentTest003() {
		
		Automaton a = Automaton.union(Automaton.makeRealAutomaton("xxx=7;"),Automaton.makeRealAutomaton("x=5;"));
		
		Automaton realResult = parser.reduceProgram(a);
		
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, true);

		states.add(q0);
		states.add(q1);
		
		delta.add(new Transition(q0, q1, "x = 5;"));
		delta.add(new Transition(q0, q1, "xxx = 7;"));
		
		assertEquals(new Automaton(delta,states), realResult); 
	}
	
	@Test
	public void singleAssignmentTest004() {
		
		Automaton a = Automaton.union(Automaton.makeRealAutomaton("y=7;"),Automaton.makeRealAutomaton("x=7;"));
		
		Automaton realResult = parser.reduceProgram(a);
		
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, true);

		states.add(q0);
		states.add(q1);
		
		delta.add(new Transition(q0, q1, "x = 7;"));
		delta.add(new Transition(q0, q1, "y = 7;"));
		
		assertEquals(new Automaton(delta,states), realResult); 
	}
	
	@Test
	public void singleAssignmentTest005() {
		
		Automaton a = Automaton.makeRealAutomaton("x=1+1;");
		
		Automaton realResult = parser.reduceProgram(a);
		
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, true);

		states.add(q0);
		states.add(q1);
		
		delta.add(new Transition(q0, q1, "x = 1+1;"));
		
		assertEquals(new Automaton(delta,states), realResult); 
	}
	

	
	@Test
	public void singleAssignmentTest006() {
		
		Automaton a = Automaton.union(Automaton.makeRealAutomaton("x=1+1;"), Automaton.makeRealAutomaton("x=1+2;"));
		
		Automaton realResult = parser.reduceProgram(a);
		
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, true);

		states.add(q0);
		states.add(q1);
		
		delta.add(new Transition(q0, q1, "x = 1+1;"));
		delta.add(new Transition(q0, q1, "x = 1+2;"));
		
		assertEquals(new Automaton(delta,states), realResult); 
	}
	

	@Test
	public void singleAssignmentTest007() {
		
		Automaton a = Automaton.union(Automaton.makeRealAutomaton("x=x+1;"), Automaton.makeRealAutomaton("x=x+2;"));
		
		Automaton realResult = parser.reduceProgram(a);
		
	
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, true);

		states.add(q0);
		states.add(q1);
		
		delta.add(new Transition(q0, q1, "x = x+1;"));
		delta.add(new Transition(q0, q1, "x = x+2;"));
		
		assertEquals(new Automaton(delta,states), realResult); 
	}
	
	@Test
	public void singleAssignmentTest008() {
		
		Automaton a = Automaton.union(Automaton.makeRealAutomaton("x=x+y;"), Automaton.makeRealAutomaton("x=x+z;"));
		
		Automaton realResult = parser.reduceProgram(a);
		
	
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, true);

		states.add(q0);
		states.add(q1);
		
		delta.add(new Transition(q0, q1, "x = x+y;"));
		delta.add(new Transition(q0, q1, "x = x+z;"));
		
		assertEquals(new Automaton(delta,states), realResult); 
	}
	
	@Test
	public void singleAssignmentTest009() {
		
		Automaton a = Automaton.union(Automaton.makeRealAutomaton("x=x+y-z;"), Automaton.makeRealAutomaton("x=x+z-y;"));
		
		Automaton realResult = parser.reduceProgram(a);
		
	
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, true);

		states.add(q0);
		states.add(q1);
		
		delta.add(new Transition(q0, q1, "x = x+y-z;"));
		delta.add(new Transition(q0, q1, "x = x+z-y;"));
		
		assertEquals(new Automaton(delta,states), realResult); 
	}


	@Test
	public void singleAssignmentTest010() {
		
		Automaton a = Automaton.union(Automaton.makeRealAutomaton("x=x+y*z;"), Automaton.makeRealAutomaton("x=x+z*y;"));
		
		Automaton realResult = parser.reduceProgram(a);
		
	
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, true);

		states.add(q0);
		states.add(q1);
		
		delta.add(new Transition(q0, q1, "x = x+y*z;"));
		delta.add(new Transition(q0, q1, "x = x+z*y;"));
		
		assertEquals(new Automaton(delta,states), realResult); 
	}

	@Test
	public void singleAssignmentTest011() {
		
		Automaton a = Automaton.union(Automaton.makeRealAutomaton("y=x+y*z;"), Automaton.makeRealAutomaton("z=x+z*y;"));
		
		Automaton realResult = parser.reduceProgram(a);
		
	
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, true);

		states.add(q0);
		states.add(q1);
		
		delta.add(new Transition(q0, q1, "y = x+y*z;"));
		delta.add(new Transition(q0, q1, "z = x+z*y;"));
		
		assertEquals(new Automaton(delta,states), realResult); 
	}

	@Test
	public void singleAssignmentTest012() {
		
		Automaton a = Automaton.union(Automaton.makeRealAutomaton("y=x+y*z;"), Automaton.makeRealAutomaton("z=x+z*y;"));
		
		Automaton realResult = parser.reduceProgram(a);
		
	
		HashSet<State> states = new HashSet<State>();
		HashSet<Transition> delta = new HashSet<Transition>();

		State q0 = new State("q0", true, false);
		State q1 = new State("q1", false, true);

		states.add(q0);
		states.add(q1);
		
		delta.add(new Transition(q0, q1, "y = x+y*z;"));
		delta.add(new Transition(q0, q1, "z = x+z*y;"));
		
		assertEquals(new Automaton(delta,states), realResult); 
	}
}
